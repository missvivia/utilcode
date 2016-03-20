package com.xyl.mmall.mobile.ios.facade.pageView.couponList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CouponVO {

	private long id;
	private String couponCode;
	private long startTime;
	private long endTime;
	private int state;
	private String condition;
	private String facValue;
	private int favorType;
	@JsonIgnore
	private String startDate;
	@JsonIgnore
	private String endDate;
	
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getFavorType() {
		return favorType;
	}
	public void setFavorType(int favorType) {
		this.favorType = favorType;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getFacValue() {
		return facValue;
	}
	public void setFacValue(String facValue) {
		this.facValue = facValue;
	}
	
	
}
