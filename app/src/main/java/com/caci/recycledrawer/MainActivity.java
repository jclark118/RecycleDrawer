package com.caci.recycledrawer;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Creates a RecyclerView of a list of locations and gives it a bottom drawer behavior
 */
public class MainActivity extends AppCompatActivity implements LocationAdapter.RecyclerViewClickListener {

    /**
     * List of location names
     */
    private List<String> locations = new ArrayList<>();

    /**
     * List of city names
     */
    private List<String> cities = new ArrayList<>();

    /**
     * RecyclerView that will hold our locations
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
     * Create the view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create floating action button
        createFab();

        // Generate a list of locations
        generateLocations();

        // Create the RecyclerView
        createRecyclerView();

    }


    /**
     * open the locations file and parse
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
     * Populate our location list from the json data
     */
    private void generateLocations(){
        try {
            JSONArray array = new JSONArray(loadLocations());
            for(int i=0; i<array.length(); i++){
                JSONObject row = array.getJSONObject(i);
                if(!locations.contains(row.get("state"))) {
                    locations.add(row.get("state").toString());
                    Collections.sort(locations);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        generateCities();
    }

    private void generateCities(){
        cities = new ArrayList<>();
        cities.add("Austin");
        cities.add("Boston");
        cities.add("Denver");
        cities.add("Herndon");
        cities.add("LA");
    }

    /**
     * Create the recycler view containing our locations
     */
    private void createRecyclerView(){
        // Attach the xml
        locationRecycler = (RecyclerView) findViewById(R.id.recycler_locations);

        // Create a layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        locationRecycler.setLayoutManager(layoutManager);

        // Create the adapter
        locationAdapter = new LocationAdapter(locations, this);
        locationRecycler.setAdapter(locationAdapter);
    }

    /**
     * On click listener - show which location is clicked
     * @param position - the clicked location
     */
    @Override
    public void onClick(int position) {
        Context context = this;
//        Toast.makeText(context, "removing: " + String.valueOf(position), Toast.LENGTH_SHORT)
//                .show();
        removeLocation(position);
//        removeAllLocations();
    }

    /**
     * Remove a location from our list, and remove it from the RecyclerView
     * @param position - the item to remove
     */
    public void removeLocation(int position){
        locations.remove(position);
        locationAdapter.notifyItemRemoved(position);
    }

    /**
     * Remove all locations from the list, and remove them from the RecyclerView
     */
    public void removeAllLocations(){
        locations.clear();
        locationAdapter.clear();
        locationAdapter.notifyDataSetChanged();
        putCitiesInRecycler();
    }

    /**
     * Give the list of cities to the adapter and bind
     */
    public void putCitiesInRecycler(){
        cityAdapter = new LocationAdapter(cities, this);
        locationRecycler.swapAdapter(cityAdapter, true);
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
