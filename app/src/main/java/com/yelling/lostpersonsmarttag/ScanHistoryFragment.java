package com.yelling.lostpersonsmarttag;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Yelling on 21/10/15.
 */
public class ScanHistoryFragment extends Fragment {

    public static String ARGS_NAME = "name";
    public static String ARGS_ADDRESS = "address";
    public static String ARGS_DESCRIPTION = "description";
    public static String ARGS_DATETIME = "datetime";

    private String helperName, helperLocation, helperDescription;
    private String helperDatetime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.helperName = getArguments().getString(ARGS_NAME);
        this.helperLocation = getArguments().getString(ARGS_ADDRESS);
        this.helperDescription = getArguments().getString(ARGS_DESCRIPTION);
        this.helperDatetime = getArguments().getString(ARGS_DATETIME);


        View rootView = inflater.inflate(R.layout.view_history, container, false);
        EditText etName = (EditText)rootView.findViewById(R.id.etHName);
        etName.setText(helperName);
        EditText etCurrentLoc = (EditText)rootView.findViewById(R.id.etHCurrentLoc);
        etCurrentLoc.setText(helperLocation);
        EditText etDescription = (EditText)rootView.findViewById(R.id.etHDescription);
        etDescription.setText(helperDescription);
        EditText etTime = (EditText)rootView.findViewById(R.id.etHTime);
        etTime.setText(helperDatetime);

        return rootView;
        //return inflater.inflate(R.layout.view_history, container, false);
    }

    public ScanHistoryFragment(){

    }
}
