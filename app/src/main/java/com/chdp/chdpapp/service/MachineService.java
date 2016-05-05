package com.chdp.chdpapp.service;

import com.chdp.chdpapp.bean.Machine;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MachineService {
    @POST("app/machine/getMachineByUuidAndType")
    @FormUrlEncoded
    Call<Machine> getMachineByUuidAndType(@Field("uuid") String uuid, @Field("type") int type);
}
