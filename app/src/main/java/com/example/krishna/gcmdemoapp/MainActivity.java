package com.example.krishna.gcmdemoapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kii.cloud.storage.KiiUser;


public class MainActivity extends Activity {
    public static final String SENDER_ID = "648972542694";
    //private static final String USER_NAME = "pvkrishnavasamsetti@gmail.com";
    //private static final String USER_NAME = "Krishna Vasamsetti";

    private static final String USER_NAME = "pvkrishnavasamsetti";
    private static final String PASSWORD = "krishna1654";

   // private static final String USER_NAME = "pavan";
   // private static final String PASSWORD = "pavan123";

    private GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the instance of GoogleCloudMessaging.
        gcm = GoogleCloudMessaging.getInstance(this.getApplicationContext());

        // if the id is saved in the preference, it skip the registration and just install push.
        String regId = GCMPreference.getRegistrationId(this.getApplicationContext());
        if (regId.isEmpty()) {
            registerGCM();
        }
    }

    private void registerGCM() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    // call register
                    String regId = gcm.register(SENDER_ID);

                    // login
                    KiiUser.logIn(USER_NAME, PASSWORD);

                    // install user device
                    KiiUser.pushInstallation().install(regId);

                    // if all succeeded, save registration ID to preference.
                    GCMPreference.setRegistrationId(MainActivity.this.getApplicationContext(), regId);
                    return null;
                } catch (Exception e) {
                    // Error Handling
                    e.printStackTrace();

                    Log.d("MainActivity", "doInBackground (Line:54) :"+"msg:"+e.getMessage());
                    return null;
                }
            }
        }.execute();
    }
}
