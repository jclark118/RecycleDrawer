package com.caci.recycledrawer;

import android.os.Bundle;

import com.caci.recycledrawer.data.CityHeaderData;
import com.caci.recycledrawer.data.Location;
import com.caci.recycledrawer.data.StateHeaderData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextSwitcher;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a RecyclerView of a list of states and gives it a bottom drawer behavior.  When clicking
 * on a state, the RecyclerView will be emptied, assigned a new adapter, and populated with a list
 * of cities.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * List of location names
     * First object is a StateHeaderData
     * The rest are Strings of state names
     */
    private List<Object> states = new ArrayList<>();

    /**
     * List of city names
     * First object in each list is a CityHeaderData
     * The rest are names of cities as Strings
     */
    private Map<String, List<Object>> cities = new HashMap<>();

    /**
     * RecyclerView that will hold our states
     */
    private RecyclerView locationRecycler;

    /**
     * Adapter for the location recycler view
     */
    private LocationAdapter locationAdapter;

    /**
     * Adapter for putting cities in the recyclerview
     */
    private LocationAdapter cityAdapter;

    /**
     * Floating action button to create new location
     */
    private FloatingActionButton fab;

    /**
     * List of location objects to be build from the json data
     */
    private List<Location> locations = new ArrayList<>();

    /**
     * Click listener to be given to the recyclerview when a user clicks on a city
     */
    private LocationAdapter.RecyclerViewClickListener cityListerner;

    /**
     * Click listener to be given to the recyclerview when a user clicks on a state
     */
    private LocationAdapter.RecyclerViewClickListener stateListerner;




    /**
     * Create the View
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the state and city click listeners
        createListeners();

        // Create floating action button
        createFab();

        // Generate a list of states from the json file in the assests dir
        generateLocations();

        // Create the RecyclerView populated with states
        createRecyclerView();

    }




    /**
     * Create the recycler view containing our states
     */
    private void createRecyclerView(){
        // Attach the xml
        locationRecycler = (RecyclerView) findViewById(R.id.recycler_locations);

        // Create a layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        locationRecycler.setLayoutManager(layoutManager);

        // Create the adapter and set it for the recyclerview
        locationAdapter = new LocationAdapter(states, stateListerner);
        putStatesInRecycler();
    }






    /**
     * Switch the adapter for the recyclerview to the locationAdapter.  This will populate
     * the recyclerview with the list of states.
     */
    public void putStatesInRecycler(){
        locationRecycler.setAdapter(locationAdapter);
    }

    /**
     * Give the list of cities (by state) to the adapter and bind.  This will happen when
     * a user clicks on a state and it's time to wipe the recyclerview and recreate it with
     * matching city names
     */
    public void putCitiesInRecycler(String state){
        cityAdapter = new LocationAdapter(getCitiesByState(state), cityListerner);
        // Give the recyclerview a new adapter.  We can't use "swapAdapter" because that will
        // keep the same click listener as before, when we really need a new click listener to
        // know what to do when clicking on a city
        locationRecycler.setAdapter(cityAdapter);
//        locationRecycler.swapAdapter(cityAdapter, true);

    }

    /**
     * Create the listeners to be used in the onclick for the recyclerview
     * cityListener - what to do when a user clicks a city
     * statelistener - what to do when a user clicks a state
     */
    public void createListeners(){
        // Log which city was clicked on
        cityListerner = new LocationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.i("Click", "Clicked city: " + position);
                //removeAllLocations();
                putStatesInRecycler();
            }
        };

        // State listener will empty out all the state entries from the recyclerview, and attach
        // a new adapter to populate it with the list of cities for the state that was clicked on
        stateListerner = new LocationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.i("Click", "Clicked state: " + position);
                if(states.get(position) instanceof String) {
                    String state = (String)states.get(position);
                    //removeAllLocations();
                    putCitiesInRecycler(state);
                }
            }
        };
    }





    /**
     * open the cities file and parse
     */
    private String loadLocations(){
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Populate our location, city, and state list from the json data
     */
    private void generateLocations(){
        try {
            JSONArray array = new JSONArray(loadLocations());
            ArrayList<String> stateNames = new ArrayList<>();
            for(int i=0; i<array.length(); i++){
                JSONObject row = array.getJSONObject(i);
                String state = row.get("state").toString();
                String city = row.get("city").toString();

                // Add city and state basic names
                if(!stateNames.contains(state)) {
                    stateNames.add(state);
                }
                if(!cities.containsKey(state)){
                    List<Object> tempCity = new ArrayList<>();
                    // Create a header object with the state name and add it first
                    //CityHeaderData cityHeader = new CityHeaderData(state);
                    //tempCity.add(cityHeader);
                    tempCity.add(city);
                    cities.put(state, tempCity);
                }else{
                    cities.get(state).add(city);
                }

                // Add a new location object to our list
                Location loc = new Location(state, city);
                locations.add(loc);
            }
            // Sort the list of states and cities alphebetically
            createAndSortStatesAndCities(stateNames);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get a list of all cities belonging to the given state
     * @param state - the state to search for
     * @return - list of cities
     */
    private List<Object> getCitiesByState(String state){
        List<Object> cityList = new ArrayList<>();
        if(cities.containsKey(state)){
            cityList = cities.get(state);
        }
        return cityList;
    }

    /**
     * Sort both the States list and all cities lists in the city hashmap
     */
    public void createAndSortStatesAndCities(ArrayList<String> stateNames){
        // First sort the state names
        Collections.sort(stateNames);
        // Add a state header object followed by the sorted state list
        StateHeaderData stateHeader = new StateHeaderData("US States", stateNames.size());
        states.add(0, stateHeader);
        states.addAll(stateNames);

        // For every state, sort their city names
        for(String key : cities.keySet()){
            List<String> cityNames = new ArrayList<>();
            String currentState = key;
            for(Object cityObj : cities.get(key)){
                if(cityObj instanceof String){
                    cityNames.add((String)cityObj);
                }
            }
            //Sort the list of city names only
            Collections.sort(cityNames);
            CityHeaderData newHeader = new CityHeaderData(currentState);
            // Replace the current list
            cities.get(key).clear();
            cities.get(key).add(newHeader);
            cities.get(key).addAll(cityNames);
        }
    }

    /**
     * Set up the Floating action button
     */
    private void createFab(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Not needed but nice to reference in the future
     */

    /**
     * Remove a location from the RecyclerView
     * @param position - the item to remove
     */
    public void removeLocation(int position){
        locationAdapter.notifyItemRemoved(position);
    }

    /**
     * Remove all objects from the adapter, which will empty the recyclerview
     */
    public void removeAllLocations(){
        locationAdapter.clear();
        locationAdapter.notifyDataSetChanged();
    }
}
