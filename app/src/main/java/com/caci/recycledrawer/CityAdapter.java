package com.caci.recycledrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caci.recycledrawer.data.CityHeaderData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Recyclerview adapter to handle a list of cities.  Two types of rows available:
 *   - City Header: A header object with back button
 *   - City Item: A row object for each city name
 */
public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * List of city names
     */
    private List<Object> mCities = new ArrayList<>();

    /**
     * Click listener for a city row item
     */
    private RecyclerViewClickListener mCityRowListener;

    /**
     * Click listener for the back button in the city header
     */
    private View.OnClickListener mCityBackListener;

    /**
     * Constants to identify the type of view to be inflated
     */
    private static final int CITY_HEADER = 1;
    private static final int LOCATION_ITEM = 2;

    /**
     * Constructor
     * @param cityList - List of city names (String), one CityHeaderData object on the front of the list
     * @param cityClickListener - Listener for clicking a city name
     * @param cityBackListener - listener for clicking the back button in the header
     */
    public CityAdapter(List<Object> cityList, RecyclerViewClickListener cityClickListener, View.OnClickListener cityBackListener){
        mCities = cityList;
        mCityRowListener = cityClickListener;
        mCityBackListener = cityBackListener;
    }

    /**
     * Crete the view holders
     * @param parent parent view
     * @param viewType type: either a header or city item
     * @return viewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder holder;
        if(viewType == CITY_HEADER){
            View v = LayoutInflater.from(context).inflate(R.layout.city_header_layout, parent, false);
            holder = new CityHeaderViewHolder(v, mCityBackListener);
        } else{
            View v = LayoutInflater.from(context).inflate(R.layout.city_row_layout, parent, false);
            holder = new CityViewHolder(v);
        }
        return holder;
    }

    /**
     * Bind view holder - populate the city row data from the given position based on the type
     * of ViewHolder that it receives
     * @param holder view holder
     * @param position position in the list that we're creating
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CityHeaderViewHolder){
            bindCityHeader(holder, position);
        } else if(holder instanceof CityViewHolder){
            bindCityRow(holder, position);
        }
    }

    /**
     * Bind a city header object
     * @param holder CityHeaderViewHolder view holder
     * @param position position
     */
    private void bindCityHeader(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof CityHeaderViewHolder){
            CityHeaderViewHolder viewHolder = (CityHeaderViewHolder)holder;
            viewHolder.getHeaderText().setText("Cities");
        }
    }

    /**
     * Bind a city row object
     * @param holder String view holder with city names
     * @param position position
     */
    private void bindCityRow(RecyclerView.ViewHolder holder, int position){
        if(holder instanceof CityViewHolder){
            CityViewHolder viewHolder = (CityViewHolder)holder;
            String city = (String)mCities.get(position);
            viewHolder.getName().setText(city);
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
        if(mCities.get(position) instanceof CityHeaderData){
            return CITY_HEADER;
        } else if(mCities.get(position) instanceof String){
            return LOCATION_ITEM;
        }
        return -1;
    }

    /**
     * Returns the number of cities in the city list
     * @return
     */
    @Override
    public int getItemCount() {
        return mCities.size();
    }

    /**
     * Clear all city names from the stored list
     */
    public void clear(){
        mCities.clear();
    }

    /**
     * Getters and setters
     * @return
     */
    public List<Object> getmCities() {
        return mCities;
    }

    public void setmCities(List<Object> mCities) {
        this.mCities = mCities;
    }

    public RecyclerViewClickListener getmCityRowListener() {
        return mCityRowListener;
    }

    public void setmCityRowListener(RecyclerViewClickListener mCityRowListener) {
        this.mCityRowListener = mCityRowListener;
    }

    public View.OnClickListener getmCityBackListener() {
        return mCityBackListener;
    }

    public void setmCityBackListener(View.OnClickListener mCityBackListener) {
        this.mCityBackListener = mCityBackListener;
    }

    /**
     * View holder for a city header object.  Contains basic data about the city to be
     * displayed at the top of the RecyclerView
     * -----------------------------------------------------------------------------------
     */
    public class CityHeaderViewHolder extends RecyclerView.ViewHolder{

        /**
         * Header text
         */
        TextView headerText;

        /**
         * Back button
         */
        Button backButton;

        /**
         * Constructor
         * @param itemView
         */
        public CityHeaderViewHolder(@NonNull final View itemView, View.OnClickListener backListener) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.city_header_name);
            backButton = (Button) itemView.findViewById(R.id.city_header_back_button);
            backButton.setOnClickListener(backListener);
        }

        /**
         * Getters and setters
         * @return
         */
        public TextView getHeaderText() {
            return headerText;
        }

        public void setHeaderText(TextView name) {
            this.headerText = name;
        }
    }

    /**
     * View holder for an individual city item
     * -------------------------------------------
     */
    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /**
         * Name of the city
         */
        TextView name;

        /**
         * Required constructor
         * @param itemView given view
         */
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.city_name);
            itemView.setOnClickListener(this);
        }

        /**
         * Click listener
         * @param view - clicked view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCityRowListener.onClick(adapterPosition);
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
