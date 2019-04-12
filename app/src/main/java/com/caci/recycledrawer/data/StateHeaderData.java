package com.caci.recycledrawer.data;

/**
 * Object containing data which will power the View to populate a State's Header display
 * in the RecyclerView.  This should populate the state_header_layout.xml fields
 */
public class StateHeaderData {
    /**
     * Name of the state
     */
    private String name;

    /**
     * Number of states
     */
    private int stateCount;

    /**
     * Constructor
     * @param name name of the state
     */
    public StateHeaderData(String name, int count){
        this.name = name;
        this.stateCount = count;
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

    public int getStateCount() {
        return stateCount;
    }

    public void setStateCount(int stateCount) {
        this.stateCount = stateCount;
    }
}
