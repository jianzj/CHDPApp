package com.chdp.chdpapp.bean;

import java.io.Serializable;

public class Prescription implements Serializable {
	private int id;
	private String uuid;
	private String outer_id;
	private int hospital_id;
	private String hospital_name;
	private String patient_name;
	private int sex;
	private int packet_num;
	private double price;
	private String create_time;
	private int class_of_medicines;
	private int need_decoct_first;
	private String decoct_first_list;
	private int need_decoct_later;
	private String decoct_later_list;
	private int need_wrapped_decoct;
	private String wrapped_decoct_list;
	private int need_take_drenched;
	private String take_drenched_list;
	private int need_melt;
	private String melt_list;
	private int need_decoct_alone;
	private String decoct_alone_list;
	private int process;
	private int process_id;
	private String user_name;
	private String finish_time;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getUuid(){
		return uuid;
	}
	
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	
	public String getOuter_id(){
		return outer_id;
	}
	
	public void setOuter_id(String outer_id){
		this.outer_id = outer_id;
	}
	
	public int getHospital_id(){
		return hospital_id;
	}
	
	public void setHospital_id(int hospital_id){
		this.hospital_id = hospital_id;
	}
	
	public String getHospital_name(){
		return hospital_name;
	}
	
	public void setHospital_name(String hospital_name){
		this.hospital_name = hospital_name;
	}
	
	public String getPatient_name(){
		return patient_name;
	}
	
	public void setPatient_name(String patient_name){
		this.patient_name = patient_name;
	}
	
	public int getSex(){
		return sex;
	}
	
	public void setSex(int sex){
		this.sex = sex;
	}
	
	public int getPacket_num(){
		return packet_num;
	}
	
	public void setPacket_num(int packet_num){
		this.packet_num = packet_num;
	}
	
	public double getPrice(){
		return price;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public String getCreate_time(){
		return create_time;
	}
	
	public void setCreate_time(String create_time){
		this.create_time = create_time;
	}
	
	public int getClass_of_medicines(){
		return class_of_medicines;
	}
	
	public void setClass_of_medicines(int class_of_medicines){
		this.class_of_medicines = class_of_medicines;
	}
	
	public int getNeed_decoct_first(){
		return need_decoct_first;
	}
	
	public void setNeed_decoct_first(int need_decoct_first){
		this.need_decoct_first = need_decoct_first;
	}
	
	public String getDecoct_first_list(){
		return decoct_first_list;
	}
	
	public int getNeed_decoct_later(){
		return need_decoct_later;
	}
	
	public void setNeed_decoct_later(int need_decoct_later){
		this.need_decoct_later = need_decoct_later;
	}
	
	public String getDecoct_later_list(){
		return decoct_later_list;
	}
	
	public void setDecoct_later_list(String decoct_later_list){
		this.decoct_later_list = decoct_later_list;
	}

	public int getNeed_wrapped_decoct(){
		return need_wrapped_decoct;
	}
	
	public void setNeed_wrapped_decoct(int need_wrapped_decoct){
		this.need_wrapped_decoct = need_wrapped_decoct;
	}
	
	public String getWrapped_decoct_list(){
		return wrapped_decoct_list;
	}
	
	public void setWrapped_decoct_list(String wrapped_decoct_list){
		this.wrapped_decoct_list = wrapped_decoct_list;
	}
	
	public int getNeed_take_drenched(){
		return need_take_drenched;
	}
	
	public void setNeed_take_drenched(int need_take_drenched){
		this.need_take_drenched = need_take_drenched;
	}
	
	public String getTake_drenched_list(){
		return take_drenched_list;
	}
	
	public void setTake_drenched_list(String take_drenched_list){
		this.take_drenched_list = take_drenched_list;
	}
	
	public int getNeed_melt(){
		return need_melt;
	}
	
	public void setNeed_melt(int need_melt){
		this.need_melt = need_melt;
	}
	
	public String getMelt_list(){
		return melt_list;
	}
	
	public void setMelt_list(String melt_list){
		this.melt_list = melt_list;
	}
	
	public int getNeed_decoct_alone(){
		return need_decoct_alone;
	}
	
	public void setNeed_decoct_alone(int need_decoct_alone){
		this.need_decoct_alone = need_decoct_alone;
	}
	
	public String getDecoct_alone_list(){
		return decoct_alone_list;
	}
	
	public void setDecoct_alone_list(String decoct_alone_list){
		this.decoct_alone_list = decoct_alone_list;
	}
	
	public int getProcess(){
		return process;
	}
	
	public void setProcess(int process){
		this.process = process;
	}
	
	public int getProcess_id(){
		return process_id;
	}
	
	public void setProcess_id(int process_id){
		this.process_id = process_id;
	}
	
	public String getFinish_time(){
		return finish_time;
	}
	
	public void setFinish_time(String finish_time){
		this.finish_time = finish_time;
	}

	public String getUser_name(){
		return user_name;
	}
	
	public void setUser_name(String userName){
		this.user_name = userName;
	}
}



