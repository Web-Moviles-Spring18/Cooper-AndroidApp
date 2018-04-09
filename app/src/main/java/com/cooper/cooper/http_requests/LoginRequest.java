package com.cooper.cooper.http_requests;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by marco on 29/03/2018.
 */

public class LoginRequest extends AsyncTask<String, Void, Object> {

    private HTTPRequestListener httpListener;
    private JSONObject loginData;

    private int statusCode;
    private static final String COOKIES_HEADER = "Set-Cookie";
    private static CookieManager CookieManager = android.webkit.CookieManager.getInstance();

    public LoginRequest(HTTPRequestListener listener, JSONObject loginData) {
        this.httpListener = listener;
        this.loginData = loginData;
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(strings[0]);
            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String cookie_str = CookieManager.getCookie(strings[0]);
            if (CookieManager.hasCookies() && cookie_str != null) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                urlConnection.setRequestProperty("Cookie", cookie_str);
            }
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            // OPTIONAL - Sets an authorization header
            //urlConnection.setRequestProperty("Authorization", "someAuthString");
            if (this.loginData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(loginData.toString());
                //writer.write(request.getBytes());
                writer.flush();
                writer.close();
            }
            this.statusCode = urlConnection.getResponseCode();
            StringBuilder response_body = new StringBuilder();
            if (this.statusCode ==  HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response_body.append(inputLine);
                }
                Log.d("Response", response_body.toString());
                in.close();
                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        CookieManager.setCookie(strings[0], cookie);
                    }
                }
            } else if(this.statusCode == HttpURLConnection.HTTP_BAD_REQUEST){
                response_body.append("Invalid Email or Password!");
            } else {
                response_body.append("There was an Error, try later!");
            }
            return response_body.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        this.httpListener.requestDone(obj, this.statusCode);
    }
}
