package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.AuthResult;
import com.chdp.chdpapp.bean.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("app/user/login")
    @FormUrlEncoded
    Call<AuthResult> login(@Field("usercode") String usercode, @Field("password") String password);

    @GET("app/user/getUser")
    Call<User> getUser();

    @GET("app/user/logout")
    Call<Void> logout();
}
