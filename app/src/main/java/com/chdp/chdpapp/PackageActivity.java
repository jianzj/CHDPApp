package com.chdp.chdpapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.ContextHolder;
import com.chdp.chdpapp.util.PrescriptionHelper;
import com.chdp.chdpapp.util.ProcessHelper;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageActivity extends WithProcessActivity {
    private Button btnPackage;
    private Button btnPackageCancel;

    private CheckBox checkLeak;
    private CheckBox checkNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        setTitle("包装处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        btnPackage = (Button) findViewById(R.id.btn_package);
        btnPackageCancel = (Button) findViewById(R.id.btn_package_back);

        btnPackage.setOnClickListener(new ForwardClickListener());
        btnPackageCancel.setOnClickListener(new BackwardClickListener());

        checkLeak = (CheckBox) findViewById(R.id.check_leak);
        checkNum = (CheckBox) findViewById(R.id.check_num);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (!data.getStringExtra(Intents.Scan.RESULT).equals(prescription.getUuid())) {
                        Toast.makeText(ContextHolder.getContext(), "包装标签与处方不符，请检查后重试", Toast.LENGTH_LONG).show();
                    } else {
                        final ProgressDialog pd = ProgressDialog.show(PackageActivity.this, "", "处理中...", true);

                        ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                        Call<AppResult> call = service.pack(prescription.getId(), presentProc.getId());
                        call.enqueue(new Callback<AppResult>() {
                            @Override
                            public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                if (response.isSuccessful()) {
                                    AppResult result = response.body();
                                    if (result.isSuccess()) {
                                        Toast.makeText(ContextHolder.getContext(), "完成包装成功", Toast.LENGTH_LONG).show();
                                        PackageActivity.this.finish();
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), "完成包装失败，请重试", Toast.LENGTH_LONG).show();
                                }
                                pd.dismiss();
                            }

                            @Override
                            public void onFailure(Call<AppResult> call, Throwable t) {
                                Toast.makeText(ContextHolder.getContext(), "完成包装失败，请重试", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        });
                    }
                    break;
                default:
                    Toast.makeText(this, "条码扫描失败，请重试", Toast.LENGTH_LONG).show();
                    this.finish();
            }
        } else {
            this.finish();
        }
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(PackageActivity.this).setMessage("确认完成包装？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intents.Scan.ACTION);
                            intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "请扫描包装标签");
                            intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
                            intent.setClass(PackageActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, 200);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    private class BackwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PackageActivity.this).setMessage("确认退回清场？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(PackageActivity.this, "", "处理中...", true);
                    String reason = "";
                    if (checkLeak.isChecked())
                        reason += "漏液等异装 ";
                    if (checkNum.isChecked())
                        reason += "数量不符 ";

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.packCancel(prescription.getId(), presentProc.getId(), reason.equals("") ? "未知原因" : reason);
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回清场成功", Toast.LENGTH_LONG).show();
                                    PackageActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回清场失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回清场失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
