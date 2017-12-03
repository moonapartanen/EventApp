package com.example.moona.eventapp;

/**
 * Created by moona on 28.11.2017.
 */

public class Keyword {

    private String mName;
    private String mKeywordId;

    public Keyword(String mName, String mKeywordId)
    {
        this.mName = mName;
        this.mKeywordId = mKeywordId;
    }

    public String getmName() {
        return mName;
    }

    public String getmKeywordId() {
        return mKeywordId;
    }

}
