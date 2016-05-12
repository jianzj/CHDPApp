package com.chdp.chdpapp.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.chdp.chdpapp.LoginActivity;
import com.chdp.chdpapp.bean.User;

public final class AuthHelper {
    private static User user;

    public static User getUser() {
        if (user == null) {
            SharedPreferences settings = ContextHolder.getContext().getSharedPreferences("setting", 0);
            String usercode = settings.getString("usercode", null);
            String name = settings.getString("name", null);
            int authority = settings.getInt("authority", -1);
            String sessionId = settings.getString("sessionId", null);
            if (sessionId == null)
                return null;
            else {
                User user = new User();
                user.setUsercode(usercode);
                user.setName(name);
                user.setAuthority(authority);
                user.setSession_id(sessionId);
                AuthHelper.user = user;
                return user;
            }
        } else
            return AuthHelper.user;
    }

    public static void resetUser() {
        SharedPreferences settings = ContextHolder.getContext().getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        AuthHelper.user = null;
    }

    public static void setUser(User user) {
        SharedPreferences settings = ContextHolder.getContext().getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usercode", user.getUsercode());
        editor.putString("name", user.getName());
        editor.putInt("authority", user.getAuthority());
        editor.putString("sessionId", user.getSession_id());
        editor.commit();
        AuthHelper.user = user;
    }

    public static User checkUser(Activity activity) {
        User user = AuthHelper.getUser();
        if (user == null) {
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            activity.startActivity(intent);
            Toast.makeText(ContextHolder.getContext(), "登录失效，请重新登录", Toast.LENGTH_LONG).show();
        }
        return user;
    }
}
