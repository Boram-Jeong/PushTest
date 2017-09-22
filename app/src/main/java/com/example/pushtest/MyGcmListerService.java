package com.example.pushtest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListerService extends GcmListenerService {
    public MyGcmListerService() {
    }

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
//        super.onMessageReceived(s, bundle);
        String message = bundle.getString("message");
        Log.d("test", "From: " + s);
        Log.d("test", "Message: " + message);
    }

    private void sendNotification(String str) {

    }
}
