package com.example.medsense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class notiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Awaking Service responsible for history management
        Intent intent2 = new Intent(context, MessagePassingService.class);
        context.startService(intent2);
        Log.d("BROADCAST RECEIVER: ", "Success");
    }
}
