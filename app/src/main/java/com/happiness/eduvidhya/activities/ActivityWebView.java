package com.happiness.eduvidhya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.happiness.eduvidhya.R;
import com.happiness.eduvidhya.datamodels.ModelAttendenceUser;
import com.happiness.eduvidhya.datamodels.ModelMeetingHistories;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ActivityWebView extends AppCompatActivity {

    private WebView wv1;
    FirebaseFirestore firebaseFirestore;
    String linkMy,meetingID,email,type,facultyEmailWhenUserComeOnThisScreen,classname;
    SharedPreferences mySharedPreferences;

    String strCheckMeetingWorkingOrExpired = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        firebaseFirestore = FirebaseFirestore.getInstance();

        wv1 = (WebView) findViewById(R.id.webview);

        mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }


        wv1.getSettings().setDomStorageEnabled(true);
        wv1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv1.getSettings().setAppCacheEnabled(true);
        wv1.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        wv1.getSettings().setDatabaseEnabled(true);
        wv1.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        wv1.getSettings().setSaveFormData(true);

        wv1.setWebChromeClient(new WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });


        wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setAllowFileAccess(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv1.getSettings().setLoadWithOverviewMode(true);
        wv1.getSettings().setDefaultTextEncodingName("utf-8");
        wv1.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv1.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

//        wv1.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        wv1.getSettings().setUserAgentString(wv1.getSettings().getUserAgentString().replace("; wv", ""));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wv1.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        linkMy = getIntent().getExtras().getString("url");
        meetingID = getIntent().getExtras().getString("meetingID");
        classname = getIntent().getExtras().getString("classname");
        facultyEmailWhenUserComeOnThisScreen = getIntent().getExtras().getString("facultyEmailWhenUserComeOnThisScreen");


        wv1.loadUrl(linkMy);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String type = mySharedPreferences.getString("type", "");
                handler.removeCallbacksAndMessages(null);
                if (type.equals("Faculty"))
                {

                    String email = mySharedPreferences.getString("user_email", "");

                    DocumentReference document = firebaseFirestore.collection("Meetings").document(email).collection("Classes").document(classname).collection("Meetings").document(meetingID);
                    document.update("meetingStatus","1");
                }

            }
        },5000);
    }
    Handler handler;

    private class MyBrowser extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (url.equals("https://noblekeyz.com/b")) {
                // meeting expired

                if (strCheckMeetingWorkingOrExpired.equals("0"))
                {
                    String type = mySharedPreferences.getString("type", "");

                    if (type.equals("Faculty"))
                    {

                        mFacultyMeetingStatus();
                    }
                    handler.removeCallbacksAndMessages(null);
                    finish();
                }

            }
            else if (url.equals("https://noblekeyz.com/")) {
                // End meeting click or Logout

                strCheckMeetingWorkingOrExpired = "1";

                email = mySharedPreferences.getString("user_email", "");
                type = mySharedPreferences.getString("type", "");

                dismissClass(email,type);

            }
            else {
//                String type = mySharedPreferences.getString("type", "");
//                handler.removeCallbacksAndMessages(null);
//                if (type.equals("Faculty"))
//                {
//
//                    mFacultyMeetingStatus();
//                }
//                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {

    }

    public void dismissClass(String email,String type)
    {


        if (type.equals("Faculty"))
        {
            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            Date currentTime = localCalendar.getTime();
            int currentDay = localCalendar.get(Calendar.DATE);
            int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
            int currentYear = localCalendar.get(Calendar.YEAR);
            String dateAndTime = String.valueOf(currentDay)+"-"+String.valueOf(currentMonth)+String.valueOf(currentYear);
            ModelMeetingHistories modelMeetingHistories = new ModelMeetingHistories("",meetingID,dateAndTime,currentTime.toString(),"3","");

            DocumentReference document = firebaseFirestore.collection("Faculties").document(email).collection("MeetingHistory").document(meetingID);
            document.set(modelMeetingHistories).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
                }
            });


        }
        else if (type.equals("User"))
        {
            String FacultyEmail = facultyEmailWhenUserComeOnThisScreen;

            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            Date currentTime = localCalendar.getTime();
            int currentDay = localCalendar.get(Calendar.DATE);
            int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
            int currentYear = localCalendar.get(Calendar.YEAR);


            String dateAndTime = String.valueOf(currentDay)+"-"+String.valueOf(currentMonth)+String.valueOf(currentYear)+" "+String.valueOf(currentTime);

            ModelAttendenceUser modelAttendenceUser = new ModelAttendenceUser(email,meetingID,dateAndTime,"P");
            firebaseFirestore.collection("Faculties").document(FacultyEmail).collection("MeetingHistory").document(meetingID).collection("Users").document(email).set(modelAttendenceUser);
        }
        finish();
    }


    private void mFacultyMeetingStatus()
    {
        String email = mySharedPreferences.getString("user_email", "");

        DocumentReference document = firebaseFirestore.collection("Meetings").document(email).collection("Classes").document(classname).collection("Meetings").document(meetingID);
        document.update("meetingStatus","2");

//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}