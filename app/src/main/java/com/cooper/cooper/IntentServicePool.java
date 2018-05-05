package com.cooper.cooper;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.cooper.cooper.http_requests.GetRequests;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServicePool extends IntentService {
    public static final String ACTION_ACCEPT = "com.cooper.cooper.action.accept";
    public static final String ACTION_DECLINE = "com.cooper.cooper.action.decline";
    public static final String EXTRA_POOL_ID = "com.cooper.cooper.extra.poolId";

    public IntentServicePool() {
        super("IntentServicePool");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String param1 = intent.getStringExtra(EXTRA_POOL_ID);
            if (ACTION_ACCEPT.equals(action)) {
                handleActionAccept(param1);
            } else if (ACTION_DECLINE.equals(action)) {
                handleActionDecline(param1);
            }
        }
    }

    /**
     * Handle action Accept in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAccept(String poolId) {
        GetRequests request = new GetRequests();
        request.execute(Utils.URL + "/pool/" + poolId + "/accept");
    }

    /**
     * Handle action Decline in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDecline(String poolId) {
        GetRequests request = new GetRequests();
        request.execute(Utils.URL + "/pool/" + poolId + "/decline");
    }
}
