package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileUnReadMessageVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -487700508495212338L;
	//未读未支付
	private int unPayCount;
	//未读已发货
	private int sendCount;
	//未读未发货
	private int unSendCount;
	
	private int unReadNewGiftMoney;
	
	private int unReadNewCoupon;
	private List<MobileCountDwonVO> countDownList = new ArrayList<MobileCountDwonVO>();

	public List<MobileCountDwonVO> getCountDownList() {
		return countDownList;
	}
	public void setCountDownList(List<MobileCountDwonVO> countDownList) {
		this.countDownList = countDownList;
	}
	public int getUnReadNewCoupon() {
		return unReadNewCoupon;
	}
	public void setUnReadNewCoupon(int unReadNewCoupon) {
		this.unReadNewCoupon = unReadNewCoupon;
	}
	public int getUnReadNewGiftMoney() {
		return unReadNewGiftMoney;
	}
	public void setUnReadNewGiftMoney(int unReadNewGiftMoney) {
		this.unReadNewGiftMoney = unReadNewGiftMoney;
	}
	public int getUnPayCount() {
		return unPayCount;
	}
	public void setUnPayCount(int unPayCount) {
		this.unPayCount = unPayCount;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public int getUnSendCount() {
		return unSendCount;
	}
	public void setUnSendCount(int unSendCount) {
		this.unSendCount = unSendCount;
	}

	
}
