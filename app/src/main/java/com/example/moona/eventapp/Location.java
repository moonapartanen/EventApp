package com.example.moona.eventapp;

/**
 * Created by moona on 28.11.2017.
 */

public class Location {

    private String mLocation;
    private String mLocationId;

    public Location(String mName, String mLocationId)
    {
        this.mLocation = mName;
        this.mLocationId = mLocationId;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmLocationId() {
        return mLocationId;
    }

}
