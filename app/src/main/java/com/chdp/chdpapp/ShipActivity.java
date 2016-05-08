package com.chdp.chdpapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.Order;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.service.OrderService;
import com.chdp.chdpapp.service.ServiceGenerator;
import com.chdp.chdpapp.util.AuthHelper;
import com.chdp.chdpapp.util.ContextHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipActivity extends ActionBarActivity {
    public User user;
    public Order order;

    private Button btnShip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);
        setTitle("出库处理");

        Intent intent = getIntent();
        user = AuthHelper.checkUser(this);
        order = (Order) intent.getSerializableExtra("order");

        btnShip = (Button) findViewById(R.id.btn_ship);
        btnShip.setOnClickListener(new ForwardClickListener());

        TextView txtOrderHospital = (TextView) findViewById(R.id.txt_order_hospital);
        final TextView txtOrderNum = (TextView) findViewById(R.id.txt_order_num);
        TextView txtOrderUuid = (TextView) findViewById(R.id.txt_order_uuid);
        TextView txtOrderTime = (TextView) findViewById(R.id.txt_order_time);

        txtOrderHospital.setText(order.getHospital_name());
        txtOrderTime.setText(order.getCreate_time());
        txtOrderUuid.setText(order.getUuid());

        OrderService service = ServiceGenerator.create(OrderService.class, user.getSession_id());
        Call<Integer> call = service.countPrsNumInOrder(order.getId());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer result = response.body();
                    txtOrderNum.setText("共" + result + "袋");
                } else {
                    Toast.makeText(ContextHolder.getContext(), "获取出库单处方数量错误，请重试", Toast.LENGTH_LONG).show();
                    ShipActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(ContextHolder.getContext(), "获取出库单处方数量错误，请重试", Toast.LENGTH_LONG).show();
                ShipActivity.this.finish();
            }
        });
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ShipActivity.this).setMessage("确认完成出库？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = ProgressDialog.show(ShipActivity.this, "", "处理中...", true);

                            OrderService service = ServiceGenerator.create(OrderService.class, user.getSession_id());
                            Call<AppResult> call = service.finishOrder(order.getId());
                            call.enqueue(new Callback<AppResult>() {
                                @Override
                                public void onResponse(Call<AppResult> call, Response<AppResult> response) {
                                    if (response.isSuccessful()) {
                                        AppResult result = response.body();
                                        if (result.isSuccess()) {
                                            Toast.makeText(ContextHolder.getContext(), "完成出库成功", Toast.LENGTH_LONG).show();
                                            ShipActivity.this.finish();
                                        } else {
                                            Toast.makeText(ContextHolder.getContext(), result.getErrorMsg() + "请重试", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(ContextHolder.getContext(), "完成出库失败，请重试", Toast.LENGTH_LONG).show();
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onFailure(Call<AppResult> call, Throwable t) {
                                    Toast.makeText(ContextHolder.getContext(), "完成出库失败，请重试", Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }
}
