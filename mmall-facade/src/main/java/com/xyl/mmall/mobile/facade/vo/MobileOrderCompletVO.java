package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 第一級類
 * @author jiangww
 *
 */
@JsonInclude(Include.NON_NULL)
public class MobileOrderCompletVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3732981682247140145L;
	//订单Id
	private long orderId;
	//网易宝地址
	private String payLink;
	//付成功
	private int paySuccess;
	//获得的优惠券信
	private String couponInfo;
	private long payCloseCD;
	private String giftInfo;
	private MobileShareVO shareGift;
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getPayLink() {
		return payLink;
	}
	public void setPayLink(String payLink) {
		this.payLink = payLink;
	}
	public int getPaySuccess() {
		return paySuccess;
	}
	public void setPaySuccess(int paySuccess) {
		this.paySuccess = paySuccess;
	}
	public String getCouponInfo() {
		return couponInfo;
	}
	public void setCouponInfo(String couponInfo) {
		this.couponInfo = couponInfo;
	}
	public MobileShareVO getShareGift() {
		return shareGift;
	}
	public void setShareGift(MobileShareVO shareGift) {
		this.shareGift = shareGift;
	}
	public String getGiftInfo() {
		return giftInfo;
	}
	public void setGiftInfo(String giftInfo) {
		this.giftInfo = giftInfo;
	}
	public long getPayCloseCD() {
		return payCloseCD;
	}
	public void setPayCloseCD(long payCloseCD) {
		this.payCloseCD = payCloseCD;
	}
	
	
	
	
}
