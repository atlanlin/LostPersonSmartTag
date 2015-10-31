package com.yelling.lostpersonsmarttag;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        if(emergencyEvent.is_ongoing==1){
            buttonsVisible();
        }else{
            if(emergencyEvent.is_approved==1){
                setApprovalText("Approved");
                buttonsInvisible();
            }else{
                setApprovalText("Rejected");
                buttonsInvisible();
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
                    sendApprovalToServer(ScanHistoryFragment.this, String.valueOf(emergencyEvent.id), "1");
                    break;
                case R.id.btnReject:
                    approvalStr = "Rejected";
                    sendApprovalToServer(ScanHistoryFragment.this, String.valueOf(emergencyEvent.id), "0");
                    break;
            }
            buttonsInvisible();
            setApprovalText(approvalStr);
        }
    };

    private void setApprovalText(String setString) {
        TextView tvApproval = (TextView) rootView.findViewById(R.id.tvApproval);
        tvApproval.setText(setString);
    }


    private void buttonsInvisible(){
        Button btnApprove = (Button)rootView.findViewById(R.id.btnApprove);
        Button btnReject = (Button)rootView.findViewById(R.id.btnReject);
        btnApprove.setVisibility(View.INVISIBLE);
        btnReject.setVisibility(View.INVISIBLE);
        TextView tvApproval = (TextView)rootView.findViewById(R.id.tvApproval);
        tvApproval.setVisibility(View.INVISIBLE);
    }

    private void buttonsVisible(){
        Button btnApprove = (Button)rootView.findViewById(R.id.btnApprove);
        Button btnReject = (Button)rootView.findViewById(R.id.btnReject);
        btnApprove.setVisibility(View.VISIBLE);
        btnReject.setVisibility(View.VISIBLE);
        TextView tvApproval = (TextView)rootView.findViewById(R.id.tvApproval);
        tvApproval.setVisibility(View.INVISIBLE);
    }

    private void sendApprovalToServer(final MyActivityInteface callback, String eventId, String is_approved) {
        String url = MainActivity.SERVER_URI + "/approveAlert";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("event_id", eventId);
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
