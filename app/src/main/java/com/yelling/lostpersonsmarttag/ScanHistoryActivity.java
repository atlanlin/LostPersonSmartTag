package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

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
        userId = SignInManager.getUserId(getActivity());
        //JSONObject eventList = fetchEventsArray(userId);





        return rootView;
    }


    private void createEventFragments(){

    }

    private EmergencyEvent[] createEventObjects(JSONObject jsonObject){
        ArrayList<EmergencyEvent> emergencyEventList = new ArrayList<EmergencyEvent>();
        int totalEvents;
        try {
            totalEvents = jsonObject.getInt("totalEvents");
            for(int i=0; i<totalEvents; i++){
                String eventId = jsonObject.getString("eventId" + i);
                String wardId = jsonObject.getString("wardId" + i);
                String wardName = jsonObject.getString("wardName" + i);
                emergencyEventList.add(new EmergencyEvent(jsonObject.getString("id" + i),
                        jsonObject.getString("ward_id" + i),
                        jsonObject.getString("ward_name" + i),
                        jsonObject.getInt("is_approved" + i),
                        jsonObject.getInt("is_ongoing" + i),
                        jsonObject.getString("guardian_description"),
                        jsonObject.getString("finder_description"),
                        jsonObject.getString("cookie")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchEventsArray(String userId){
        String url = "" + "?userId=" + userId;
        final MyActivityInteface callback = this;
        JsonController.jsonObjectGetRequest(url,
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
        createEventObjects(jsonObject);

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

        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(R.id.fragment_container, fragmentone);
        ft.add(R.id.fragment_container, fragmenttwo);
        ft.add(R.id.fragment_container, fragmentthree);
        ft.commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
