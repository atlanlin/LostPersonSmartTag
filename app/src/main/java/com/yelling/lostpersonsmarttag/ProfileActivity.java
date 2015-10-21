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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class ProfileActivity extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ViewGroup classContainer;
    private ImageView ivProfile;
    private String profileUri;
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
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        classContainer = container;
        Button btnApply = (Button)rootView.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(myListener);
        Button btnCancel = (Button)rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(myListener);
        ivProfile = (ImageView)rootView.findViewById(R.id.profileImage);
        //if(profileUri!=null)
        //Log.d("YeLinDebug", "URI: " + profileUri);
            //setImage(Uri.parse(profileUri));
        ivProfile.setOnClickListener(myListener);
        retrieveInfo(rootView);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            switch (v.getId()){
                case R.id.btnApply:
                    Boolean request = validateFillinInformations();
                    request = updateToDatabase() && request;
                    if(request == true)
                        Toast.makeText(getActivity(), "Information has updated", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Unable to update, please validate information", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btnCancel:

                    break;
                case R.id.profileImage:
                    showDialogBox(getActivity());
                    break;
            }
        }
    };



    private boolean validateFillinInformations(){
        return true;
    }

    private boolean updateToDatabase(){
        return true;
    }

    private void retrieveInfo(View rootView){

        String[] gInfo = {MainActivity.accountId, "8453544", "This is his niece."};
        String[] pInfo = {"Ah Lin", "Block 412, Jurong West Street 23, Singapore 640412",
                "Person is currently suffering dementia"};

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
    }

    public void showDialogBox(Context context){
        new AlertDialog.Builder(context)
                .setTitle("Insert Profile Picture")
                .setMessage("Please select one option to upload profile picture of patient")
                .setPositiveButton(R.string.gallery, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        accessGallery();
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

    public void accessGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri targetUri = data.getData();
            setImage(targetUri);
        }
    }

    protected void setImage(Uri targetUri){


            //BitmapFactory.Options o = new BitmapFactory.Options();
            //o.inSampleSize = 5;
            //bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri),
            //        null, o);
            //bitmap = shrinkBitmap(targetUri.toString(), 150, 150);
            ivProfile.setImageBitmap(
                    decodeSampledBitmapFromResource(targetUri, ivProfile.getWidth(), ivProfile.getHeight()));
            profileUri = targetUri.toString();
            //ivProfile.setImageBitmap(bitmap);

    }

    public Bitmap decodeSampledBitmapFromResource(Uri targetUri, int reqWidth, int reqHeight) {
        Bitmap bitmap;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(res, resId, options);
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri),
                    null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
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

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

        return inSampleSize;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here


    }
}
