package com.yelling.lostpersonsmarttag;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yelling on 24/9/15.
 */
public class SignInManager{

    protected static final String G_PREFS_NAME = "G_SigninPrefs";
    protected static final String G_PREFS_URI = "G_ProfileUri";
    protected static final String G_PREF_ADDRESS = "G_Address";
    protected static final String G_PREFS_DESC = "G_Description";

    protected static final String P_PREFS_NAME = "P_SigninPrefs";
    protected static final String P_PREFS_URI = "P_ProfileUri";
    protected static final String P_PREF_ADDRESS = "P_Address";
    protected static final String P_PREFS_DESC = "P_Description";

    protected static final String P_PREFS_PHOTO_WIDTH = "P Width";
    protected static final String P_PREFS_PHOTO_HEIGHT = "P Height";

    //protected static final String[] pContainedInfo = {"P Name", "P Profile Uri", "P Address", "P Description"};
    //protected static final String[] gGuardianInfo = {"G Name", "G Address", "G Description"};

    protected static String patientDescription;
    protected static String patientPhotoUri;
    protected static String patientPhotoWScale;
    protected static String patientPhotoHScale;


    protected static void signinRequest(final MyActivityInteface callback, String username, String password){
        String url= "http://10.27.186.191:62969/ASESvc.svc/test" + "?username=" + username + "&password=" + password;
        JsonController.jsonObjectGetRequest(url, new MyCallbackInterface() {
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

    protected static void saveUserId(Context context, String userId){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //prefs.edit().putString("userid", userId).commit();
        Context applicationContext = SigninActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        prefs.edit().putString(G_PREFS_NAME, userId).commit();
        /*
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userid", userId);
        */
    }



    protected static boolean isUserSignedIn(Context context){
        String userId = getUserId(context);
        Log.d("Ye Lin", "User Id: " + userId);
        if(userId.equals(""))
            return false;
        return true;
    }

    protected static String getUserId(Context context){
        //SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        /*
        SharedPreferences mPrefs = context.getSharedPreferences(PREFS_NAME, 0);
        String userId= mPrefs.getString("userid", "");
        return userId;
        */
        Context applicationContext = SigninActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String userId = prefs.getString(G_PREFS_NAME, "");
        return userId;
    }

    protected static void getSharedPreferences(){
        Context applicationContext = SigninActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        patientDescription = prefs.getString(P_PREFS_DESC, "Person is currently suffering dementia.");
        patientPhotoUri = prefs.getString(P_PREFS_URI, "");
        patientPhotoWScale = prefs.getString(P_PREFS_PHOTO_WIDTH, "1024");
        patientPhotoHScale = prefs.getString(P_PREFS_PHOTO_HEIGHT, "1024");
    }

    protected static void setSharedPreferences(){
        Context applicationContext = SigninActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        prefs.edit().putString(P_PREFS_DESC, patientDescription).commit();
        prefs.edit().putString(P_PREFS_URI, patientPhotoUri).commit();
        prefs.edit().putString(P_PREFS_PHOTO_WIDTH, patientPhotoWScale).commit();
        prefs.edit().putString(P_PREFS_PHOTO_HEIGHT, patientPhotoHScale).commit();
    }

    protected static void updateInstances(String[] pInfo, String[] gInfo){
        patientDescription = pInfo[0];
        patientPhotoUri = pInfo[1];
    }

    protected static void clearUserId(Context context){
        saveUserId(context, "");
    }
}
