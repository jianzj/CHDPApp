package com.chdp.chdpapp.service;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ConfigService {
    @POST("app/config/getVersion")
    @FormUrlEncoded
    Call<String> getVersion();
}
