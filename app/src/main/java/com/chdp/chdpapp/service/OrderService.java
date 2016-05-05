package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.Order;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderService {
    @POST("app/order/getOrder")
    @FormUrlEncoded
    Call<Order> getOrder(@Field("uuid") String uuid);
}
