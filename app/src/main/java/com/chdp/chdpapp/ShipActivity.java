package com.chdp.chdpapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

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
    }

    private class ForwardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
			new AlertDialog.Builder(PourActivity.this).setMessage("确认完成出库？")
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
