package com.example.android.quakereport.model;

public class Earthquake {
    private double mMagnitude;
    private String mPlace;
    private long mTime;

    public Earthquake(double magnitude, String place, long time) {
        mMagnitude = magnitude;
        mPlace = place;
        mTime = time;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public long getTime() {
        return mTime;
    }
}
