package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("app/user/login")
    @FormUrlEncoded
    Call<AppResult> login(@Field("usercode") String usercode, @Field("password") String password);
	
	@POST("app/user/changePassword")
    @FormUrlEncoded
    Call<AppResult> changePassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @GET("app/user/getUser")
    Call<User> getUser();

    @GET("app/user/logout")
    Call<Void> logout();
}
