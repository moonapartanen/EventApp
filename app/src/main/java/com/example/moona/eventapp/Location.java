package com.example.moona.eventapp;

/**
 * Created by moona on 28.11.2017.
 */

public class Location {

    private String mLocation;
    private String mId;

    public Location(String mName, String mId)
    {
        this.mLocation = mName;
        this.mId = mId;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmId() {
        return mId;
    }

}
