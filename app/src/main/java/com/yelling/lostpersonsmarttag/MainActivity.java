package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MyActivityInteface{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static final String SERVER_URI = "http://10.27.186.191:8082/ASESvc.svc";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    protected static String guardian_id;
    protected static Guardian myStaticGuardian;
    private boolean guardianRetrieved;
    private boolean wardRetrieved;
    protected static Ward myStaticWard;
    //private FragmentManager fragmentManager = getFragmentManager();
    private android.app.Fragment replacedFragment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...");
        guardianRetrieved = false;
        wardRetrieved = false;
        Intent intent = getIntent();
        guardian_id = intent.getStringExtra(SigninActivity.USERNAME_KEY);
        fetchGuardianObject(MainActivity.this, guardian_id);
        fetchWardObject(MainActivity.this, guardian_id);

        setContentView(R.layout.activity_main);
        /*
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            replacedFragment = getFragmentManager().getFragment(
                    savedInstanceState, "mContent");

        }
        */
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    private void fetchGuardianObject(final MyActivityInteface callback, String guardian_id){
        String url = MainActivity.SERVER_URI + "/getGuardianDetail";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("guardian_id", guardian_id);
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

    private void fetchWardObject(final MyActivityInteface callback, String guardian_id){
        String url = MainActivity.SERVER_URI + "/getWards";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("guardian_id", guardian_id);
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

    @Override
    public void callbackFunction(String result){

    }

    @Override
    public void callbackFunction(JSONObject jsonObject){
        try {
            JSONObject jsob = jsonObject.getJSONObject("d");
            Gson gson = new Gson();
            if(!jsob.isNull("guardian")) {
                JsonElement guardianJson = (JsonElement) jsob.get("guardian");
                myStaticGuardian = gson.fromJson(guardianJson, Guardian.class);
                guardianRetrieved = true;
            }else if(!jsob.isNull("list")){
                JSONArray jsonArray = jsob.getJSONArray("list");
                if(jsonArray.length()>0){
                    JsonElement wardJson = (JsonElement)jsonArray.get(0);
                    myStaticWard = gson.fromJson(wardJson, Ward.class);
                }else{
                    Log.d("YeLinDebug", "There are no ward under this guardian");
                }
                wardRetrieved = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(guardianRetrieved && wardRetrieved){
            progressDialog.dismiss();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Boolean isLogout = false;
        switch(position){
            case 0:
                replacedFragment = ProfileActivity.newInstance(position + 1);
                break;
            case 1:
                replacedFragment = QrGeneratorActivity.newInstance(position + 1);
                break;
            case 2:
                replacedFragment = ScannerActivity.newInstance(position + 1);
                break;
            case 3:
                replacedFragment = ScanHistoryActivity.newInstance(position + 1);
                break;
            case 4:
                replacedFragment = PlaceholderFragment.newInstance(position + 1);
                SignInManager.clearUserId(MainActivity.this);
                guardian_id = "";
                isLogout = true;
                break;
            default:
                replacedFragment = PlaceholderFragment.newInstance(position + 1);
                break;
        }
        if(isLogout){
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, replacedFragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getFragmentManager().putFragment(outState, "mContent", replacedFragment);


    }
    */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

}
