/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quakereport.adapter.EarthquakeAdapter;
import com.example.android.quakereport.model.Earthquake;
import com.example.android.quakereport.network.EarthquakeLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=20";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter mEarthquakeAdapter;

    @BindView(R.id.list)
    ListView mEarthquakeListView;

    @BindView(R.id.text_empty_state)
    TextView mDisplayEmptyState;

    @BindView(R.id.progress_indicator)
    ProgressBar mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ButterKnife.bind(this);

        // Create a new {@link EarthquakeAdapter} of earthquakes
        mEarthquakeAdapter =
                new EarthquakeAdapter(getApplicationContext(), new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(mEarthquakeAdapter);

        // Set empty view to the ListView so that in case
        // of no earthquakes were found or there is no
        // internet connection then we can show something
        // to the user to understand what exactly happened
        // instead of showing blank screen
        mEarthquakeListView.setEmptyView(mDisplayEmptyState);

        // Check internet connectivity before making network calls
        if (hasConnection()) {
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            hideProgressIndicator();
            mDisplayEmptyState.setText(getString(R.string.error_no_internet_connection));
        }

        mEarthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake earthquake = (Earthquake) adapterView.getAdapter().getItem(position);
                Uri earthquakeUrl = Uri.parse(earthquake.getUrl());

                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(earthquakeUrl);
                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(EarthquakeActivity.this, R.string.warn_no_app_found,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case EARTHQUAKE_LOADER_ID:
                return new EarthquakeLoader(this, REQUEST_URL);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        hideProgressIndicator();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            updateUi(earthquakes);
        } else {
            mDisplayEmptyState.setText(R.string.error_no_earthquakes_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mEarthquakeAdapter.clear();
    }

    /**
     * Loads the earthquakes in the ListView.
     */
    private void updateUi(List<Earthquake> earthquakes) {
        mEarthquakeAdapter.clear();
        mEarthquakeAdapter.addAll(earthquakes);
    }

    private void hideProgressIndicator() {
        mProgressIndicator.setVisibility(View.GONE);
    }

    /**
     * Returns true or false by checking the internet connectivity.
     */
    private boolean hasConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }
}
