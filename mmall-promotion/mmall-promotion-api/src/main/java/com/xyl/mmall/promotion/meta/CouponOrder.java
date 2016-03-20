/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.CouponOrderType;


/**
 * CouponOrder.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
@AnnonOfClass(desc = "优惠券订单", tableName = "Mmall_Promotion_CouponOrder", dbCreateTimeName = "CreateTime")
public class CouponOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键ID", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单ID")
	private long orderId;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "优惠券", type = "VARCHAR(32)")
	private String couponCode;
	
	@AnnonOfField(desc="用户 优惠券Id")
	private long userCouponId;
	
	@AnnonOfField(desc = "订单优惠券类型")
	private CouponOrderType couponOrderType = CouponOrderType.USE_COUPON;

	@AnnonOfField(desc = "处理情况")
	private ActivationHandlerType couponHandlerType = ActivationHandlerType.DEFAULT;

	@AnnonOfField(desc = "返券来源")
	private int returnSource;

	@AnnonOfField(desc = "固定时间")
	private boolean fixDay;
	
	@AnnonOfField(desc = "订单使用优惠券效果索引", defa = "-1", hasDefault = true)
	private int couponIdx;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}

	public CouponOrderType getCouponOrderType() {
		return couponOrderType;
	}

	public void setCouponOrderType(CouponOrderType couponOrderType) {
		this.couponOrderType = couponOrderType;
	}

	public ActivationHandlerType getCouponHandlerType() {
		return couponHandlerType;
	}

	public void setCouponHandlerType(ActivationHandlerType couponHandlerType) {
		this.couponHandlerType = couponHandlerType;
	}

	public int getReturnSource() {
		return returnSource;
	}

	public void setReturnSource(int returnSource) {
		this.returnSource = returnSource;
	}

	public boolean isFixDay() {
		return fixDay;
	}

	public void setFixDay(boolean fixDay) {
		this.fixDay = fixDay;
	}
	
	public int getCouponIdx() {
		return couponIdx;
	}

	public void setCouponIdx(int couponIdx) {
		this.couponIdx = couponIdx;
	}

	public CouponOrder cloneObject() {
		CouponOrder co = new CouponOrder();
		BeanUtils.copyProperties(this, co);
		return co;
	}
}
