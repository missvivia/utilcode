package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一級類
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileOrderBuildVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7803477082604287969L;
	//校验hash
	private String hash;
	//截止时间
	private long endTime;
	//剩余时间
	private long countDownTime;
	//网易宝余额
	private double balance;
	//可用红包余额
	private double giftMoney;
	//运费
	private double carriageFee	;
	//支持的付款方式,多选，未来有多，就增加 1：货到付款，0：网易宝
	private List<Integer> payChannel;
	//优惠券 key是 id，Value是显示
	private List<MobileKeyPairVO> coupon;
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getCountDownTime() {
		return countDownTime;
	}
	public void setCountDownTime(long countDownTime) {
		this.countDownTime = countDownTime;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getGiftMoney() {
		return giftMoney;
	}
	public void setGiftMoney(double giftMoney) {
		this.giftMoney = giftMoney;
	}
	public double getCarriageFee() {
		return carriageFee;
	}
	public void setCarriageFee(double carriageFee) {
		this.carriageFee = carriageFee;
	}
	public List<Integer> getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(List<Integer> payChannel) {
		this.payChannel = payChannel;
	}
	public List<MobileKeyPairVO> getCoupon() {
		return coupon;
	}
	public void setCoupon(List<MobileKeyPairVO> coupon) {
		this.coupon = coupon;
	}
	
	
	
}
