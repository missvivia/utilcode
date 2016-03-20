/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;

import com.xyl.mmall.promotion.meta.UserCoupon;

/**
 * UserCouponDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public class UserCouponDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserCoupon userCoupon;
	
	private CouponDTO couponDTO;
	
	public UserCouponDTO(){}
	
	public UserCouponDTO(UserCoupon userCoupon){
		this.userCoupon = userCoupon;
	}

	public UserCoupon getUserCoupon() {
		return userCoupon;
	}

	public void setUserCoupon(UserCoupon userCoupon) {
		this.userCoupon = userCoupon;
	}

	public CouponDTO getCouponDTO() {
		return couponDTO;
	}

	public void setCouponDTO(CouponDTO couponDTO) {
		this.couponDTO = couponDTO;
	}
}
