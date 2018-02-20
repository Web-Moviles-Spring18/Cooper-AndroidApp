package com.cooper.cooper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by marco on 18/02/2018.
 */

public class HttpHandler extends AsyncTask<String, String, String>{

    private URL url;
    private HashMap<String, Integer> responsesCode;

    public HttpHandler() {
        this.responsesCode = new HashMap<>();
    }

    @Override
    protected String doInBackground(String... strings) {
        String response;
        switch (strings[0]) {
            case "login":
                int response_code = this.logIn(strings[1], strings[2]);
                this.responsesCode.put("login", response_code);
                response =  response_code + "";
                break;
            case "pool":
                response = this.createPool(strings[1], strings[2], false, "10/05/2018", 342.21) + "";
                break;
            default:
                response = "Not Endpoint founded";
                break;
        }
        //Log.d("response login()", response);
        return response;
    }

    public int getResponseCode(String api_path) {
        return this.responsesCode.get(api_path);
    }
    private int logIn(String user, String password) {
        try {
            Log.d("USER", user);
            Log.d("PASS", password);

            JSONObject json_response = new JSONObject();
            json_response.put("email", user);
            json_response.put("password", password);
            String request = json_response.toString();
            Log.d("JSON_REQUEST", json_response.toString());

            this.url = new URL(Utils.URL + "/login");
            Log.d("url", this.url.toString());
            HttpURLConnection post = (HttpURLConnection) this.url.openConnection();
            post.setRequestMethod("POST");
            post.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            post.setRequestProperty("Accept", "application/json");
            post.setDoInput(true);
            post.setDoOutput(true);

            Log.d("beforeWrite", this.url.toString());
            OutputStream  wr = new BufferedOutputStream(post.getOutputStream());
            wr.write(request.getBytes());
            Log.d("afterWrite", this.url.toString());
            wr.flush();
            wr.close();

            int responseCode = post.getResponseCode();
            Log.d("Sending POST", this.url.toString());
            Log.d("Response Code ", responseCode + "");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(post.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Log.d("Response ", response.toString());
            return responseCode;
        } catch (Exception e) {
            Log.d("EXC_LOGIN","Exception: " + e.toString());
            return 400;
        }

    }

    private String createPool(String name, String paymentMethod, boolean isPrivate, String endDate, double totalAmount) {
        try {

            JSONObject json_response = new JSONObject();
            json_response.put("name", name);
            json_response.put("endDate", endDate);
            json_response.put("paymentMethod", paymentMethod);
            json_response.put("private", isPrivate);
            json_response.put("totalAmount", totalAmount);
            String request = json_response.toString();
            Log.d("JSON_REQUEST", json_response.toString());

            this.url = new URL(Utils.URL + "/pool");
            Log.d("url", this.url.toString());
            HttpURLConnection post = (HttpURLConnection) this.url.openConnection();
            post.setRequestMethod("POST");
            post.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            post.setRequestProperty("Accept", "application/json");
            post.setDoInput(true);
            post.setDoOutput(true);

            OutputStream  wr = new BufferedOutputStream(post.getOutputStream());
            wr.write(request.getBytes());
            wr.flush();
            wr.close();

            int responseCode = post.getResponseCode();
            Log.d("Sending POST", this.url.toString());
            Log.d("Response Code ", responseCode + "");
            // Read the API response
            BufferedReader in = new BufferedReader(new InputStreamReader(post.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return responseCode + "";
        } catch (Exception e) {
            return "Exception: " + e.toString();
        }
    }

    /*private boolean validNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }*/
}
