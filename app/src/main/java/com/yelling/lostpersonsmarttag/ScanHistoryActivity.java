package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Yelling on 21/10/15.
 */
public class ScanHistoryActivity extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
        View rootView = inflater.inflate(R.layout.activity_scan_history, container, false);
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
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
