package com.chdp.chdpapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MixCheckActivity extends WithProcessActivity {
    private Button btnMixcheck;
    private Button btnMixcheckCancel;

    private CheckBox checkWrong;
    private CheckBox checkMiss;
    private CheckBox checkMore;
    private CheckBox checkDup;
    private CheckBox checkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_check);
        setTitle("调配审核处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        btnMixcheck = (Button) findViewById(R.id.btn_miccheck);
        btnMixcheckCancel = (Button) findViewById(R.id.btn_mixcheck_back);

        checkWrong = (CheckBox) findViewById(R.id.check_wrong);
        checkMiss = (CheckBox) findViewById(R.id.check_miss);
        checkMore = (CheckBox) findViewById(R.id.check_more);
        checkDup = (CheckBox) findViewById(R.id.check_dup);
        checkAll = (CheckBox) findViewById(R.id.check_all);

        btnMixcheck.setOnClickListener(new ForwardClickListener());
        btnMixcheckCancel.setOnClickListener(new BackwardClickListener());
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MixCheckActivity.this).setMessage("确认完成调配审核？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(MixCheckActivity.this, "", "处理中...", true);

                            ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                            Call<AppResult> call = service.mixcheck(prescription.getId(), presentProc.getId());
                            call.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "完成调配审核成功", Toast.LENGTH_LONG).show();
                                            MixCheckActivity.this.finish();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "完成调配审核失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "完成调配审核失败，请重试", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MixCheckActivity.this).setMessage("确认退回调配？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(MixCheckActivity.this, "", "处理中...", true);
                    String reason = "";
                    if (checkWrong.isChecked())
                        reason += "错配 ";
                    if (checkMiss.isChecked())
                        reason += "漏配 ";
                    if (checkMore.isChecked())
                        reason += "多配 ";
                    if (checkDup.isChecked())
                        reason += "重配 ";
                    if (checkAll.isChecked())
                        reason += "总量不符 ";

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.mixcheckCancel(prescription.getId(), presentProc.getId(), reason.equals("") ? "未知原因" : reason);
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回调配成功", Toast.LENGTH_LONG).show();
                                    MixCheckActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回调配失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回调配失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
