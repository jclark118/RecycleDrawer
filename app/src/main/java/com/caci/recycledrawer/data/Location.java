package com.caci.recycledrawer.data;

/**
 * A Location made up of:
 * City
 * Lat
 * Lon
 * Population
 * State
 */
public class Location {
    String city;
    String state;
    String lat;
    String lon;
    String population;

    public Location(String state, String city){
        this.state = state;
        this.city = city;
    }

    /**
     * Getters and setters
     * @return
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
}
