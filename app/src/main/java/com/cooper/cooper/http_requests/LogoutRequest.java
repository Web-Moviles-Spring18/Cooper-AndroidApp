package com.cooper.cooper.http_requests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marco on 08/04/2018.
 */

public class LogoutRequest extends AsyncTask<String, Void, String>{
    private HTTPRequestListener requestListener;
    private int statusCode;
    static final String COOKIES_HEADER = "Set-Cookie";
    static android.webkit.CookieManager CookieManager = android.webkit.CookieManager.getInstance();

    public LogoutRequest(HTTPRequestListener httpRequestListener) {
        this.requestListener = httpRequestListener;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String cookie_str = CookieManager.getCookie(strings[0]);
            if (CookieManager.hasCookies() && cookie_str != null) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                //Log.d("Cookie", TextUtils.join(";",  CookieManager.getCookieStore().getCookies()));
                urlConnection.setRequestProperty("Cookie", cookie_str);
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            this.statusCode = urlConnection.getResponseCode();

            if(this.statusCode == HttpURLConnection.HTTP_OK) {
                CookieManager.setCookie(strings[0], null);
                return "Logged out!";
            } else {
                return "Logged out not complete!";
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.requestListener.requestDone(s, this.statusCode);
    }
}
