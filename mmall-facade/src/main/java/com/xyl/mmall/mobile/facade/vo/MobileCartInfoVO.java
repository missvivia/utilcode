package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileCartInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5776354317897115842L;
	
	//最新购物车的标示符
	private String cartHash;
	//商品数量(件为单位)
	private int prdtCount;
	//倒计时
	private long countdownTime;
	//购物车失效时间点
	private long endTime;
	//总价
	private double totalPrice;
	
	private int timeOutCount;
	public String getCartHash() {
		return cartHash;
	}
	public void setCartHash(String cartHash) {
		this.cartHash = cartHash;
	}
	public int getPrdtCount() {
		return prdtCount;
	}
	public void setPrdtCount(int prdtCount) {
		this.prdtCount = prdtCount;
	}
	public long getCountdownTime() {
		return countdownTime;
	}
	public void setCountdownTime(long countdownTime) {
		this.countdownTime = countdownTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getTimeOutCount() {
		return timeOutCount;
	}
	public void setTimeOutCount(int timeOutCount) {
		this.timeOutCount = timeOutCount;
	}
	
	
}
