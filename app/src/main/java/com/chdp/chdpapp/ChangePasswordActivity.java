package com.chdp.chdpapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("修改密码");

        final EditText editOldPassword = (EditText) findViewById(R.id.edit_old_password);
        final EditText editNewPassword = (EditText) findViewById(R.id.edit_new_password);
        final EditText editRePassword = (EditText) findViewById(R.id.edit_re_password);
        Button btnChangePassword = (Button) findViewById(R.id.btn_change_password);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = editOldPassword.getText().toString();
                String newPassword = editNewPassword.getText().toString();
                String rePassword = editRePassword.getText().toString();
                if (!newPassword.equals(rePassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                } else {
                    changePassword(oldPassword, newPassword);
                }
            }
        });
    }

    private void changePassword(String oldPassword, String newPassword) {
        UserService service = ServiceGenerator.create(UserService.class);
        Call<AppResult> call = service.changePassword(oldPassword, newPassword);
        call.enqueue(new Callback<AppResult>() {
            @Override
            public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                if (response.isSuccessful()) {
                    AppResult result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(ChangePasswordActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                        ChangePasswordActivity.this.finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, result.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "修改密码失败，请重试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AppResult> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "修改密码失败，请重试", Toast.LENGTH_LONG).show();
            }
        });
    }
}
