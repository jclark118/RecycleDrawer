package com.caci.recycledrawer.data;

/**
 * Object containing data which will power the View to populate a City's Header display
 * in the RecyclerView.  This should populate the city_header_layout.xml fields
 */
public class CityHeaderData {
    /**
     * Name of the city
     */
    private String name;

    /**
     * Constructor
     * @param name name of the city
     */
    public CityHeaderData(String name){
        this.name = name;
    }

    /**
     * Getters and setters
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
