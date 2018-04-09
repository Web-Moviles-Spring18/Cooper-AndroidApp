package com.cooper.cooper.http_requests;

/**
 * Created by marco on 20/02/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.view.View;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by marco on 18/02/2018.
 */

public class PostRequests extends AsyncTask<String, String, JSONObject> {

    private JSONObject postData;
    private Context context;
    private View view;
    static final String COOKIES_HEADER = "Set-Cookie";
    static android.webkit.CookieManager CookieManager = android.webkit.CookieManager.getInstance();

    public PostRequests(JSONObject postData, Context context) {
        if (postData != null) {
            this.postData = postData;
        }
        this.context = context;
    }

    public PostRequests(JSONObject postData) {
        if (postData != null) {
            this.postData = postData;
        }
    }

    public PostRequests(JSONObject postData, Context context, View view) {
        if (postData != null) {
            this.postData = postData;
        }
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            boolean isLogin = strings[0].contains("login");
            boolean isSignUp = strings[0].contains("signup");
            // This is getting the url from the string we passed in
            URL url = new URL(strings[0]);
            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


           /* String myCookie = "cooper.sid";
            urlConnection.setRequestProperty("Cookie", myCookie);*/
             String cookie_str = CookieManager.getCookie(strings[0]);
            if (CookieManager.hasCookies() && cookie_str != null) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                urlConnection.setRequestProperty("Cookie", cookie_str);
            }


            /**/
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

            int statusCode = urlConnection.getResponseCode();
            StringBuilder response_body = new StringBuilder();
            if (statusCode ==  200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response_body.append(inputLine);
                }
                Log.d("Response", response_body.toString());
                in.close();
            } else {
                response_body.append("Error, try later!");
                // Status code is not 200
                // Do something to handle the error
            }
            if(isLogin || isSignUp) {

                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        CookieManager.setCookie(strings[0], cookie);
                        //CookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }
            }
            JSONObject response = new JSONObject();
            response.put("status_code", statusCode);
            response.put("response", response_body.toString());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void onPostExecute(String result) {
        /*if(result.equals("200")) {
            Intent intent = new Intent(this.context,MainMenu.class);
            this.context.startActivity(intent);
        } else {
            new AlertToast().Show_Toast(this.context, this.view, result);
        }*/
        //Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
    }

}
