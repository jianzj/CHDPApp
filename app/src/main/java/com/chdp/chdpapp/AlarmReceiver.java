package com.chdp.chdpapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
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
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setVibrate(new long[] {0,300,500,700});
        builder.setLights(0xff0000ff, 300, 0);
        builder.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"));
        builder.setContentIntent(PendingIntent.getActivity(ContextHolder.getContext(), id, new Intent(ContextHolder.getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        manager.notify(id, builder.build());
    }
}
