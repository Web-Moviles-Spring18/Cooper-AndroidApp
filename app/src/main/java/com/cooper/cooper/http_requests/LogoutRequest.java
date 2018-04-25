package com.cooper.cooper.http_requests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.cooper.cooper.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by marco on 08/04/2018.
 */

public class LogoutRequest extends AsyncTask<String, Void, String>{
    private HTTPRequestListener requestListener;
    private int statusCode;
    static final String COOKIES_HEADER = "Set-Cookie";
    static android.webkit.CookieManager CookieManager = android.webkit.CookieManager.getInstance();
    private Resources resources;;
    public LogoutRequest(HTTPRequestListener httpRequestListener, Resources resources) {
        this.requestListener = httpRequestListener;
        this.resources = resources;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            /*CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = this.resources.openRawResource(R.raw.cooper);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

// Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);*/


            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setSSLSocketFactory(context.getSocketFactory());

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
