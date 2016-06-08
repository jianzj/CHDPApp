package com.chdp.chdpapp.util;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chdp.chdpapp.R;
import com.chdp.chdpapp.WithProcessActivity;
import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.Process;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessHelper {
    public static void setProcessStatus(final WithProcessActivity activity) {
        final ProgressDialog pd = ProgressDialog.show(activity, "", "处理中...", true);
        final TextView txtProcName = (TextView) activity.findViewById(R.id.txt_proc_name);
        final TextView txtProcUser = (TextView) activity.findViewById(R.id.txt_proc_user);
        final TextView txtProcStatus = (TextView) activity.findViewById(R.id.txt_proc_status);
        final TextView labelProcError = (TextView) activity.findViewById(R.id.label_proc_error);
        final TextView txtProcError = (TextView) activity.findViewById(R.id.txt_proc_error);
        ProcessService service = ServiceGenerator.create(ProcessService.class, activity.user.getSession_id());
        Call<List<Process>> call = service.getPresentPreviousProcess(activity.prescription.getId());
        call.enqueue(new Callback<List<Process>>() {
            @Override
            public void onResponse(Call<List<Process>> call, Response<List<Process>> response) {
                if (response.isSuccessful()) {
                    List<Process> procs = response.body();
                    activity.presentProc = procs.get(0);
                    activity.previousProc = procs.get(1);

                    txtProcName.setText(Constants.getProcessName(activity.previousProc.getProcess_type()));
                    txtProcUser.setText(activity.previousProc.getUser_name());
                    if (activity.previousProc.getError_type() == 0) {
                        txtProcStatus.setText("正常");
                        txtProcStatus.setTextColor(Color.GREEN);
                    } else if (activity.previousProc.getError_type() == 1) {
                        txtProcStatus.setText("手动回退");
                        txtProcStatus.setTextColor(Color.RED);
                        labelProcError.setVisibility(View.VISIBLE);
                        txtProcError.setVisibility(View.VISIBLE);
                        txtProcError.setText(activity.previousProc.getError_msg());
                    } else if (activity.previousProc.getError_type() == 2) {
                        txtProcStatus.setText("错误回退");
                        txtProcStatus.setTextColor(Color.RED);
                        labelProcError.setVisibility(View.VISIBLE);
                        txtProcError.setVisibility(View.VISIBLE);
                        txtProcError.setText(activity.previousProc.getError_msg());
                    }

                    if (activity.presentProc.getProcess_type() == Constants.DECOCT) {
                        LinearLayout layoutDecoctTime = (LinearLayout) activity.findViewById(R.id.layout_decoct_time);
                        LinearLayout layoutMiddleTime = (LinearLayout) activity.findViewById(R.id.layout_middle_time);
                        LinearLayout layoutDecoctMachine = (LinearLayout) activity.findViewById(R.id.layout_decoct_machine);
                        TextView txtDecoctTime = (TextView) activity.findViewById(R.id.txt_decoct_time);
                        TextView txtMiddleTime = (TextView) activity.findViewById(R.id.txt_middle_time);
                        TextView txtDecoctMachine = (TextView) activity.findViewById(R.id.txt_decoct_machine);
                        Button btnDecoctStart = (Button) activity.findViewById(R.id.btn_decoct_start);
                        Button btnMiddleStart = (Button) activity.findViewById(R.id.btn_decoct_middle);
                        Button btnDecoctFinish = (Button) activity.findViewById(R.id.btn_decoct_finish);
                        Button btnDecoctCancel = (Button) activity.findViewById(R.id.btn_decoct_cancel);

                        if (activity.presentProc.getBegin() != null && activity.presentProc.getMiddle() != null) {
                            btnDecoctStart.setVisibility(View.GONE);
                            btnMiddleStart.setVisibility(View.GONE);
                            btnDecoctCancel.setVisibility(View.GONE);
                            txtDecoctTime.setText(activity.presentProc.getBegin());
                            txtMiddleTime.setText(activity.presentProc.getMiddle());
                            txtDecoctMachine.setText(activity.presentProc.getMachine_name());
                        } else if (activity.presentProc.getBegin() != null && activity.presentProc.getMiddle() == null) {
                            btnDecoctStart.setVisibility(View.GONE);
                            btnDecoctFinish.setVisibility(View.GONE);
                            layoutMiddleTime.setVisibility(View.GONE);
                            btnDecoctCancel.setVisibility(View.GONE);
                            txtDecoctTime.setText(activity.presentProc.getBegin());
                            txtDecoctMachine.setText(activity.presentProc.getMachine_name());
                        } else {
                            btnMiddleStart.setVisibility(View.GONE);
                            btnDecoctFinish.setVisibility(View.GONE);
                            layoutDecoctTime.setVisibility(View.GONE);
                            layoutMiddleTime.setVisibility(View.GONE);
                            layoutDecoctMachine.setVisibility(View.GONE);
                        }

                        if (activity.presentProc.getBegin() == null) {
                            ProcessService service2 = ServiceGenerator.create(ProcessService.class, activity.user.getSession_id());
                            Call<AppResult> call2 = service2.checkAndFinish(activity.previousProc.getId());
                            call2.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "更新浸泡结束时间成功", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "", Toast.LENGTH_LONG).show();
                                            activity.finish();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "更新浸泡结束时间失败，请重试", Toast.LENGTH_LONG).show();
                                        activity.finish();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "更新浸泡结束时间失败，请重试", Toast.LENGTH_LONG).show();
                                    activity.finish();
                                    pd.dismiss();
                                }
                            });
                        } else {
                            pd.dismiss();
                        }
                    } else if (activity.presentProc.getProcess_type() == Constants.MIX) {
                        Button btnMixStart = (Button) activity.findViewById(R.id.btn_mix_start);
                        Button btnMixFinish = (Button) activity.findViewById(R.id.btn_mix_finish);
                        TextView txtMixTime = (TextView) activity.findViewById(R.id.txt_mix_time);

                        if (activity.presentProc.getBegin() != null) {
                            btnMixStart.setVisibility(View.GONE);
                            txtMixTime.setText(activity.presentProc.getBegin());
                        } else {
                            btnMixFinish.setVisibility(View.GONE);
                            txtMixTime.setVisibility(View.GONE);
                        }
                        pd.dismiss();
                    } else if (activity.presentProc.getProcess_type() == Constants.CLEAN) {
                        TextView txtCleanTime = (TextView) activity.findViewById(R.id.txt_clean_time);
                        txtCleanTime.setText(activity.presentProc.getBegin());
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                    }
                } else {
                    Toast.makeText(ContextHolder.getContext(), "获取流程信息失败", Toast.LENGTH_LONG).show();
                    activity.finish();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Process>> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "获取流程信息失败，请后退重试", Toast.LENGTH_LONG).show();
                activity.finish();
                pd.dismiss();
            }
        });
    }
}
