package com.caci.recycledrawer;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Creates a RecyclerView of a list of states and gives it a bottom drawer behavior
 */
public class MainActivity extends AppCompatActivity {

    /**
     * List of location names
     */
    private List<String> states = new ArrayList<>();

    /**
     * List of city names
     */
    private Map<String, List<String>> cities = new HashMap<>();

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
     * Create the view
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

        // Generate a list of states
        generateLocations();

        // Create the RecyclerView
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

        // Create the adapter
        locationAdapter = new LocationAdapter(states, stateListerner);
        locationRecycler.setAdapter(locationAdapter);
    }





    /**
     * Remove a location from the RecyclerView
     * @param position - the item to remove
     */
    public void removeLocation(int position){
//        states.remove(position);
        locationAdapter.notifyItemRemoved(position);
    }

    /**
     * Remove all states from the adapter, which will empty the recyclerview
     */
    public void removeAllLocations(){
        locationAdapter.clear();
        locationAdapter.notifyDataSetChanged();
    }

    /**
     * Give the list of cities (by state) to the adapter and bind
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
     */
    public void createListeners(){
        // Log which city was clicked on
        cityListerner = new LocationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.i("Click", "Clicked city: " + position);
            }
        };

        // State listener will empty out all the state entries from the recyclerview, and attach
        // a new adapter to populate it with the list of cities for the state that was clicked on
        stateListerner = new LocationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.i("Click", "Clicked state: " + position);
                String state = states.get(position);
                removeAllLocations();
                putCitiesInRecycler(state);
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
            for(int i=0; i<array.length(); i++){
                JSONObject row = array.getJSONObject(i);
                String state = row.get("state").toString();
                String city = row.get("city").toString();

                // Add city and state basic names
                if(!states.contains(state)) {
                    states.add(state);
                }
                if(!cities.containsKey(state)){
                    List<String> tempCity = new ArrayList<>();
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
            sortStatesAndCities();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get a list of all cities belonging to the given state
     * @param state - the state to search for
     * @return - list of cities
     */
    private List<String> getCitiesByState(String state){
        List<String> cityList = new ArrayList<>();
        if(cities.containsKey(state)){
            cityList = cities.get(state);
        }
        return cityList;
    }

    /**
     * Sort both the States list and all cities lists in the city hashmap
     */
    public void sortStatesAndCities(){
        Collections.sort(states);
        for(List<String> tempCities : cities.values()){
            Collections.sort(tempCities);
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
}
