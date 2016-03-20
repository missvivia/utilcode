/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mobile.web.vo;

import java.util.List;
import java.util.Map;

/**
 * CaculateParamVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-24
 * @since      1.0
 */
public class CaculateParamVO {
	
	/**
	 * 活动id
	 */
	private long activityId;
	
	/**
	 * 优惠券id
	 */
	private String couponCode;
	
	/**
	 * cart对应的sku对象，根据po分组
	 */
	private Map<Long, List<CartSkuItemVO>> cartItems;
	
	/**
	 * 站点
	 */
	private int province;


	public long getActivityId() {
		return activityId;
	}


	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public String getCouponCode() {
		return couponCode;
	}


	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}


	public Map<Long, List<CartSkuItemVO>> getCartItems() {
		return cartItems;
	}


	public void setCartItems(Map<Long, List<CartSkuItemVO>> cartItems) {
		this.cartItems = cartItems;
	}


	public int getProvince() {
		return province;
	}


	public void setProvince(int province) {
		this.province = province;
	}
}
