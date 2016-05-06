package com.chdp.chdpapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class PourActivity extends WithProcessActivity {
	private Button btnPour;
    private Button btnPourCancel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour);
		setTitle("调配处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatus(this);

        btnPour = (Button) findViewById(R.id.btn_pour);
        btnPourCancel = (Button) findViewById(R.id.btn_pour_back);

        btnPour.setOnClickListener(new ForwardClickListener());
        btnPourCancel.setOnClickListener(new BackwardClickListener());
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    final ProgressDialog pd = ProgressDialog.show(DecoctActivity.this, "", "处理中...", true);

                    MachineService service = ServiceGenerator.create(MachineService.class, user.getSession_id());
                    Call<Machine> call = service.getMachineByUuidAndType(data.getStringExtra(Intents.Scan.RESULT), Constants.FILLING_MACHINE);
                    call.enqueue(new Callback<Machine>() {
                        @Override
                        public void onResponse(Call<Machine> call, Response<Machine> response) {
                            if (response.isSuccessful()) {
                                Machine machine = response.body();

                                ProcessService service2 = ServiceGenerator.create(ProcessService.class, user.getSession_id());
								Call<AppResult> call2 = service2.pour(prescription.getId(), presentProc.getId());
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
            new AlertDialog.Builder(PourActivity.this).setMessage("确认完成灌装？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intents.Scan.ACTION);
                            intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "请扫描灌装机标签");
                            intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
                            intent.setClass(DecoctActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, 200);
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
