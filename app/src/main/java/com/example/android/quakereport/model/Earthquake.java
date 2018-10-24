package com.example.android.quakereport.model;

public class Earthquake {
    private double mMagnitude;
    private String mPlace;
    private String mDate;

    public Earthquake(double magnitude, String place, String date) {
        mMagnitude = magnitude;
        mPlace = place;
        mDate = date;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public String getDate() {
        return mDate;
    }
}
