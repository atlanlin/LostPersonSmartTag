package com.yelling.lostpersonsmarttag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SigninActivity extends ActionBarActivity {

    public static final String USERNAME_KEY = "username";
    public static Context contextOfApplication;

    private Intent myIntent;

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
                    Boolean request = SignInManager.signinRequest(username
                            , etPassword.getText().toString());
                    if(request){
                        myIntent= new Intent(SigninActivity.this, MainActivity.class);
                        SignInManager.saveUserId(SigninActivity.this, username);
                        myIntent.putExtra(USERNAME_KEY, username);
                        gotoMainPage();
                    }
                    //Toast.makeText(SigninActivity.this, Boolean.toString(request), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void gotoMainPage(){
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        startActivity(myIntent);
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
