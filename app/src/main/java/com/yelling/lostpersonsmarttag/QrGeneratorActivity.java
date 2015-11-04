package com.yelling.lostpersonsmarttag;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;

//import android.support.v4.app.Fragment;


public class QrGeneratorActivity extends Fragment {

    protected ImageView ivQrCode;
    protected Bitmap qrCodeBitmap;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static QrGeneratorActivity newInstance(int sectionNumber) {
        QrGeneratorActivity fragment = new QrGeneratorActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public QrGeneratorActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_qr_generator, container, false);
        ivQrCode = (ImageView)rootView.findViewById(R.id.ivQrCode);

        Button btnGenerate = (Button)rootView.findViewById(R.id.btnGenerate);
        Button btnSaveQR = (Button)rootView.findViewById(R.id.btnSaveQR);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    createRequest(rootView);
                }
                else
                    Toast.makeText(getActivity(), "Connection not availabe. Please check your network" +
                            " Connection.", Toast.LENGTH_LONG).show();
            }
        });


        btnSaveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MediaStore.Images.Media.insertImage(getContentResolver(), yourBitmap, yourTitle , yourDescription);
                //String url = CapturePhotoUtils.insertImage(getActivity().getContentResolver(), qrCodeBitmap, "Smart Tag QR Code", "Smart tag qr");
                createDirectoryAndSaveFile(qrCodeBitmap, "Lost_Tag_" + MainActivity.myStaticWard.name);
                //MediaScannerConnection.MediaScannerConnectionClient mediaClient = new MediaScannerConnection.MediaScannerConnectionClient();
                String url = "file://" + Environment.getExternalStorageDirectory();
                //new MediaScannerConnection(getActivity().getApplicationContext(), null).scanFile(url, MimeTypeMap.getFileExtensionFromUrl(url));
                //getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                  //      + Environment.getExternalStorageDirectory())));
                Toast.makeText(getActivity().getApplicationContext(), "Saved image at " +
                        "SmartTag Folder.", Toast.LENGTH_LONG).show();



            }
        });

        return rootView;
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DCIM/SmartTag");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/SmartTag/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/SmartTag"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(getActivity(), new String[]{

                        file.getAbsolutePath()},

                null, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri)

                    {
                        //Toast.makeText(getActivity().getApplicationContext(), "Saved image at " +
                        //"gallery.", Toast.LENGTH_LONG).show();
                        Log.d("YeLinDebug", "Saved image at " + uri.toString());

                    }

                });


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void createRequest(final View rootView){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("YellingDebug", "Width: " + Integer.toString(width));
        String link = MainActivity.SERVER_URI.substring(0, MainActivity.SERVER_URI.length() - 10);
        Log.d("YeLinDebug", "Link: " + link);
        String url ="https://api.qrserver.com/v1/create-qr-code/?size=" + Integer.toString(width) +
                "x" +Integer.toString(width) + "&data=" + link + "/Pages/find?q=" +
                MainActivity.myStaticWard.ward_tag;

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        ivQrCode.requestLayout();
                        ivQrCode.getLayoutParams().height = 720;
                        ivQrCode.setImageBitmap(bitmap);
                        qrCodeBitmap = bitmap;
                        Button btnSaveQR = (Button)rootView.findViewById(R.id.btnSaveQR);
                        btnSaveQR.setEnabled(true);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        ivQrCode.setImageResource(R.drawable.error);
                    }
                });


        /*
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("YellingDebug","There is response");
                        Log.d("YellingDebug","Response is: " + response.substring(0,100));

                        //tvResult.setText("Response is: "+ response.substring(0,500));
                        //ivQrCode.setImageBitmap(Bitmap.);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvResult.setText("That didn't work!");
            }
        });

        */
        // Add the request to the RequestQueue.
        queue.add(request);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
