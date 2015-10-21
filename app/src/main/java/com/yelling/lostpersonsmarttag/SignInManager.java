package com.yelling.lostpersonsmarttag;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Yelling on 24/9/15.
 */
public class SignInManager {

    protected static final String PREFS_NAME = "SigninPrefs";

    protected static boolean signinRequest(String username, String password){
        if(username.equals(""))
            return false;
        return true;
    }

    protected static void saveUserId(Context context, String userId){
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //prefs.edit().putString("userid", userId).commit();
        Context applicationContext = SigninActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        prefs.edit().putString(PREFS_NAME, userId).commit();
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
        String userId = prefs.getString(PREFS_NAME, "");
        return userId;
    }

    protected static void clearUserId(Context context){
        saveUserId(context, "");
    }
}
