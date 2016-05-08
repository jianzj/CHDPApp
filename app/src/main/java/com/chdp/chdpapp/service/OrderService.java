package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.Order;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderService {
    @POST("app/order/getOrder")
    @FormUrlEncoded
    Call<Order> getOrder(@Field("uuid") String uuid);

    @POST("app/order/finishOrder")
    @FormUrlEncoded
    Call<AppResult> finishOrder(@Field("orderId") int orderId);

    @POST("app/order/countPrsNumInOrder")
    @FormUrlEncoded
    Call<Integer> countPrsNumInOrder(@Field("orderId") int orderId);
}
