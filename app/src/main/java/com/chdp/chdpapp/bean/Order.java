package com.chdp.chdpapp.bean;

import java.io.Serializable;

public class Order implements Serializable {

    private int id;
    private String uuid;
    private int hospital_id;
    private String create_time;
    private int create_user_id;
    private String outbound_time;
    private int outbound_user_id;
    private int status;

    private String hospital_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getOutbound_time() {
        return outbound_time;
    }

    public void setOutbound_time(String outbound_time) {
        this.outbound_time = outbound_time;
    }

    public int getOutbound_user_id() {
        return outbound_user_id;
    }

    public void setOutbound_user_id(int outbound_user_id) {
        this.outbound_user_id = outbound_user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

}
