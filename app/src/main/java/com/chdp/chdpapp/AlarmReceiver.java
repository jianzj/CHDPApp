package com.chdp.chdpapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.chdp.chdpapp.util.ContextHolder;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");

        NotificationManager manager = (NotificationManager) ContextHolder.getContext().getSystemService(ContextHolder.getContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ContextHolder.getContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setTicker(title);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringURI);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000});
        builder.setContentIntent(PendingIntent.getActivity(ContextHolder.getContext(), id, new Intent(ContextHolder.getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify(id, builder.build());
    }
}
