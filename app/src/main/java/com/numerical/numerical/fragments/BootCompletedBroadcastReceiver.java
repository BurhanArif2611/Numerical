package com.numerical.numerical.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       /* Intent serviceIntent = new Intent(context, LService.class);
        context.startService(serviceIntent);*/
        Log.e("BroadcastReceiver", "Started");
    }
}
