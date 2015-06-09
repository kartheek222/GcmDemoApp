package com.example.krishna.gcmdemoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kii.cloud.storage.DirectPushMessage;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.PushMessageBundleHelper;
import com.kii.cloud.storage.PushToAppMessage;
import com.kii.cloud.storage.PushToUserMessage;
import com.kii.cloud.storage.ReceivedMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * Created by krishna on 19/5/15.
 */
public class KiiPushBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        showNotifAsBlocked(context, "You got message form gcm " + messageType);

        Log.d("KiiPushBroadcast", "onReceive (Line:39) : GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE"+GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE);
        Log.d("KiiPushBroadcast", "onReceive (Line:40) messageType :"+messageType);

        fileLog("KiiPushBroadcast", "Received message : BUndleString 9999" + intent.getExtras().toString());

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Bundle extras = intent.getExtras();

            if (extras == null) {
                showNotifAsBlocked(context, "Intent extra is null");
            } else {
                showNotifAsBlocked(context, "Intent extra is not a null");
            }

            try {
                Set<String> cats = intent.getCategories();
                for (String cat : cats) {

                    String val = null;
                    try {
                        val = intent.getStringExtra(cat);
                    } catch (Exception e) {
                        val = "error";
                    }

                    Log.d("KiiPushBroadcast", "onReceive (Line:53) Intent:"+cat+" "+val);
                    showNotifAsBlocked(context, " Intent CatKeys :" + cat + "  " + val);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showNotifAsBlocked(context, "Intent categories are null");
            }

            try {
                Set<String> keys = extras.keySet();

                for (String key : keys) {

                    String val = null;
                    try {
                        val = intent.getStringExtra(key);
                    } catch (Exception e) {
                        val = "error";
                    }

                    showNotifAsBlocked(context, " Intent CatKeys :" + key + "  " + val);
                    Log.d("KiiPushBroadcast", "onReceive (Line:73) Extras:"+key+" "+val);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showNotifAsBlocked(context, "extras key set is null");
            }

            ReceivedMessage message = PushMessageBundleHelper.parse(extras);
            KiiUser sender = message.getSender();
            PushMessageBundleHelper.MessageType type = message.pushMessageType();

            Log.d("KiiPushBroadcast", "onReceive (Line:85) :"+"type:"+type);
           // showNotifAsBlocked(context, "You got message :" + message.getMessage() + " " + message.getSender().getDisplayname());

            Toast.makeText(context, "message" + "GCM", Toast.LENGTH_SHORT).show();
            switch (type) {
                case PUSH_TO_APP:
                    Log.d("KiiPushBroadcast", "onReceive (Line:91) : PushToApp");
                    PushToAppMessage appMsg = (PushToAppMessage) message;
                    break;
                
                case PUSH_TO_USER:
                    PushToUserMessage userMsg = (PushToUserMessage) message;
                    Log.d("KiiPushBroadcast", "onReceive (Line:97) :"+"PushToUser");
                    break;
                case DIRECT_PUSH:
                    DirectPushMessage directMsg = (DirectPushMessage) message;
                    if(directMsg==null){
                        Log.d("KiiPushBroadcast", "onReceive (Line:102) : DirectPushMsg is null");
                    }else {

                        KiiUser msgSender = directMsg.getSender();

                        if(msgSender==null){
                            Log.d("KiiPushBroadcast", "onReceive (Line:108) :"+"kii sender is null");
                        }else {
                            String dispName = msgSender.getDisplayname();
                            String email = msgSender.getEmail();
                            String uname = msgSender.getUsername();
                            Log.d("KiiPushBroadcast", "onReceive (Line:113) : DispName:"+dispName+" Email:"+email+" uName:"+uname);
                        }

                        PushMessageBundleHelper.MessageType msgType = directMsg.pushMessageType();
                        Log.d("KiiPushBroadcast", "onReceive (Line:117) :"+messageType);

                        Bundle msg = directMsg.getMessage();
                        Log.d("KiiPushBroadcast", "onReceive (Line:101) :" + "DirectPush");
                        if(msg==null){
                            Log.d("KiiPushBroadcast", "onReceive (Line:107) :DirectPushMsg bundle is null");
                        }else{
                            try {
                                Set<String> keys = msg.keySet();

                                for (String key : keys) {

                                    String val = null;
                                    try {
                                        val = intent.getStringExtra(key);
                                    } catch (Exception e) {
                                        val = "error";
                                    }

                                    showNotifAsBlocked(context, " Intent CatKeys :" + key + "  " + val);
                                    Log.d("KiiPushBroadcast", "onReceive (Line:73) Extras:" + key + " " + val);
                                }


                            }catch (Exception e){
                                e.printStackTrace();
                                Log.d("KiiPushBroadcast", "onReceive (Line:126) :"+"DirectMsgException");
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void fileLog(String tag, String message) {
        Log.i(tag, message);
        final String path = "pushlog.txt";
        File f = new File(Environment.getExternalStorageDirectory(), path);
        BufferedWriter bw = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(f, true));
            bw.write(tag + " : " + message);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Shows the notification message for the given phoneNumber.
     *
     * @param context
     * @param message
     */
    public static void showNotifAsBlocked(Context context, String message) {
        int id = new Random().nextInt(1000);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("GMC MSG:" + id)
                .setContentText(" Msg: " + message).setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, noti);
    }
}