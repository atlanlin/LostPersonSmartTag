package com.yelling.lostpersonsmarttag;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yelling on 24/10/15.
 */
public class SignupManager{

    public static String getGCMToken(String loginId){
        return "token";
    }

    public static void signupOnServer(final MyActivityInteface callback, JSONObject jsonObject){
        String url = MainActivity.SERVER_URI + "/";
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
