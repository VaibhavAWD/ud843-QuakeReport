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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.android.quakereport.adapter.EarthquakeAdapter;
import com.example.android.quakereport.model.Earthquake;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link EarthquakeAdapter} of earthquakes
        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(this, getEarthquakeList());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapter);
    }

    private ArrayList<Earthquake> getEarthquakeList() {
        ArrayList<Earthquake> earthquakeList = new ArrayList<>();
        earthquakeList.add(new Earthquake(7.9, "San Fransisco", "Nov 12, 2016"));
        earthquakeList.add(new Earthquake(6.1, "London", "Jan 21, 2015"));
        earthquakeList.add(new Earthquake(5.4, "Mexico City", "May 30, 2014"));
        earthquakeList.add(new Earthquake(4.8, "Tokyo", "Jun 15, 2013"));
        earthquakeList.add(new Earthquake(6.3, "San Fransisco", "Aug 21, 2012"));
        earthquakeList.add(new Earthquake(5.3, "Hong Kong", "Oct 16, 2011"));
        earthquakeList.add(new Earthquake(7.1, "Rio de Janeiro", "Dec 11, 2010"));
        earthquakeList.add(new Earthquake(4.9, "Paris", "Apr 19, 2009"));
        earthquakeList.add(new Earthquake(5.7, "Moscow", "Feb 26, 2008"));
        earthquakeList.add(new Earthquake(6.2, "Havana", "Jul 28, 2007"));

        return earthquakeList;
    }
}
