package com.example.android.quakereport.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Earthquake implements Parcelable {
    private double mMagnitude;
    private String mPlace;
    private long mTime;
    private String mUrl;

    public Earthquake(double magnitude, String place, long time, String url) {
        mMagnitude = magnitude;
        mPlace = place;
        mTime = time;
        mUrl = url;
    }

    protected Earthquake(Parcel in) {
        mMagnitude = in.readDouble();
        mPlace = in.readString();
        mTime = in.readLong();
        mUrl = in.readString();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public long getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mMagnitude);
        parcel.writeString(mPlace);
        parcel.writeLong(mTime);
        parcel.writeString(mUrl);
    }
}
