/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponDto.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public class CouponDTO extends Coupon {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applyUserName;
	
	private String auditUserName;
	
	private String start;
	
	private String end;
	
	private int couponState;
	
	/**
	 * 优惠用的索引
	 */
	private int index = -1;
	
	/**
	 * 用户优惠券id
	 */
	private long userCouponId;
	
	private List<String> orderSerial = new ArrayList<>();
	
	/**
	 * binder用户的列表	
	 */
	private List<Long> binderUserList = new ArrayList<>();
	

	public CouponDTO(){};
	
	public CouponDTO(Coupon coupon) {
		ReflectUtil.convertObj(this, coupon, false);
	}
	
	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
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

	public List<Long> getBinderUserList() {
		return binderUserList;
	}

	public void setBinderUserList(List<Long> binderUserList) {
		this.binderUserList = binderUserList;
	}

	public int getCouponState() {
		return couponState;
	}

	public void setCouponState(int couponState) {
		this.couponState = couponState;
	}

	public List<String> getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(List<String> orderSerial) {
		this.orderSerial = orderSerial;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}
}
