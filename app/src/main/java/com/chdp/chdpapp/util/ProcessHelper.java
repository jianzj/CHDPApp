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
                } else {
                    Toast.makeText(ContextHolder.getContext(), "获取流程信息失败", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<Process>> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "获取流程信息失败，请后退重试", Toast.LENGTH_LONG).show();
                activity.finish();
                pd.dismiss();
            }
        });
    }

    public static void setProcessStatusWithCheck(final WithProcessActivity activity) {
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

                    LinearLayout layoutDecoctTime = (LinearLayout) activity.findViewById(R.id.layout_decoct_time);
					LinearLayout layoutDecoctMachine = (LinearLayout) activity.findViewById(R.id.layout_decoct_machine);
                    TextView txtDecoctTime = (TextView) activity.findViewById(R.id.txt_decoct_time);
					TextView txtDecoctMachine = (TextView) activity.findViewById(R.id.txt_decoct_machine);
                    Button btnDecoctStart = (Button) activity.findViewById(R.id.btn_decoct_start);
                    Button btnDecoctFinish = (Button) activity.findViewById(R.id.btn_decoct_finish);
                    if (activity.presentProc.getBegin() != null) {
                        btnDecoctStart.setVisibility(View.GONE);
                        txtDecoctTime.setText(activity.presentProc.getBegin());
						txtDecoctMachine.setText(activity.presentProc.getMachine_name());
                    } else {
                        btnDecoctFinish.setVisibility(View.GONE);
                        layoutDecoctTime.setVisibility(View.GONE);
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
	
	public static void setProcessStatusWithTime(final WithProcessActivity activity) {
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
					
                    TextView txtCleanTime = (TextView) activity.findViewById(R.id.txt_clean_time);
                    txtCleanTime.setText(activity.presentProc.getBegin());
                } else {
                    Toast.makeText(ContextHolder.getContext(), "获取流程信息失败", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
                pd.dismiss();
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
