package com.example.pushtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    public MyInstanceIDListenerService() {
    }

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
    }
}
