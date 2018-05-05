package com.cooper.cooper;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cooper.cooper.Menu.Coop_Detail_Act;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TYPE_INVITE = "invite";
    private static final String TYPE_PAYMENT_REQUEST = "paymentRequest";
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            handleNow(remoteMessage);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(RemoteMessage payload) {
        Log.d(TAG, "Short lived task is done.");
        try {
            String title = payload.getNotification().getTitle();
            String subject = payload.getNotification().getBody();
            Map<String, String> data = payload.getData();
            Long poolId = Long.parseLong(data.get("poolId"));
            String type = data.get("type");

            sendNotification(title, subject, type, poolId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM title received.
     * @param subject FCM message body received.
     * @param type Cooper notification type received.
     * @param poolId Cooper poolId received.
     */
    private void sendNotification(String title, String subject, String type, Long poolId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.cooper_icon)
                        .setContentTitle(title)
                        .setContentText(subject)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

        switch (type) {
            case TYPE_INVITE:
                // build with buttons accept and decline, and open pool view.
                // TODO: decline and accept buttons.

                Intent acceptIntent = new Intent(this, IntentServicePool.class);
                acceptIntent.putExtra(IntentServicePool.EXTRA_POOL_ID, poolId);
                acceptIntent.setAction(IntentServicePool.ACTION_ACCEPT);
                PendingIntent acceptPIntent = PendingIntent.getService(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action accept = new NotificationCompat.Action.Builder(
                        R.mipmap.ic_cooper_launcher,
                        "Accept", acceptPIntent).build();
                notificationBuilder.addAction(accept);

                Intent declineIntent = new Intent(this, IntentServicePool.class);
                acceptIntent.putExtra(IntentServicePool.EXTRA_POOL_ID, poolId);
                declineIntent.setAction(IntentServicePool.ACTION_DECLINE);

                PendingIntent declinePIntent = PendingIntent.getService(this, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action decline = new NotificationCompat.Action.Builder(
                        R.mipmap.ic_cooper_launcher,
                        "Decline", declinePIntent).build();
                notificationBuilder.addAction(decline);
            default:
                // open pool view.
                Intent poolIntent = new Intent(this, Coop_Detail_Act.class);
                poolIntent.putExtra("pool", poolId);
                PendingIntent pIntent = PendingIntent.getActivity(this, 0, poolIntent, PendingIntent.FLAG_ONE_SHOT);
                notificationBuilder.setContentIntent(pIntent);
                break;
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}