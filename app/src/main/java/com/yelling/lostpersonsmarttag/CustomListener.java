package com.yelling.lostpersonsmarttag;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Yelling on 1/8/15.
 */
public class CustomListener extends GcmListenerService {


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(SignupManager.GCM_LOG_TAG, "From: " + from);
        Log.d(SignupManager.GCM_LOG_TAG, "Message: " + message);
        generateNotification(getApplicationContext(), message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);
    }

    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.merlion;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("ms", message);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //notification.setLatestEventInfo(context, title, message, intent);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Recevied scanned from " + title)
                .setContentText(message)
                .setSmallIcon(icon)
                .setWhen(when)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }


}
