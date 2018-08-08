package com.example.cqc.testopencv.jobservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OpenserviceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(context,AlwaysRunningService.class);
            context.startService(serviceIntent);
        Log.d("我的广播接收者","--------------------");
    }
}
