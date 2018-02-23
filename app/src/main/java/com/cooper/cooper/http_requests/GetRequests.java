package com.cooper.cooper.http_requests;

import android.os.AsyncTask;
import android.util.Log;

import com.cooper.cooper.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marco on 23/02/2018.
 */

public class GetRequests extends AsyncTask<String, String, JSONObject> {

    private JSONObject getData;

    public GetRequests(JSONObject getData) {
        this.getData = getData;
    }
    public GetRequests() {
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                return null;
            }

            JSONObject response = new JSONObject();
            response.put("status_code", statusCode);
            response.put("response", buffer.toString());

            return response;

        } catch (Exception e) {
            Log.d("Error", e.toString());
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }
    }
}
