package com.example.android.quakereport.utils;

import android.util.Log;

import com.example.android.quakereport.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final int READ_TIMEOUT = 10000; /* milliseconds */
    private static final int CONNECTION_TIMEOUT = 15000; /* milliseconds */
    private static final String REQUEST_METHOD = "GET";
    private static final int RESPONSE_CODE_OK = 200;
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns the list of {@link Earthquake} fetched by making HTTP request
     * and by parsing the JSON response.
     *
     * @param stringUrl to fetch the earthquakes from.
     */
    public static List<Earthquake> fetchEarthquakes(String stringUrl) {
        URL requestUrl = createNewUrl(stringUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream.", e);
        }

        return extractEarthquakes(jsonResponse);
    }

    /**
     * Creates a new {@link URL} from the string.
     *
     * @param stringUrl from which the URL needs to be created.
     * @return {@link URL} for fetching the earthquake results.
     */
    private static URL createNewUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Failed to create URL.", e);
        }
        return url;
    }

    /**
     * Make HTTP request and retrieve the earthquake JSON response.
     *
     * @param requestUrl to make network request and fetch results from.
     * @return JSON response in form of String
     */
    private static String makeHttpRequest(URL requestUrl) throws IOException {
        String jsonResponse = "";

        // Exit early if the URL is null
        if (requestUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            // Read the input stream and parse the response
            // if the response code is 200 (OK)
            if (urlConnection.getResponseCode() == RESPONSE_CODE_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving earthquake results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} to String which contains the JSON response from
     * the server.
     *
     * @param inputStream received by making the network request.
     * @return JSON response in form of String
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName(CHARSET_NAME));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the retrieved JSON response. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Get root json object
            JSONObject main = new JSONObject(jsonResponse);

            // Get json array 'features'
            JSONArray features = main.optJSONArray("features");

            // Get all features using loop
            for (int i = 0; i < features.length(); i++) {
                // Get current earthquake data
                JSONObject currentEarthquake = features.optJSONObject(i);

                // Get json object 'properties'
                JSONObject properties = currentEarthquake.optJSONObject("properties");

                // Get magnitude of the earthquake
                double magnitude = properties.optDouble("mag");

                // Get name of the place where earthquake took place
                String place = properties.optString("place");

                // Get time when the earthquake occurred
                long time = properties.optLong("time");

                // Get url of the earthquake report
                String url = properties.optString("url");

                // Add the earthquake to the list
                earthquakes.add(new Earthquake(magnitude, place, time, url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
