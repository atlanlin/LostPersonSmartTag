package com.yelling.lostpersonsmarttag;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Yelling on 24/10/15.
 */
public class SignupManager{

    public static final String SENDER_ID = "321919514148";
    public static final String GCM_LOG_TAG = "GCMLog";

    public static String getGCMToken(Context context){
        try {
            InstanceID instanceID = InstanceID.getInstance(context);
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            String token = instanceID.getToken(SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(GCM_LOG_TAG,"GCM Token : " + token);
            return token;
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(GCM_LOG_TAG, "GCM Registration failed");
        }
        return null;
    }

    public static void getGcmTokenInBackground(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    InstanceID instanceID = InstanceID.getInstance(context);
                    String token = instanceID.getToken(SENDER_ID,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    /*
                    String token =
                            InstanceID.getInstance(MainActivity.this).getToken(senderId,
                                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    mLogger.log(Log.INFO, "registration succeeded." +
                            "\nsenderId: " + senderId + "\ntoken: " + token);
                    // Save the token in the address book
                    Sender entry = mSenders.getSender(senderId);
                    if (entry == null) {
                        mLogger.log(Log.ERROR, "Could not save token, missing sender id");
                        return null;
                    }
                    entry.testAppToken = token;
                    mSenders.updateSender(entry);
                    */
                    Log.d(GCM_LOG_TAG, "registration succeeded." +
                            "\nsenderId: " + SENDER_ID + "\ntoken: " + token);
                    //textbox.setText(token);

                } catch (final IOException e) {
                    Log.d(GCM_LOG_TAG, "registration failed." +
                            "\nsenderId: " + SENDER_ID + "\nerror: " + e.getMessage());
                }
                return null;
            }

        }.execute();
    }

    public static void signupGuardianOnServer(final MyActivityInteface callback, HashMap<String, String> params){
        String url = MainActivity.SERVER_URI + "/createGuardian";
        JsonController.jsonObjectPostRequest(url, params,
                new MyCallbackInterface() {
                    @Override
                    public void onFetchFinish(JSONObject response) {
                        callback.callbackFunction(response);
                    }

                    @Override
                    public void onFetchFinish(JSONArray response) {

                    }

                    @Override
                    public void onFetchFinish(String result) {

                    }
                });


    }

    public static void signupWardOnServer(final MyActivityInteface callback, JSONObject jsonObject){
        String url = MainActivity.SERVER_URI + "/createWard";
        JsonController.jsonObjectPostRequest(url, jsonObject,
                new MyCallbackInterface() {
                    @Override
                    public void onFetchFinish(JSONObject response) {
                        callback.callbackFunction(response);
                    }

                    @Override
                    public void onFetchFinish(JSONArray response) {

                    }

                    @Override
                    public void onFetchFinish(String result) {

                    }
                });
    }




}
