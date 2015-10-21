package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v4.app.Fragment;


public class ScannerActivity extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView tvFormat;
    private TextView tvContent;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScannerActivity newInstance(int sectionNumber) {
        ScannerActivity fragment = new ScannerActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScannerActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_scanner, container, false);

        tvFormat = (TextView)rootView.findViewById(R.id.tvFormat);
        tvContent = (TextView)rootView.findViewById(R.id.tvContent);
        Button btnScan = (Button)rootView.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ScannerActivity.this);
                scanIntegrator.initiateScan();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();
            //output to UI
            tvFormat.setText("FORMAT: "+scanFormat);
            tvContent.setText("CONTENT: "+scanContent);
        }
        else{
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
