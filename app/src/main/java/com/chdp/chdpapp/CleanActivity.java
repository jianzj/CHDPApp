package com.chdp.chdpapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.ContextHolder;
import com.chdp.chdpapp.util.PrescriptionHelper;
import com.chdp.chdpapp.util.ProcessHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CleanActivity extends WithProcessActivity {
    private Button btnClean;
    private Button btnCleanCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        setTitle("清场处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        btnClean = (Button) findViewById(R.id.btn_clean);
        btnCleanCancel = (Button) findViewById(R.id.btn_clean_back);

        btnClean.setOnClickListener(new ForwardClickListener());
        btnCleanCancel.setOnClickListener(new BackwardClickListener());
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long now = System.currentTimeMillis();
            long begin = 0;
            try {
                begin = df.parse(presentProc.getBegin()).getTime();
            } catch (ParseException e) {
                Toast.makeText(ContextHolder.getContext(), "获取清场时间失败，请重试", Toast.LENGTH_LONG).show();
                return;
            }
            new AlertDialog.Builder(CleanActivity.this).setMessage("清场时长：" + Math.ceil((now - begin) / 1000 / 60.0) + "，确认完成？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(CleanActivity.this, "", "处理中...", true);

                            ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                            Call<AppResult> call = service.clean(prescription.getId(), presentProc.getId());
                            call.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "完成清场成功", Toast.LENGTH_LONG).show();
                                            CleanActivity.this.finish();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "完成清场失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "完成清场失败，请重试", Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    private class BackwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CleanActivity.this).setTitle("退回灌装原因");
            final EditText input = new EditText(CleanActivity.this);
            builder.setView(input);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(CleanActivity.this, "", "处理中...", true);
                    if (input.getText().toString().equals("")) {
                        input.setText("未知原因");
                    }

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.cleanCancel(prescription.getId(), presentProc.getId(), input.getText().toString());
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回灌装成功", Toast.LENGTH_LONG).show();
                                    CleanActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回灌装失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回灌装失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
