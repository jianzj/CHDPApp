package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.Herb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HerbService {
    @POST("app/herb/getHerbByType")
    @FormUrlEncoded
    Call<List<Herb>> getHerbByType(@Field("type") int type);

    @GET("app/herb/getHerbs")
    Call<List<Herb>> getHerbs();
}
