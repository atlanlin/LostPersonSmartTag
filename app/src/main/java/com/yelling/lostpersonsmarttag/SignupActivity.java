package com.yelling.lostpersonsmarttag;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yelling on 24/10/15.
 */
public class SignupActivity extends ActionBarActivity implements MyActivityInteface {


    private ArrayList<EditText> guardianEditTextList;
    private ArrayList<EditText> wardEditTextList;
    private String userLoginId;
    public static final String SIGNUP_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        LinearLayout guardianLayout = (LinearLayout)findViewById(R.id.guardianLayout);
        LinearLayout wardLayout = (LinearLayout)findViewById(R.id.wardLayout);

        guardianEditTextList = new ArrayList<>();
        wardEditTextList = new ArrayList<>();
        for( int i = 0; i < guardianLayout.getChildCount(); i++ )
            if( guardianLayout.getChildAt( i ) instanceof EditText )
                guardianEditTextList.add( (EditText) guardianLayout.getChildAt( i ) );

        for( int i = 0; i < wardLayout.getChildCount(); i++ )
            if( wardLayout.getChildAt( i ) instanceof EditText )
                wardEditTextList.add( (EditText) wardLayout.getChildAt( i ) );

        Button btnClear = (Button)findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllEditText();
            }
        });

        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isAnyFieldEmpty()){
                    Toast.makeText(SignupActivity.this, "Some fields are not filled up"
                            , Toast.LENGTH_LONG).show();
                }else{
                    HashMap<String,String> params = createHashmapFromEditTexts();
                    String gcmToken = SignupManager.getGCMToken(userLoginId);
                    params.put("GCMToken", gcmToken);
                    signupOnServer(SignupActivity.this, params);
                }
            }
        });
    }

    public static void signupOnServer(final MyActivityInteface callback, HashMap<String,String> params){
        JsonController.jsonObjectPostRequest(SIGNUP_URL, params,
                new MyCallbackInterface() {
                    @Override
                    public void onFetchFinish(JSONObject response) {
                        String result = null;
                        try {
                            result = response.getString("SignupResult").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.callbackFunction(result);
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
        if(result == null){
            Toast.makeText(SignupActivity.this, "Signup unsuccessful", Toast.LENGTH_LONG).show();
            return;
        }

        if(result.equals("success")){
            Toast.makeText(SignupActivity.this, "Signup success", Toast.LENGTH_LONG).show();
        }else if(result.equals("userexists")){
            Toast.makeText(SignupActivity.this, "Username already exists", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(SignupActivity.this, "Signup unsuccessful", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void callbackFunction(JSONObject jsonObject){

    }

    private void clearAllEditText(){
        for(int i=0; i < guardianEditTextList.size(); i++){
            guardianEditTextList.get(i).setText(null);
        }
        for(int i=0; i <wardEditTextList.size(); i++){
            wardEditTextList.get(i).setText(null);
        }
    }

    private boolean isAnyFieldEmpty(){
        for(int i=0; i<guardianEditTextList.size(); i++){
            if(guardianEditTextList.get(i).getText().toString().equals(""))
                return true;
        }
        for(int i=0; i<guardianEditTextList.size(); i++){
            if(wardEditTextList.get(i).getText().toString().equals(""))
                return true;
        }
        return false;
    }

    private HashMap<String, String> createHashmapFromEditTexts(){
        HashMap<String, String> params = new HashMap<String, String>();
        for(int i=0; i<guardianEditTextList.size(); i++){
            // get the ID Name of xml elements
            String idName = getResources().getResourceEntryName(guardianEditTextList.get(i).getId());
            String value = guardianEditTextList.get(i).getText().toString();
            if(idName.equals("etLoginName"))
                userLoginId = value;
            params.put(idName, value);
        }
        return params;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
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



}




