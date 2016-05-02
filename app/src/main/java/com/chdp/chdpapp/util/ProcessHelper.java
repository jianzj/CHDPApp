package com.chdp.chdpapp.util;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chdp.chdpapp.R;
import com.chdp.chdpapp.WithProcessActivity;
import com.chdp.chdpapp.service.ProcessService;
import com.chdp.chdpapp.service.ServiceGenerator;

import com.chdp.chdpapp.bean.Process;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessHelper {
    public static void setProcessStatus(final WithProcessActivity activity) {
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
                    Toast.makeText(ContextHolder.getContext(), "获取流程信息失败，请后退重试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Process>> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "获取流程信息失败，请后退重试", Toast.LENGTH_LONG).show();
            }
        });
    }
}
