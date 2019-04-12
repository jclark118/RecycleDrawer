package com.caci.recycledrawer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caci.recycledrawer.data.CityHeaderData;
import com.caci.recycledrawer.data.StateHeaderData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView adapter for the list of Locations
 * Handles 3 types of possible rows (ViewHolders)
 *   - State Header: A header object for when the list is a list of states
 *   - City Header: A header object for when the list is a list of cities
 *   - Location Item: just a string object that represents either a state or city name
 */
public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * List of location names
     */
    private List<Object> mLocations = new ArrayList<>();

    /**
     * Click listener
     */
    private RecyclerViewClickListener mListener;

    /**
     * Constants to identify the type of view to be inflated
     */
    private static final int STATE_HEADER = 1;
    private static final int CITY_HEADER = 2;
    private static final int LOCATION_ITEM = 3;

    /**
     * Constructor
     * @param locations - ArrayList of location names
     */
    public LocationAdapter(List<Object> locations, RecyclerViewClickListener listener){
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder holder;
        if(viewType == LOCATION_ITEM) {
            View v = LayoutInflater.from(context).inflate(R.layout.location_row_layout, parent, false);
            holder = new LocationViewHolder(v);
        } else if(viewType == STATE_HEADER){
            View v = LayoutInflater.from(context).inflate(R.layout.state_header_layout, parent, false);
            holder = new StateHeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.city_header_layout, parent, false);
            holder = new CityHeaderViewHolder(v);
        }
        return holder;
    }

    /**
     * Bind view holder - populate the row data from the given position based on the type
     * of ViewHolder that it receives
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof LocationViewHolder) {
            bindLocation(holder, position);
        } else if (holder instanceof StateHeaderViewHolder){
            bindStateHeader(holder, position);
        }else if (holder instanceof CityHeaderViewHolder){
            bindCityHeader(holder, position);
        }
    }

    /**
     * Bind a Location row object
     * @param holder a LocationViewHolder
     * @param position position of clicked element
     */
    private void bindLocation(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof LocationViewHolder){
            LocationViewHolder viewHolder = (LocationViewHolder)holder;
            String locString = (String)mLocations.get(position);
            viewHolder.getName().setText(locString);
        }
    }

    /**
     * Bind a State Header Object
     * @param holder StateViewHolder object
     * @param position should be 0
     */
    private void bindStateHeader(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof StateHeaderViewHolder){
            StateHeaderViewHolder viewHolder = (StateHeaderViewHolder)holder;
            viewHolder.getName().setText("State Header set");
        }
    }

    /**
     * Bind a City Header Object
     * @param holder CityViewHolder object
     * @param position should be 0
     */
    private void bindCityHeader(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof CityHeaderViewHolder){
            CityHeaderViewHolder viewHolder = (CityHeaderViewHolder)holder;
            viewHolder.getName().setText("City Header set");
        }
    }

    /**
     * Called by default on every row when constructing the list.  This will tell the create
     * method what type of ViewHolder to construct
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        if(mLocations.get(position) instanceof StateHeaderData){
            return STATE_HEADER;
        } else if(mLocations.get(position) instanceof String){
            return LOCATION_ITEM;
        } else if(mLocations.get(position) instanceof CityHeaderData){
            return CITY_HEADER;
        }
        return -1;
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
     *  Remove all locations
     */
    public void clear(){
        mLocations.clear();
    }

    /**
     * Getters and setters
     */
    public List<Object> getmLocations() {
        return mLocations;
    }

    public void setmLocations(List<Object> mLocations) {
        this.mLocations = mLocations;
    }

    public RecyclerViewClickListener getmListener() {
        return mListener;
    }

    public void setmListener(RecyclerViewClickListener mListener) {
        this.mListener = mListener;
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
            mListener.onClick(adapterPosition);
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
     * View holder for a city header object.  Contains basic data about the city to be
     * displayed at the top of the RecyclerView
     * -----------------------------------------------------------------------------------
     */
    public class CityHeaderViewHolder extends RecyclerView.ViewHolder{

        /**
         * Name of the City
         */
        TextView name;

        /**
         * Constructor
         * @param itemView
         */
        public CityHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.city_header_name);
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
     * View holder for a state header object.  Contains basic data about the state to be
     * displayed at the top of the RecyclerView
     * -----------------------------------------------------------------------------------
     */
    public class StateHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /**
         * Name of the state
         */
        TextView name;

        /**
         * Constructor
         * @param itemView
         */
        public StateHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.state_header_name);
        }

        /**
         * Click listener
         * @param view - clicked view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mListener.onClick(adapterPosition);
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
        void onClick(int position);
    }
}
