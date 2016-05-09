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
import com.chdp.chdpapp.bean.Machine;
import com.chdp.chdpapp.service.MachineService;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.ContextHolder;
import com.chdp.chdpapp.util.PrescriptionHelper;
import com.chdp.chdpapp.util.ProcessHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PourActivity extends WithProcessActivity {
	private Button btnPour;
    private Button btnPourCancel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour);
		setTitle("灌装处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        btnPour = (Button) findViewById(R.id.btn_pour);
        btnPourCancel = (Button) findViewById(R.id.btn_pour_back);

        btnPour.setOnClickListener(new ForwardClickListener());
        btnPourCancel.setOnClickListener(new BackwardClickListener());
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(PourActivity.this).setMessage("确认完成灌装？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(PourActivity.this, "", "处理中...", true);

							MachineService service = ServiceGenerator.create(MachineService.class, user.getSession_id());
							Call<Machine> call = service.getMachineById(previousProc.getMachine_id());
							call.enqueue(new Callback<Machine>() {
								@Override
								public void onResponse(Call<Machine> call, Response<Machine> response) {
									if (response.isSuccessful()) {
										Machine machine = response.body();

										ProcessService service2 = ServiceGenerator.create(ProcessService.class, user.getSession_id());
										Call<AppResult> call2 = service2.pour(prescription.getId(), presentProc.getId(), machine.getPour_machine_id());
										call2.enqueue(new Callback<AppResult>() {
											@Override
											public void onResponse(Call<AppResult> call, Response<AppResult> response) {
												if (response.isSuccessful()) {
													AppResult result = response.body();
													if (result.isSuccess()) {
														Toast.makeText(ContextHolder.getContext(), "完成灌装成功", Toast.LENGTH_LONG).show();
														PourActivity.this.finish();
													} else {
														Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
													}
												} else {
													Toast.makeText(ContextHolder.getContext(), "完成灌装失败，请重试", Toast.LENGTH_LONG).show();
												}
												pd.dismiss();
											}

											@Override
											public void onFailure(Call<AppResult> call, Throwable t) {
												Toast.makeText(ContextHolder.getContext(), "完成灌装失败，请重试", Toast.LENGTH_LONG).show();
												pd.dismiss();
											}
										});

									} else {
										Toast.makeText(ContextHolder.getContext(), "获取灌装机信息失败，请重试", Toast.LENGTH_LONG).show();
										pd.dismiss();
									}
								}

								@Override
								public void onFailure(Call<Machine> call, Throwable t) {
									Toast.makeText(ContextHolder.getContext(), "获取灌装机信息失败，请重试", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PourActivity.this).setTitle("退回煎煮原因");
            final EditText input = new EditText(PourActivity.this);
            builder.setView(input);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(PourActivity.this, "", "处理中...", true);
                    if (input.getText().toString().equals("")) {
                        input.setText("未知原因");
                    }

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.pourCancel(prescription.getId(), presentProc.getId(), input.getText().toString());
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回煎煮成功", Toast.LENGTH_LONG).show();
                                    PourActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回煎煮失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回煎煮失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
