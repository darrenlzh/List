package com.example.darrenlim.list;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


public class AlarmService extends Service{

    @Override
    public IBinder onBind(Intent arg0) {return null;}

    @Override
    public void onCreate() {super.onCreate();}

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(intent == null){
            return;
        }

        Intent i = new Intent(getApplicationContext(),CardDetails.class)
                .putExtra("notify",intent.getBooleanExtra("notify",true))
                .putExtra("title",intent.getStringExtra("title"))
                .putExtra("notes",intent.getStringExtra("notes"))
                .putExtra("category",intent.getStringExtra("category"));
        PendingIntent pI = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder n  = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Reminder")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_add_white_24dp)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pI)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager nf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nf.notify(1, n.build());
    }

    @Override
    public void onDestroy() {super.onDestroy();}
}