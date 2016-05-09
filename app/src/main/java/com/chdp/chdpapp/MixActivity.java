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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MixActivity extends WithProcessActivity {
    private Button btnMixStart;
    private Button btnMixFinish;;
    private Button btnMixCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);
        setTitle("调配处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

		btnMixStart = (Button) findViewById(R.id.btn_mix_start);
        btnMixFinish = (Button) findViewById(R.id.btn_mix_finish);
        btnMixCancel = (Button) findViewById(R.id.btn_mix_back);

		btnMixStart.setOnClickListener(new StartClickListener());
        btnMixFinish.setOnClickListener(new ForwardClickListener());
        btnMixCancel.setOnClickListener(new BackwardClickListener());
    }

	private class StartClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MixActivity.this).setMessage("确认开始调配？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
							Call<AppResult> call = service.start(presentProc.getId(), Constants.MIX);
							call.enqueue(new Callback<AppResult>() {
								@Override
								public void onResponse(Call<AppResult> call, Response<AppResult> response) {
									if (response.isSuccessful()) {
										AppResult result = response.body();
										if (result.isSuccess()) {
											Toast.makeText(ContextHolder.getContext(), "开始调配成功", Toast.LENGTH_LONG).show();
											MixActivity.this.finish();
										} else {
											Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
										}
									} else {
										Toast.makeText(ContextHolder.getContext(), "开始调配失败，请重试", Toast.LENGTH_LONG).show();
									}
									pd.dismiss();
								}

								@Override
								public void onFailure(Call<AppResult> call, Throwable t) {
									Toast.makeText(ContextHolder.getContext(), "开始调配失败，请重试", Toast.LENGTH_LONG).show();
									pd.dismiss();
								}
							});
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }
	
    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MixActivity.this).setMessage("确认完成调配？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(MixActivity.this, "", "处理中...", true);

                            ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                            Call<AppResult> call = service.mix(prescription.getId(), presentProc.getId());
                            call.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "完成调配成功", Toast.LENGTH_LONG).show();
                                            MixActivity.this.finish();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "完成调配失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "完成调配失败，请重试", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MixActivity.this).setTitle("退回审方原因");
            final EditText input = new EditText(MixActivity.this);
            builder.setView(input);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(MixActivity.this, "", "处理中...", true);
                    if (input.getText().toString().equals("")) {
                        input.setText("未知原因");
                    }

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.mixCancel(prescription.getId(), presentProc.getId(), input.getText().toString());
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回审方成功", Toast.LENGTH_LONG).show();
                                    MixActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回审方失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回审方失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
