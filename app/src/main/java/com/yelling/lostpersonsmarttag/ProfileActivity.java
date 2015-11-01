package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ProfileActivity extends Fragment implements MyActivityInteface{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private View rootView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String STATE_PROFILE_URI = "profile uri";
    private ViewGroup classContainer;
    private ImageView ivProfile, guardianivProfile;
    private String wardProfileUri, guardianProfileUri;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileActivity newInstance(int sectionNumber) {
        ProfileActivity fragment = new ProfileActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_profile, container, false);
        classContainer = container;
        ivProfile = (ImageView)rootView.findViewById(R.id.profileImage);
        guardianivProfile = (ImageView)rootView.findViewById(R.id.guardianProfileImage);
        loadPreferences(rootView);
        Button btnApply = (Button)rootView.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(myListener);
        Button btnCancel = (Button)rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(myListener);
        /*
        if(wardProfileUri!=null) {
            Log.d("YeLinDebug", "URI: " + wardProfileUri.toString());
            setImage(wardProfileUri);
        }
        */
        ivProfile.setOnClickListener(myListener);
        guardianivProfile.setOnClickListener(myListener);
        retrieveInfo(rootView);
        return rootView;
    }
    /*
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            wardProfileUri = Uri.parse(savedInstanceState.getString(STATE_PROFILE_URI));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putString(STATE_PROFILE_URI, wardProfileUri.toString());

    }
    */



    private View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            switch (v.getId()){
                case R.id.btnApply:

                    Guardian newGuardian = MainActivity.myStaticGuardian.copyOf();
                    newGuardian = updateGuardian(newGuardian);
                    Ward newWard = MainActivity.myStaticWard.copyOf();
                    newWard = updateWard(newWard);
                    Boolean request = validateFillinInformations(newGuardian, newWard);
                    if(request) {
                        MainActivity.myStaticGuardian = newGuardian;
                        MainActivity.myStaticWard = newWard;
                        savePreferences(rootView);
                        updateDataToServer(ProfileActivity.this);
                    }
                    break;
                case R.id.btnCancel:
                    restoreGuardianInfo();
                    restoreWardInfo();
                    break;
                case R.id.profileImage:
                    showDialogBox(getActivity(), 0);
                    break;
                case R.id.guardianProfileImage:
                    showDialogBox(getActivity(), 1);
                    break;
            }
        }
    };

    private Guardian updateGuardian(Guardian newGuardian){
        EditText etGName = (EditText)rootView.findViewById(R.id.etGName);
        newGuardian.name = etGName.getText().toString();
        EditText etGAddress =(EditText)rootView.findViewById(R.id.etGAddress);
        newGuardian.address = etGAddress.getText().toString();
        EditText etGContact = (EditText)rootView.findViewById(R.id.etGContact);
        newGuardian.contact_number = etGContact.getText().toString();
        EditText etGDescription = (EditText)rootView.findViewById(R.id.etGDescription);
        newGuardian.description = etGDescription.getText().toString();
        return newGuardian;
    }

    private Ward updateWard(Ward newWard){
        EditText etWName = (EditText)rootView.findViewById(R.id.etPName);
        newWard.name = etWName.getText().toString();
        EditText etWDescription = (EditText)rootView.findViewById(R.id.etPDescription);
        newWard.description = etWDescription.getText().toString();
        return newWard;
    }

    private void restoreGuardianInfo(){
        EditText etGName = (EditText)rootView.findViewById(R.id.etGName);
        etGName.setText(MainActivity.myStaticGuardian.name);
        EditText etGAddress = (EditText)rootView.findViewById(R.id.etGAddress);
        etGAddress.setText(MainActivity.myStaticGuardian.address);
        EditText etGContact = (EditText)rootView.findViewById(R.id.etGContact);
        etGContact.setText(MainActivity.myStaticGuardian.contact_number);
        EditText etGDescription = (EditText)rootView.findViewById(R.id.etGDescription);
        etGDescription.setText(MainActivity.myStaticGuardian.description);
    }

    private void restoreWardInfo(){
        EditText etWName = (EditText)rootView.findViewById(R.id.etPName);
        etWName.setText(MainActivity.myStaticWard.name);
        EditText etWDescription = (EditText)rootView.findViewById(R.id.etPDescription);
        etWDescription.setText(MainActivity.myStaticWard.description);
    }

    private boolean validateFillinInformations(Guardian guardian, Ward ward){
        return guardian.validateInfo() && ward.validateInfo();
    }

    private void updateDataToServer(final MyActivityInteface callback){
        try {
            String guardianUpdateUrl = MainActivity.SERVER_URI + "/updateGuardianDetail";
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guardian", gson.toJson(MainActivity.myStaticGuardian));
            JsonController.jsonObjectPostRequest(guardianUpdateUrl, jsonObject, new MyCallbackInterface() {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void savePreferences(View rootView){
        EditText etPDesc = (EditText)rootView.findViewById(R.id.etPDescription);
        String[] pInfo = {wardProfileUri};
        String[] gInfo = {guardianProfileUri};
        SignInManager.updateInstances(pInfo, gInfo);
        SignInManager.setSharedPreferences();
    }

    protected boolean loadPreferences(View rootView){
        SignInManager.getSharedPreferences();
        return true;
    }

    @Override
    public void callbackFunction(JSONObject jsonObject){
        if(true) {
            Toast.makeText(getActivity(), "Information has updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Unable to update, please validate information", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callbackFunction(String response){

    }

    private void retrieveInfo(View rootView){

        String[] gInfo = {MainActivity.guardian_id, "8453544", "This is his niece."};
        String[] pInfo = {"Ah Lin", "Block 412, Jurong West Street 23, Singapore 640412",
                "The person is currently suffering demnetia"};

        int[] gEditText = {R.id.etGName, R.id.etGContact, R.id.etGDescription};
        int[] pEditText = {R.id.etPName, R.id.etPAddress, R.id.etPDescription};

        for(int i=0; i<gEditText.length; i++){
            EditText tempET = (EditText)rootView.findViewById(gEditText[i]);
            tempET.setText(gInfo[i]);
        }

        for(int i=0; i<gEditText.length; i++){
            EditText tempET = (EditText)rootView.findViewById(pEditText[i]);
            tempET.setText(pInfo[i]);
        }

        setImage(Uri.parse(SignInManager.patientPhotoUri), ivProfile);
        setImage(Uri.parse(SignInManager.patientPhotoUri), ivProfile);
    }

    public void showDialogBox(Context context, final int reqCode){
        new AlertDialog.Builder(context)
                .setTitle("Insert Profile Picture")
                .setMessage("Please select one option to upload profile picture")
                .setPositiveButton(R.string.gallery, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        accessGallery(reqCode);
                    }
                })
                .setNegativeButton(R.string.take_pic, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void accessGallery(int reqCode){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, reqCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Uri targetUri = data.getData();
            if(requestCode ==0) {
                setImage(targetUri, ivProfile);
            }else{
                setImage(targetUri, guardianivProfile);
            }
        }
    }

    protected void setImage(Uri targetUri, ImageView imgView){
            //BitmapFactory.Options o = new BitmapFactory.Options();
            //o.inSampleSize = 5;
            //bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri),
            //        null, o);
            //bitmap = shrinkBitmap(targetUri.toString(), 150, 150);
        if(targetUri.toString() == "")
            imgView.setImageResource(R.drawable.default_profile);
        else {
            imgView.setImageBitmap(
                    decodeSampledBitmapFromResource(targetUri, imgView.getWidth(), imgView.getHeight(), imgView));
            if(imgView.equals(ivProfile))
                wardProfileUri = targetUri.toString();
            else
                guardianProfileUri = targetUri.toString();
            Log.d("YelinDebug", targetUri.toString());
        }
            //imgView.setImageBitmap(bitmap);

    }

    public Bitmap decodeSampledBitmapFromResource(Uri targetUri, int reqWidth, int reqHeight,
                                                  ImageView imgView) {
        Bitmap bitmap;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(res, resId, options);
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri),
                    null, options);

            // Calculate inSampleSize

            if(reqWidth == 0 && reqHeight == 0){
                if(imgView.equals(ivProfile)) {
                    reqWidth = Integer.parseInt(SignInManager.patientPhotoWScale);
                    reqHeight = Integer.parseInt(SignInManager.patientPhotoHScale);
                }else{

                    reqWidth = Integer.parseInt(SignInManager.guardianPhotoWScale);
                    reqHeight = Integer.parseInt(SignInManager.guardianPhotoHScale);
                }
            }

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight, imgView);
            Log.d("YelinDebug","Sample Size: " + options.inSampleSize);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri),
                    null, options);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight,
                                     ImageView imgView) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {//height > reqHeight ||

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (//(halfHeight / inSampleSize) > reqHeight &&
                     (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        if(imgView.equals(ivProfile)) {
            SignInManager.patientPhotoWScale = String.valueOf(reqWidth);
            SignInManager.patientPhotoHScale = String.valueOf(reqHeight);
        }else{
            SignInManager.guardianPhotoWScale = String.valueOf(reqWidth);
            SignInManager.guardianPhotoHScale = String.valueOf(reqHeight);
        }
        return inSampleSize;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


}
