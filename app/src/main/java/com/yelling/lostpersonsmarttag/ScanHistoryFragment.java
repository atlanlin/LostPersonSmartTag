package com.yelling.lostpersonsmarttag;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Yelling on 21/10/15.
 */
public class ScanHistoryFragment extends Fragment implements MyActivityInteface {

    public static String ARGS_NAME = "name";
    public static String ARGS_ADDRESS = "address";
    public static String ARGS_DESCRIPTION = "description";
    public static String ARGS_DATETIME = "datetime";
    public static String ARGS_EMERGENCY_EVENT = "emergency event";

    /*
    private String helperName, helperLocation, helperDescription;
    private String helperDatetime;
    */
    protected EmergencyEvent emergencyEvent;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
        this.helperName = getArguments().getString(ARGS_NAME);
        this.helperLocation = getArguments().getString(ARGS_ADDRESS);
        this.helperDescription = getArguments().getString(ARGS_DESCRIPTION);
        this.helperDatetime = getArguments().getString(ARGS_DATETIME);
        */
        this.emergencyEvent = getArguments().getParcelable(ARGS_EMERGENCY_EVENT);


        rootView = inflater.inflate(R.layout.view_history, container, false);
        EditText etName = (EditText) rootView.findViewById(R.id.etHName);
        etName.setText(emergencyEvent.finder_name);
        EditText etCurrentLoc = (EditText) rootView.findViewById(R.id.etHCurrentLoc);
        etCurrentLoc.setText(emergencyEvent.finder_location);
        EditText etDescription = (EditText) rootView.findViewById(R.id.etHDescription);
        etDescription.setText(emergencyEvent.finder_description);
        EditText etTime = (EditText) rootView.findViewById(R.id.etHTime);
        etTime.setText(emergencyEvent.scan_time);
        EditText etWName = (EditText)rootView.findViewById(R.id.etWardName);
        etWName.setText(emergencyEvent.ward_name);

        if(emergencyEvent.is_ongoing==1){
            buttonsVisible();
        }else{
            if(emergencyEvent.is_approved==1){
                setApprovalText("Approved");
                buttonsInvisible("Approved");
            }else{
                setApprovalText("Rejected");
                buttonsInvisible("Rejected");
            }
        }


        Button btnApprove = (Button) rootView.findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(myListener);
        Button btnReject = (Button) rootView.findViewById(R.id.btnReject);
        btnReject.setOnClickListener(myListener);
        return rootView;
        //return inflater.inflate(R.layout.view_history, container, false);
    }

    public ScanHistoryFragment() {

    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        String approvalStr= "";
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnApprove:
                    approvalStr = "Approved";
                    sendApprovalMsgToServer(ScanHistoryFragment.this, String.valueOf(emergencyEvent.id), "1");
                    break;
                case R.id.btnReject:
                    approvalStr = "Rejected";
                    sendApprovalMsgToServer(ScanHistoryFragment.this, String.valueOf(emergencyEvent.id), "0");
                    break;
            }
            buttonsInvisible(approvalStr);
            setApprovalText(approvalStr);
            //ViewGroup vg = (ViewGroup)rootView.findViewById (R.id.history_frag_layout);
            //vg.invalidate();
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    };

    private void setApprovalText(String setString) {
        TextView tvApproval = (TextView) rootView.findViewById(R.id.tvApproval);
        tvApproval.setText(setString);
    }


    private void buttonsInvisible(String approvalStr){
        LinearLayout llGInfo = (LinearLayout)rootView.findViewById(R.id.llguardianEventInfo);
        llGInfo.setVisibility(View.GONE);
        /*
        Button btnApprove = (Button)rootView.findViewById(R.id.btnApprove);
        Button btnReject = (Button)rootView.findViewById(R.id.btnReject);
        btnApprove.setVisibility(View.INVISIBLE);
        btnReject.setVisibility(View.INVISIBLE);
        */
        TextView tvApproval = (TextView)rootView.findViewById(R.id.tvApproval);
        tvApproval.setVisibility(View.VISIBLE);
        tvApproval.setGravity(Gravity.CENTER);
        tvApproval.setTypeface(Typeface.DEFAULT_BOLD);
        if(approvalStr.substring(0,1).equals("A"))
            tvApproval.setTextColor(Color.GREEN);
        else
            tvApproval.setTextColor(Color.RED);

    }

    private void buttonsVisible(){
        LinearLayout llGInfo = (LinearLayout)rootView.findViewById(R.id.llguardianEventInfo);
        llGInfo.setVisibility(View.VISIBLE);
        /*
        Button btnApprove = (Button)rootView.findViewById(R.id.btnApprove);
        Button btnReject = (Button)rootView.findViewById(R.id.btnReject);
        btnApprove.setVisibility(View.VISIBLE);
        btnReject.setVisibility(View.VISIBLE);
        */
        TextView tvApproval = (TextView)rootView.findViewById(R.id.tvApproval);
        tvApproval.setVisibility(View.INVISIBLE);
    }

    private void sendApprovalMsgToServer(final MyActivityInteface callback, String eventId, String is_approved){
        String url = MainActivity.SERVER_URI + "/updateEventVerbose";
        HashMap<String, String> params = new HashMap<String, String>();
        EditText etGDescription = (EditText)rootView.findViewById(R.id.etGDescription);
        EditText etGGLocation = (EditText)rootView.findViewById(R.id.etGAddress);

        params.put("id", eventId);
        params.put("guardian_description", etGDescription.getText().toString());
        params.put("guardian_location", etGGLocation.getText().toString());
        params.put("is_ongoing", "0");
        params.put("is_approved", is_approved);

        JsonController.jsonObjectPostRequest(url, params, new MyCallbackInterface() {
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

    private void sendApprovalToServer(final MyActivityInteface callback, String eventId, String is_approved) {
        String url = MainActivity.SERVER_URI + "/approveAlert";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", eventId);
        params.put("is_approved", is_approved);
        JsonController.jsonObjectPostRequest(url, params, new MyCallbackInterface() {
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

    public void callbackFunction(String result) {

    }

    public void callbackFunction(JSONObject jsonObject){
        Log.d("YeLinDebug", "Received Json Object");
    }

}
