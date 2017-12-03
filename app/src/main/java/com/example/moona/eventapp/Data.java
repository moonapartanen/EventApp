package com.example.moona.eventapp;

/**
 * Created by moona on 28.11.2017.
 */

public class Data {

    private String mName;
    private String mDateAndLocation;
    private String mDescription;
    private String mImageUrl;
    private String mPhotographer;
    private String mUrl;
    private String mFullDescription;
    private String mEventId;

    public Data(String mName, String mDateAndLocation, String mDescription, String mImageUrl, String mEventId, String mPhotographer, String mUrl, String mFullDescription)
    {
        this.mName = mName;
        this.mDateAndLocation = mDateAndLocation;
        this.mDescription = mDescription;
        this.mImageUrl = mImageUrl;
        this.mEventId = mEventId;
        this.mPhotographer = mPhotographer;
        this.mUrl = mUrl;
        this.mFullDescription = mFullDescription;
    }

    public String getmName() {
        return mName;
    }

    public String getmDateAndLocation() {
        return mDateAndLocation;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmEventId() {
        return mEventId;
    }

    public String getmPhotographer() {
        return mPhotographer;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmFullDescription() {
        return mFullDescription;
    }
}
