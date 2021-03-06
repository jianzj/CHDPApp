package com.chdp.chdpapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.service.UserService;
import com.chdp.chdpapp.util.AuthHelper;
import com.chdp.chdpapp.util.ContextHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ActionBarActivity {
    private EditText usercodeEdit;
    private EditText passwordEdit;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户登录");
    }

    @Override
    protected void onResume() {
        super.onResume();
        usercodeEdit = (EditText) findViewById(R.id.usercode_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usercode = usercodeEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                login(usercode, password);
            }
        });
    }

    private void login(String usercode, String password) {
        if (usercode.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        UserService service = ServiceGenerator.create(UserService.class);
        Call<AppResult> call = service.login(usercode, password);
        call.enqueue(new Callback<AppResult>() {
            @Override
            public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                if (response.isSuccessful()) {
                    AppResult result = response.body();
                    if (result.isSuccess()) {
                        afterLogin(result);
                    } else {
                        Toast.makeText(LoginActivity.this, result.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AppResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void afterLogin(final AppResult result) {
        UserService service = ServiceGenerator.create(UserService.class, result.getSessionId());
        Call<User> call = service.getUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    user.setSession_id(result.getSessionId());
                    AuthHelper.setUser(user);
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(ContextHolder.getContext(), "请求用户信息失败，请重新登录", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "请求用户信息失败，请重新登录", Toast.LENGTH_LONG).show();
            }
        });
    }
}
