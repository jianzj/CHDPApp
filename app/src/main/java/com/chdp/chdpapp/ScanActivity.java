package com.chdp.chdpapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.chdp.chdpapp.bean.Order;
import com.chdp.chdpapp.bean.Prescription;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.bean.UserAuthority;
import com.chdp.chdpapp.service.OrderService;
import com.chdp.chdpapp.service.PrescriptionService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.AuthHelper;
import com.chdp.chdpapp.util.Constants;
import com.chdp.chdpapp.util.ContextHolder;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends ActionBarActivity {
    private UserAuthority auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = getIntent();
        auth = (UserAuthority) intent.getSerializableExtra("auth");
        user = AuthHelper.checkUser(this);
        setTitle("处方扫描");

        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, "请扫描处方标签");
        intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
        intent.setClass(ScanActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != data && requestCode == 200) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (auth.ordinal() != Constants.SHIP)
                        checkPrescription(data.getStringExtra(Intents.Scan.RESULT));
                    else
                        checkOrder(data.getStringExtra(Intents.Scan.RESULT));
                    break;
                default:
                    Toast.makeText(this, "条码扫描失败，请重试", Toast.LENGTH_LONG).show();
                    this.finish();
            }
        } else {
            this.finish();
        }
    }

    private void checkPrescription(String uuid) {
        PrescriptionService service = ServiceGenerator.create(PrescriptionService.class, user.getSession_id());
        Call<Prescription> call = null;
		if(auth.ordinal() == Constants.CLEAN)
			call = service.getPrescriptionByCleanMachineUuid(uuid);
		else
			call = service.getPrescription(uuid);
        call.enqueue(new Callback<Prescription>() {
            @Override
            public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                if (response.isSuccessful()) {
                    Prescription prs = response.body();
                    if (prs == null) {
                        Toast.makeText(ContextHolder.getContext(), "请求的处方不存在，请重试", Toast.LENGTH_LONG).show();
                        ScanActivity.this.finish();
                    } else {
                        int process = prs.getProcess();
                        if (auth.ordinal() == process) {
                            Intent intent = new Intent();
                            intent.putExtra("prescription", prs);
                            switch (process) {
                                case Constants.CHECK:
                                    intent.setClass(ScanActivity.this, CheckActivity.class);
                                    break;
                                case Constants.MIX:
                                    intent.setClass(ScanActivity.this, MixActivity.class);
                                    break;
                                case Constants.MIXCHECK:
                                    intent.setClass(ScanActivity.this, MixCheckActivity.class);
                                    break;
                                case Constants.SOAK:
                                    intent.setClass(ScanActivity.this, SoakActivity.class);
                                    break;
                                case Constants.DECOCT:
                                    intent.setClass(ScanActivity.this, DecoctActivity.class);
                                    break;
                                case Constants.POUR:
                                    intent.setClass(ScanActivity.this, PourActivity.class);
                                    break;
                                case Constants.CLEAN:
                                    intent.setClass(ScanActivity.this, CleanActivity.class);
                                    break;
                                case Constants.PACKAGE:
                                    intent.setClass(ScanActivity.this, PackageActivity.class);
                                    break;
                            }
                            ScanActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(ContextHolder.getContext(), "处方当前处于“" + Constants.getProcessName(process) +
                                    "”阶段，无法进行“" + Constants.getProcessName(auth.ordinal()) + "”操作", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(ContextHolder.getContext(), "请求处方信息失败，请重试", Toast.LENGTH_LONG).show();
                }
                ScanActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Prescription> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "请求处方信息失败，请重试", Toast.LENGTH_LONG).show();
                ScanActivity.this.finish();
            }
        });
    }

    private void checkOrder(String uuid) {
        OrderService service = ServiceGenerator.create(OrderService.class, user.getSession_id());
        Call<Order> call = service.getOrder(uuid);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    Order order = response.body();
                    if (order == null) {
                        Toast.makeText(ContextHolder.getContext(), "请求的出库单不存在，请重试", Toast.LENGTH_LONG).show();
                        ScanActivity.this.finish();
                    } else {
                        if (order.getStatus() == Constants.ORDER_BEGIN) {
                            Intent intent = new Intent();
                            intent.putExtra("order", order);
                            intent.setClass(ScanActivity.this, ShipActivity.class);
                            ScanActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(ContextHolder.getContext(), "出库单已完成出库，请勿重复操作", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(ContextHolder.getContext(), "请求出库单信息失败，请重试", Toast.LENGTH_LONG).show();
                }
                ScanActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "请求出库单信息失败，请重试", Toast.LENGTH_LONG).show();
                ScanActivity.this.finish();
            }
        });
    }
}
