package com.example.darrenlim.list;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null){
            return;
        }
        Intent i = new Intent(context, AlarmService.class)
                .putExtra("notify", intent.getBooleanExtra("notify", true))
                .putExtra("title", intent.getStringExtra("title"))
                .putExtra("notes",intent.getStringExtra("notes"))
                .putExtra("category",intent.getStringExtra("category"));
        context.startService(i);
    }
}
