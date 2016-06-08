package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.AppResult;
import com.chdp.chdpapp.bean.Process;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProcessService {
    @POST("app/process/getPresentPreviousProcess")
    @FormUrlEncoded
    Call<List<Process>> getPresentPreviousProcess(@Field("id") int id);

    @POST("app/process/check")
    @FormUrlEncoded
    Call<AppResult> check(@FieldMap Map<String, String> map);

    @POST("app/process/checkCancel")
    @FormUrlEncoded
    Call<AppResult> checkCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/mix")
    @FormUrlEncoded
    Call<AppResult> mix(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/mixCancel")
    @FormUrlEncoded
    Call<AppResult> mixCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/start")
    @FormUrlEncoded
    Call<AppResult> start(@Field("procId") int procId, @Field("proc") int proc);

    @POST("app/process/startWithMachine")
    @FormUrlEncoded
    Call<AppResult> startWithMachine(@Field("procId") int procId, @Field("proc") int proc, @Field("machineId") int machineId);

    @POST("app/process/checkAndFinish")
    @FormUrlEncoded
    Call<AppResult> checkAndFinish(@Field("procId") int procId);

    @POST("app/process/mixcheck")
    @FormUrlEncoded
    Call<AppResult> mixcheck(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/mixcheckCancel")
    @FormUrlEncoded
    Call<AppResult> mixcheckCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/soak")
    @FormUrlEncoded
    Call<AppResult> soak(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/soakCancel")
    @FormUrlEncoded
    Call<AppResult> soakCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/decoct")
    @FormUrlEncoded
    Call<AppResult> decoct(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/middle")
    @FormUrlEncoded
    Call<AppResult> middle(@Field("procId") int procId);

    @POST("app/process/decoctCancel")
    @FormUrlEncoded
    Call<AppResult> decoctCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/pour")
    @FormUrlEncoded
    Call<AppResult> pour(@Field("prsId") int prsId, @Field("procId") int procId, @Field("machineId") int machineId);

    @POST("app/process/pourCancel")
    @FormUrlEncoded
    Call<AppResult> pourCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/clean")
    @FormUrlEncoded
    Call<AppResult> clean(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/cleanCancel")
    @FormUrlEncoded
    Call<AppResult> cleanCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

    @POST("app/process/pack")
    @FormUrlEncoded
    Call<AppResult> pack(@Field("prsId") int prsId, @Field("procId") int procId);

    @POST("app/process/packCancel")
    @FormUrlEncoded
    Call<AppResult> packCancel(@Field("prsId") int prsId, @Field("procId") int procId, @Field("reason") String reason);

}
