package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yelling on 21/10/15.
 */
public class ScanHistoryActivity extends Fragment implements MyActivityInteface{

    private static final String ARG_SECTION_NUMBER = "section_number";
    protected View rootView;
    private String userId;

    private TextView tvFormat;
    private TextView tvContent;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScanHistoryActivity newInstance(int sectionNumber) {
        ScanHistoryActivity fragment = new ScanHistoryActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScanHistoryActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_scan_history, container, false);
        //userId = SignInManager.getUserId(getActivity());
        fetchEventsArray(MainActivity.guardian_id);

        return rootView;
    }



    private ArrayList<EmergencyEvent> createEventObjects(JSONObject jsonObject){
        ArrayList<EmergencyEvent> emergencyEventList = new ArrayList<EmergencyEvent>();
        Gson gson = new Gson();
        try {
            JSONObject dObject= jsonObject.getJSONObject("d");
            JSONArray jsonArray = dObject.getJSONArray("list");
            for(int i=0; i<jsonArray.length(); i++){
                JsonElement eachJsonObject = (JsonElement)jsonArray.get(i);
                EmergencyEvent eachEvent = gson.fromJson(eachJsonObject, EmergencyEvent.class);
                emergencyEventList.add(eachEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emergencyEventList;
    }

    private ArrayList<ScanHistoryFragment> createFragmentsFromEventClass(
                ArrayList<EmergencyEvent> emergencyEventArrayList){
        ArrayList<ScanHistoryFragment> scanHistoryFragmentArrayList = new ArrayList<ScanHistoryFragment>();
        for(int i=0; i<emergencyEventArrayList.size(); i++){
            ScanHistoryFragment scanHistoryFragment = new ScanHistoryFragment();
            Bundle args = new Bundle();
            args.putParcelable(ScanHistoryFragment.ARGS_EMERGENCY_EVENT, emergencyEventArrayList.get(i));
            scanHistoryFragment.setArguments(args);
            scanHistoryFragmentArrayList.add(scanHistoryFragment);
        }
        return scanHistoryFragmentArrayList;

    }

    private void fetchEventsArray(String userId){
        String url = MainActivity.SERVER_URI + "/getEvents";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("guardian_id", MainActivity.guardian_id);
        final MyActivityInteface callback = this;
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

    @Override
    public void callbackFunction(String result){

    }

    @Override
    public void callbackFunction(JSONObject jsonObject){
        ArrayList<EmergencyEvent> emergencyEventArrayList = createEventObjects(jsonObject);
        ArrayList<ScanHistoryFragment> historyFragmentArrayList =
                createFragmentsFromEventClass(emergencyEventArrayList);

        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        for(int i=0; i<historyFragmentArrayList.size(); i++){
            ft.add(R.id.fragment_container, historyFragmentArrayList.get(i));
        }
        ft.commit();

        /*
        ScanHistoryFragment fragmentone = new ScanHistoryFragment();
        Bundle args2 = new Bundle();
        args2.putString(ScanHistoryFragment.ARGS_NAME, "Boon Eng");
        args2.putString(ScanHistoryFragment.ARGS_ADDRESS, "NTU");
        args2.putString(ScanHistoryFragment.ARGS_DESCRIPTION, "Hello there");
        args2.putString(ScanHistoryFragment.ARGS_DATETIME, new Date(2015, 8, 12, 22, 23).toString());
        fragmentone.setArguments(args2);

        ScanHistoryFragment fragmenttwo = new ScanHistoryFragment();
        args2 = new Bundle();
        args2.putString(ScanHistoryFragment.ARGS_NAME, "Gibson");
        args2.putString(ScanHistoryFragment.ARGS_ADDRESS, "NTU");
        args2.putString(ScanHistoryFragment.ARGS_DESCRIPTION, "Hi!");
        args2.putString(ScanHistoryFragment.ARGS_DATETIME, new Date(2015, 9, 20, 11, 00).toString());
        fragmenttwo.setArguments(args2);


        ScanHistoryFragment fragmentthree = new ScanHistoryFragment();
        args2 = new Bundle();
        args2.putString(ScanHistoryFragment.ARGS_NAME, "Gibson");
        args2.putString(ScanHistoryFragment.ARGS_ADDRESS, "NTU");
        args2.putString(ScanHistoryFragment.ARGS_DESCRIPTION, "Hi!");
        args2.putString(ScanHistoryFragment.ARGS_DATETIME, new Date(2015, 9, 20, 11, 00).toString());
        fragmentthree.setArguments(args2);

        */
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
