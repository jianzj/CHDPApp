package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.Prescription;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PrescriptionService {
    @POST("app/prescription/getPrescription")
    @FormUrlEncoded
    Call<Prescription> getPrescription(@Field("uuid") String uuid);
	
	@POST("app/prescription/getPrescriptionByCleanMachineUuid")
    @FormUrlEncoded
    Call<Prescription> getPrescriptionByCleanMachineUuid(@Field("uuid") String uuid);
}
