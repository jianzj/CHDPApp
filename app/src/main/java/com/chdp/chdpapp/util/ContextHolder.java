package com.chdp.chdpapp.util;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.chdp.chdpapp.AlarmReceiver;

public class ContextHolder {
    private final ContextHolder self = this;

    static Context ApplicationContext;

    public static void init(Context context) {
        ApplicationContext = context;
    }

    public static Context getContext() {
        if (ApplicationContext == null) {
            try {
                Application application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Application application = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    ApplicationContext = application;
                    return application;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
        }
        return ApplicationContext;
    }

    public static void setAlarm(int id, int minutes, String title, String msg) {
        AlarmManager manager = (AlarmManager) ApplicationContext.getSystemService(ApplicationContext.ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000;
        Intent i = new Intent(ApplicationContext, AlarmReceiver.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        i.putExtra("msg", msg);
        PendingIntent pi = PendingIntent.getBroadcast(ApplicationContext, id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }
}