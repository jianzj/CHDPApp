package com.chdp.chdpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.bean.UserAuthority;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.service.UserService;
import com.chdp.chdpapp.util.AuthHelper;
import com.chdp.chdpapp.util.ContextHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ActionBarActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = AuthHelper.checkUser(this);
        if (user == null)
            return;

        setTitle("当前用户：" + user.getName());
        generateIcon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_logout:
                UserService service = ServiceGenerator.create(UserService.class, user.getSession_id());
                Call<Void> call = service.logout();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            AuthHelper.resetUser();
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(ContextHolder.getContext(), "注销失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ContextHolder.getContext(), "注销失败，请重试", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.menu_change_password:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ChangePasswordActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
        return true;
    }

    private void generateIcon() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_main);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 10, 0);
        layout.removeAllViews();
        for (final UserAuthority auth : user.getAuthorityList()) {
            if (auth == UserAuthority.ADMIN || auth == UserAuthority.RECEIVE)
                continue;
            Button btn = new Button(this);
            if (auth == UserAuthority.CHECK) {
                btn.setText("审方处理");
            } else if (auth == UserAuthority.MIX) {
                btn.setText("调配处理");
            } else if (auth == UserAuthority.MIXCHECK) {
                btn.setText("调配审核处理");
            } else if (auth == UserAuthority.SOAK) {
                btn.setText("浸泡处理");
            } else if (auth == UserAuthority.DECOCT) {
                btn.setText("煎煮处理");
            } else if (auth == UserAuthority.POUR) {
                btn.setText("灌装处理");
            } else if (auth == UserAuthority.CLEAN) {
                btn.setText("清场处理");
            } else if (auth == UserAuthority.PACKAGE) {
                btn.setText("包装处理");
            } else if (auth == UserAuthority.SHIP) {
                btn.setText("运输处理");
            }
            btn.setLayoutParams(params);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("auth", auth);
                    intent.setClass(MainActivity.this, ScanActivity.class);
                    MainActivity.this.startActivityForResult(intent, 200);
                }
            });
            layout.addView(btn);
        }
    }
}
