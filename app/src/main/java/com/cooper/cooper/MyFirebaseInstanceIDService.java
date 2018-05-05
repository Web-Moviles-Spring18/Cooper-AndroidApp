package com.cooper.cooper;

import android.util.Log;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.http_requests.PostRequests;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("fcmToken", FirebaseInstanceId.getInstance().getToken());
            PostRequests updateProfile = new PostRequests(postData);
            updateProfile.execute(Utils.URL.concat("/account/profile"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
