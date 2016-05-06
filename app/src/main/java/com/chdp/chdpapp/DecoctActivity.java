package com.chdp.chdpapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.Machine;
import com.chdp.chdpapp.service.MachineService;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.Constants;
import com.chdp.chdpapp.util.ContextHolder;
import com.chdp.chdpapp.util.PrescriptionHelper;
import com.chdp.chdpapp.util.ProcessHelper;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecoctActivity extends WithProcessActivity {
    private Button btnDecoctStart;
    private Button btnDecoctFinish;
    private Button btnDecoctCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoct);

        setTitle("煎煮处理");

        PrescriptionHelper.setPrescriptionBasicInfo(this);
        ProcessHelper.setProcessStatusWithCheck(this);

        btnDecoctStart = (Button) findViewById(R.id.btn_decoct_start);
        btnDecoctFinish = (Button) findViewById(R.id.btn_decoct_finish);
        btnDecoctCancel = (Button) findViewById(R.id.btn_decoct_cancel);

        btnDecoctStart.setOnClickListener(new StartClickListener());
        btnDecoctFinish.setOnClickListener(new ForwardClickListener());
        btnDecoctCancel.setOnClickListener(new BackwardClickListener());

        TextView txtType = (TextView) findViewById(R.id.txt_prs_type);
        TextView txtParam = (TextView) findViewById(R.id.txt_param);
        if (prescription.getClass_of_medicines() == 1) {
            txtType.setText("解表或芳香类药");
            txtParam.setText("温度105℃-110℃，压力<0.1MPa，保温时间20分钟");
        } else if (prescription.getClass_of_medicines() == 2) {
            txtType.setText("一般治疗药");
            txtParam.setText("温度110℃-115℃，压力<0.1MPa，保温时间30分钟");
        } else if (prescription.getClass_of_medicines() == 3) {
            txtType.setText("调理滋补药");
            txtParam.setText("温度115℃-120℃，压力<0.1MPa，保温时间40分钟");
        }

        LinearLayout layoutFirst = (LinearLayout) findViewById(R.id.layout_first);
        TextView txtFirst = (TextView) findViewById(R.id.txt_show_first);
        if (prescription.getNeed_decoct_first() == 0) {
            layoutFirst.setVisibility(View.GONE);
        } else {
            txtFirst.setText(prescription.getDecoct_first_list());
        }

        LinearLayout layoutLater = (LinearLayout) findViewById(R.id.layout_later);
        TextView txtLater = (TextView) findViewById(R.id.txt_show_later);
        if (prescription.getNeed_decoct_later() == 0) {
            layoutLater.setVisibility(View.GONE);
        } else {
            txtLater.setText(prescription.getDecoct_later_list());
        }

        LinearLayout layoutWrap = (LinearLayout) findViewById(R.id.layout_wrap);
        TextView txtWrap = (TextView) findViewById(R.id.txt_show_wrap);
        if (prescription.getNeed_wrapped_decoct() == 0) {
            layoutWrap.setVisibility(View.GONE);
        } else {
            txtWrap.setText(prescription.getWrapped_decoct_list());
        }

        LinearLayout layoutDrink = (LinearLayout) findViewById(R.id.layout_drink);
        TextView txtDrink = (TextView) findViewById(R.id.txt_show_drink);
        if (prescription.getNeed_take_drenched() == 0) {
            layoutDrink.setVisibility(View.GONE);
        } else {
            txtDrink.setText(prescription.getTake_drenched_list());
        }

        LinearLayout layoutMelt = (LinearLayout) findViewById(R.id.layout_melt);
        TextView txtMelt = (TextView) findViewById(R.id.txt_show_melt);
        if (prescription.getNeed_melt() == 0) {
            layoutMelt.setVisibility(View.GONE);
        } else {
            txtMelt.setText(prescription.getMelt_list());
        }

        LinearLayout layoutAlone = (LinearLayout) findViewById(R.id.layout_alone);
        TextView txtAlone = (TextView) findViewById(R.id.txt_show_alone);
        if (prescription.getNeed_decoct_alone() == 0) {
            layoutAlone.setVisibility(View.GONE);
        } else {
            txtAlone.setText(prescription.getDecoct_alone_list());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    final ProgressDialog pd = ProgressDialog.show(DecoctActivity.this, "", "处理中...", true);

                    MachineService service = ServiceGenerator.create(MachineService.class, user.getSession_id());
                    Call<Machine> call = service.getMachineByUuidAndType(data.getStringExtra(Intents.Scan.RESULT), Constants.DECOCTION_MACHINE);
                    call.enqueue(new Callback<Machine>() {
                        @Override
                        public void onResponse(Call<Machine> call, Response<Machine> response) {
                            if (response.isSuccessful()) {
                                Machine machine = response.body();

                                ProcessService service2 = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                                Call<AppResult> call2 = service2.startWithMachine(presentProc.getId(), Constants.DECOCT, machine.getId());
                                call2.enqueue(new Callback<AppResult>() {
                                    @Override
                                    public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                        if (response.isSuccessful()) {
                                            AppResult result = response.body();
                                            if (result.isSuccess()) {
                                                Toast.makeText(ContextHolder.getContext(), "开始煎煮成功", Toast.LENGTH_LONG).show();
                                                DecoctActivity.this.finish();
                                            } else {
                                                Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), "开始煎煮失败，请重试", Toast.LENGTH_LONG).show();
                                        }
                                        pd.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<AppResult> call, Throwable t) {
                                        Toast.makeText(ContextHolder.getContext(), "开始煎煮失败，请重试", Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
                                });

                            } else {
                                Toast.makeText(ContextHolder.getContext(), "获取煎煮机信息失败，请重试", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Machine> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "获取煎煮机信息失败，请重试", Toast.LENGTH_LONG).show();
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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long now = System.currentTimeMillis();
			long begin = df.parse(presentProc.getBegin()).getTime();
            new AlertDialog.Builder(DecoctActivity.this).setMessage("煎煮时长："+Math.ceil((now - begin) / 1000 / 60.0)+"，确认完成？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(DecoctActivity.this, "", "处理中...", true);

                            ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                            Call<AppResult> call = service.decoct(prescription.getId(), presentProc.getId());
                            call.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "完成煎煮成功", Toast.LENGTH_LONG).show();
                                            DecoctActivity.this.finish();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "完成煎煮失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "完成煎煮失败，请重试", Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    private class StartClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(DecoctActivity.this).setMessage("确认开始煎煮？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intents.Scan.ACTION);
                            intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "请扫描煎煮机标签");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(DecoctActivity.this).setTitle("退回浸泡原因");
            final EditText input = new EditText(DecoctActivity.this);
            builder.setView(input);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = ProgressDialog.show(DecoctActivity.this, "", "处理中...", true);
                    if (input.getText().toString().equals("")) {
                        input.setText("未知原因");
                    }

                    ProcessService service = ServiceGenerator.create(ProcessService.class, user.getSession_id());
                    Call<AppResult> call = service.decoctCancel(prescription.getId(), presentProc.getId(), input.getText().toString());
                    call.enqueue(new Callback<AppResult>() {
                        @Override
                        public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                            if (response.isSuccessful()) {
                                AppResult result = response.body();
                                if (result.isSuccess()) {
                                    Toast.makeText(ContextHolder.getContext(), "退回浸泡成功", Toast.LENGTH_LONG).show();
                                    DecoctActivity.this.finish();
                                } else {
                                    Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ContextHolder.getContext(), "退回浸泡失败，请重试", Toast.LENGTH_LONG).show();
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onFailure(Call<AppResult> call, Throwable t) {
                            Toast.makeText(ContextHolder.getContext(), "退回浸泡失败，请重试", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
                }
            }).setNegativeButton("取消", null).show();
        }
    }
}
