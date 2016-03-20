package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileGiftMoneyDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1227905791337893542L;
	//红包使用日期
	private long usedTime;
	//红包使用金额
	private double usedMoney;
	public long getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
	public double getUsedMoney() {
		return usedMoney;
	}
	public void setUsedMoney(double usedMoney) {
		this.usedMoney = usedMoney;
	}
	

	
	
}
