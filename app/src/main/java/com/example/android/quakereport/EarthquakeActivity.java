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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.quakereport.adapter.EarthquakeAdapter;
import com.example.android.quakereport.model.Earthquake;
import com.example.android.quakereport.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private static final String KEY_CURRENT_EARTHQUAKES = "KEY_CURRENT_EARTHQUAKES";

    private List<Earthquake> mEarthquakes;

    @BindView(R.id.list)
    ListView mEarthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ButterKnife.bind(this);

        /*
          Display the current earthquakes if the saved instance state is not null
          and contains list of earthquakes otherwise make network call and retrieve
          new earthquakes with respect to the request URL.
         */
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_EARTHQUAKES)) {
            mEarthquakes = savedInstanceState.getParcelableArrayList(KEY_CURRENT_EARTHQUAKES);
            updateUi();
        } else {
            // Check internet connectivity before making network calls
            if (hasConnection()) {
                new EarthquakeAsyncTask().execute();
            } else {
                Toast.makeText(this, R.string.error_no_internet_connection,
                        Toast.LENGTH_SHORT).show();
            }
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

    /**
     * Make HTTP request and fetch earthquakes on Background Thread and publish the
     * results on the UI Thread using {@link AsyncTask}.
     */
    private class EarthquakeAsyncTask extends AsyncTask<Void, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(Void... voids) {
            return QueryUtils.fetchEarthquakes(REQUEST_URL);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            mEarthquakes = earthquakes;
            updateUi();
        }

    }

    /**
     * Loads the earthquakes in the ListView.
     */
    private void updateUi() {
        // Create a new {@link EarthquakeAdapter} of earthquakes
        EarthquakeAdapter earthquakeAdapter =
                new EarthquakeAdapter(getApplicationContext(), mEarthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(earthquakeAdapter);
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

    /**
     * Store the current earthquakes in order to avoid making
     * repetitive network calls on config changes.
     */
    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(
                KEY_CURRENT_EARTHQUAKES,
                (ArrayList<? extends Parcelable>) mEarthquakes);
        super.onSaveInstanceState(currentState);
    }
}
