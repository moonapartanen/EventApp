package com.example.moona.eventapp;

/**
 * Created by moona on 28.11.2017.
 */

public class Data {

    private String mName;
    private String mDate;
    private String mDescription;
    private String mImageUrl;

    public Data(String mName, String mDate, String mDescription, String mImageUrl)
    {
        this.mName = mName;
        this.mDate = mDate;
        this.mDescription = mDescription;
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
}
