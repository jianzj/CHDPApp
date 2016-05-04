package com.chdp.chdpapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.chdp.chdpapp.bean.Prescription;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.bean.UserAuthority;
import com.chdp.chdpapp.service.PrescriptionService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.AuthHelper;
import com.chdp.chdpapp.util.Constants;
import com.chdp.chdpapp.util.ContextHolder;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends ActionBarActivity {
    private UserAuthority auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = getIntent();
        auth = (UserAuthority) intent.getSerializableExtra("auth");
        user = AuthHelper.checkUser(this);
        setTitle("处方扫描");

        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "请扫描处方标签");
        intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
        intent.setClass(ScanActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    checkPrescription(data.getStringExtra(Intents.Scan.RESULT));
                    break;
                default:
                    Toast.makeText(this, "条码扫描失败，请重试", Toast.LENGTH_LONG).show();
                    this.finish();
            }
        } else {
            this.finish();
        }
    }

    private void checkPrescription(String uuid) {
        PrescriptionService service = ServiceGenerator.create(PrescriptionService.class, user.getSession_id());
        Call<Prescription> call = service.getPrescription(uuid);
        call.enqueue(new Callback<Prescription>() {
            @Override
            public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                if (response.isSuccessful()) {
                    Prescription prs = response.body();
                    if (prs == null) {
                        Toast.makeText(ContextHolder.getContext(), "请求的处方不存在，请重试", Toast.LENGTH_LONG).show();
                        ScanActivity.this.finish();
                    } else {
                        int process = prs.getProcess();
                        if (auth.ordinal() == process) {
                            Intent intent = new Intent();
                            intent.putExtra("prescription", prs);
                            switch (process) {
                                case Constants.CHECK:
                                    intent.setClass(ScanActivity.this, CheckActivity.class);
                                    break;
                                case Constants.MIX:
                                    intent.setClass(ScanActivity.this, MixActivity.class);
                                    break;
                                case Constants.MIXCHECK:
                                    intent.setClass(ScanActivity.this, MixCheckActivity.class);
                                    break;
                                case Constants.SOAK:
                                    intent.setClass(ScanActivity.this, SoakActivity.class);
                                    break;
                            }
                            ScanActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(ContextHolder.getContext(), "处方当前处于“" + Constants.getProcessName(process) +
                                    "”阶段，无法进行“" + Constants.getProcessName(auth.ordinal()) + "”操作", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(ContextHolder.getContext(), "请求处方信息失败，请重试", Toast.LENGTH_LONG).show();
                }
                ScanActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Prescription> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "请求处方信息失败，请重试", Toast.LENGTH_LONG).show();
                ScanActivity.this.finish();
            }
        });
    }
}
