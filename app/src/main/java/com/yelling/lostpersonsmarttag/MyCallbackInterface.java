package com.yelling.lostpersonsmarttag;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yelling on 25/10/15.
 */
public interface MyCallbackInterface {

    void onFetchFinish(JSONObject response);
    void onFetchFinish(JSONArray response);
    void onFetchFinish(String result);
}
