package com.example.krishna.gcmdemoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by krishna on 19/5/15.
 */
public class GCMPreference {

    private static final String PREFERENCE_NAME = "KiiTest";
    private static final String PROPERTY_REG_ID = "GCMregId";

    static String getRegistrationId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        return registrationId;
    }

    static void setRegistrationId(Context context, String regId) {
        Toast.makeText(context, "Successfully registered...", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.commit();
    }
}


