package com.caci.recycledrawer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a RecyclerView of a list of locations and gives it a bottom drawer behavior
 */
public class MainActivity extends AppCompatActivity {

    /**
     * List of location names
     */
    private List<String> locations = new ArrayList<>();

    /**
     * RecyclerView that will hold our locations
     */
    private RecyclerView locationRecycler;

    /**
     * Adapter for the location recycler view
     */
    private LocationAdapter locationAdapter;

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
     * Populate our location list
     */
    private void generateLocations(){
        for(int i=0; i<50; i++){
            String location = "City " + i;
            locations.add(location);
        }
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
        locationAdapter = new LocationAdapter(locations);
        locationRecycler.setAdapter(locationAdapter);
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
