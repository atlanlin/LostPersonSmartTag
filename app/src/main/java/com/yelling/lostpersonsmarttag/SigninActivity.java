package com.yelling.lostpersonsmarttag;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class SigninActivity extends ActionBarActivity  implements MyActivityInteface{

    public static final String USERNAME_KEY = "username";
    public static Context contextOfApplication;

    private Intent myIntent;
    private ProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();

        if(SignInManager.isUserSignedIn(this)){
            myIntent= new Intent(this, MainActivity.class);
            String username = SignInManager.getUserId(this);
            myIntent.putExtra(USERNAME_KEY, username);
            gotoMainPage();
        }

        setContentView(R.layout.activity_signin);


        Button btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(myListener);
        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(myListener);


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

    private View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            switch (v.getId()){
                case R.id.btnSignin:
                    EditText etUsername=(EditText)findViewById(R.id.etUsername);
                    EditText etPassword=(EditText)findViewById(R.id.etPassword);
                    String username = etUsername.getText().toString();
                    SignInManager.signinRequest(SigninActivity.this, username
                            , etPassword.getText().toString());

                    View view = SigninActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    myProgressDialog = ProgressDialog.show(SigninActivity.this, "Logging in", "Please wait...");
                    //Toast.makeText(SigninActivity.this, Boolean.toString(request), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnSignup:
                    goToSignUpPage();
                    break;
            }
        }
    };

    @Override
    public void callbackFunction(JSONObject jsonObject){
        try {
            Log.d("ConnectWithServer", "Received: " + jsonObject.toString());
            JSONObject jsob = jsonObject.getJSONObject("d");
            int loginResult = jsob.getInt(MainActivity.ERROR_CODE_TAG);
            if(loginResult == 0){
                String guardian_id = String.valueOf(jsob.getInt("guardian_id"));
                myIntent= new Intent(SigninActivity.this, MainActivity.class);
                SignInManager.saveUserId(SigninActivity.this, guardian_id);
                myIntent.putExtra(USERNAME_KEY, guardian_id);
                gotoMainPage();
            }else {
                Log.d("ConnectWithServer", "Login Error");
                Toast.makeText(SigninActivity.this, "Login unsuccessful," +
                        jsob.getJSONObject("errorMsg").toString(), Toast.LENGTH_LONG).show();
                EditText etPassword = (EditText)findViewById(R.id.etPassword);
                etPassword.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myProgressDialog.dismiss();

    }

    @Override
    public void callbackFunction(String result){

    }

    private void gotoMainPage(){
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        startActivity(myIntent);
    }

    private void goToSignUpPage(){
        Intent myIntent= new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(myIntent);
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
