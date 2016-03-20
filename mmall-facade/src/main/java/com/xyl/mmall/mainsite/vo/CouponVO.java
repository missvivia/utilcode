/*
 * @(#) 2014-10-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.vo;

import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-11
 * @since      1.0
 */
public class CouponVO extends CouponDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CouponVO() {}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CouponVO(Coupon obj) {
		super(obj);
	}
}
