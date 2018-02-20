package com.cooper.cooper.http_requests;

/**
 * Created by marco on 20/02/2018.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cooper.cooper.CustomToast;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.Utils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marco on 18/02/2018.
 */

public class PostRequests extends AsyncTask<String, String, String> {

    private URL url;
    private JSONObject postData;
    private int statusCode;
    private Context context;
    private View view;

    public PostRequests(JSONObject postData, Context context) {
        if (postData != null) {
            this.postData = postData;
        }
        this.context = context;
    }
    public PostRequests(JSONObject postData, Context context, View view) {
        if (postData != null) {
            this.postData = postData;
        }
        this.context = context;
        this.view = view;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(strings[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            // OPTIONAL - Sets an authorization header
            //urlConnection.setRequestProperty("Authorization", "someAuthString");

            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                //writer.write(request.getBytes());
                writer.flush();
                writer.close();
            }

            this.statusCode = urlConnection.getResponseCode();

            if (this.statusCode ==  200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response_body = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response_body.append(inputLine);
                }
                Log.d("Response", response_body.toString());
                in.close();

                // From here you can convert the string to JSON with whatever JSON parser you like to use

                // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method

            } else {
                // Status code is not 200
                // Do something to handle the error
            }
            return this.statusCode+"";
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    protected void onPostExecute(String result) {
        if(result.equals("200")) {
            Intent intent = new Intent(this.context,MainMenu.class);
            this.context.startActivity(intent);
        } else {
            new CustomToast().Show_Toast(this.context, this.view, result);
        }
        //Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
    }

}