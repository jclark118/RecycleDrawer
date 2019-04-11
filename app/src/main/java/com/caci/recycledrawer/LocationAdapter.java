package com.caci.recycledrawer;

import android.content.Context;
import android.util.Log;
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
    List<String> mLocations = new ArrayList<>();

    /**
     * Click listener
     */
    private RecyclerViewClickListener mListener;

    /**
     * Constructor
     * @param locations - ArrayList of location names
     */
    public LocationAdapter(List<String> locations, RecyclerViewClickListener listener){
        mLocations = locations;
        mListener = listener;
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
        Log.i("bind","Binding location: " + position);
        holder.name.setText(mLocations.get(position));
    }

    /**
     * Get the number of locations
     * @return number of locations
     */
    @Override
    public int getItemCount() {
        return mLocations.size();
    }




    /**
     * View holder for an individual location item
     * -------------------------------------------
     */
    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            itemView.setOnClickListener(this);
        }

        /**
         * Click listener
         * @param view - clicked view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mListener.onClick(mLocations.get(adapterPosition));
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

    /**
     * Interface for the click listener on our recyclerview
     * ----------------------------------------------------
     */
    public interface RecyclerViewClickListener {
        void onClick(String name);
    }
}
