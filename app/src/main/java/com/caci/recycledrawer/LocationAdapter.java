package com.caci.recycledrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView adapter for the list of Locations
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    /**
     * List of location names
     */
    List<String> locations = new ArrayList<>();

    /**
     * Constructor
     * @param locations - ArrayList of location names
     */
    public LocationAdapter(List<String> locations){
        this.locations = locations;
    }

    /**
     * Creating ViewHolders
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.location_row_layout, parent, false);
        LocationViewHolder holder = new LocationViewHolder(v);
        return holder;
    }

    /**
     * Bind view holder - populate the row data from the given position
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.name.setText(locations.get(position));
    }

    /**
     * Get the number of locations
     * @return number of locations
     */
    @Override
    public int getItemCount() {
        return locations.size();
    }




    /**
     * View holder for an individual location item
     * -------------------------------------------
     */
    public class LocationViewHolder extends RecyclerView.ViewHolder{

        /**
         * Name of the Location
         */
        TextView name;

        /**
         * Required constructor
         * @param itemView given view
         */
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.location_name);
        }

        /**
         * Getters and setters
         * @return
         */
        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }
    }
}
