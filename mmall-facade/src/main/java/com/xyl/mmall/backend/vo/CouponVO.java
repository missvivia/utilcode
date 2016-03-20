package com.xyl.mmall.backend.vo;

import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.promotion.dto.CouponDTO;


public class CouponVO {
	
	/**
	 * 有效期开始时间
	 */
	private String start;
	
	/**
	 * 有效期结束时间
	 */
	private String end;
	
	/**
	 * 优惠券code"
	 */
	private String couponCode;
	
	/**
	 * 优惠券简称
	 */
	private String couponName;
	
	public CouponVO() {};
	
	public CouponVO(CouponDTO couponDTO) {
		this.start = couponDTO.getStartTime()>0?DateFormatEnum.TYPE5.getFormatDate(couponDTO.getStartTime()):"";
		this.end = couponDTO.getEndTime()>0?DateFormatEnum.TYPE5.getFormatDate(couponDTO.getEndTime()):"";
		this.couponCode = couponDTO.getCouponCode();
		this.couponName = couponDTO.getName();
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	
	
	

}
