package com.chdp.chdpapp.util;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.chdp.chdpapp.ChangePasswordActivity;
import com.chdp.chdpapp.LoginActivity;
import com.chdp.chdpapp.R;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuHelper {
    public static void handleMenu(final Activity activity, Menu menu, final User user) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                UserService service = ServiceGenerator.create(UserService.class, user.getSession_id());
                Call<Void> call = service.logout();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            AuthHelper.resetUser();
                            Intent intent = new Intent();
                            intent.setClass(activity, LoginActivity.class);
                            activity.startActivity(intent);
                        } else {
                            Toast.makeText(ContextHolder.getContext(), "注销失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ContextHolder.getContext(), "注销失败，请重试", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
        });
		
		menu.findItem(R.id.menu_change_password).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.setClass(activity, ChangePasswordActivity.class);
                activity.startActivity(intent);

                return true;
            }
        });
    }
}
