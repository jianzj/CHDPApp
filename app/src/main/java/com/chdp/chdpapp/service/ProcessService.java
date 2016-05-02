package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.Process;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProcessService {
    @POST("app/process/getPresentPreviousProcess")
    @FormUrlEncoded
    Call<List<Process>> getPresentPreviousProcess(@Field("id") int id);
}
