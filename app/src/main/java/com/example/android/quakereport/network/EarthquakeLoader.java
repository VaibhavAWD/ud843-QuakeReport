package com.example.android.quakereport.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.model.Earthquake;
import com.example.android.quakereport.utils.QueryUtils;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mStringUrl;

    public EarthquakeLoader(Context context, String stringUrl) {
        super(context);
        mStringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (mStringUrl == null) {
            return null;
        }

        return QueryUtils.fetchEarthquakes(mStringUrl);
    }
}
