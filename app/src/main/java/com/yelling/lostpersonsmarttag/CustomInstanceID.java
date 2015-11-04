package com.yelling.lostpersonsmarttag;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Yelling on 1/8/15.
 */
public class CustomInstanceID extends InstanceIDListenerService {

    public CustomInstanceID(){}

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, SignupActivity.class);
        startService(intent);
    }
}
