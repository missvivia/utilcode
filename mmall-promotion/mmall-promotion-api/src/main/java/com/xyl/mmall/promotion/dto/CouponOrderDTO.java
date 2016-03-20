/*
 * @(#) 2014-10-12
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CouponOrderDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-12
 * @since      1.0
 */
public class CouponOrderDTO extends CouponOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CouponDTO couponDTO;
	
	/**
	 * 订单序列号
	 */
	private String orderSerial;
	
	
	public CouponOrderDTO(){};
	
	public CouponOrderDTO(CouponOrder couponOrder) {
		ReflectUtil.convertObj(this, couponOrder, false);
	}

	public CouponDTO getCouponDTO() {
		return couponDTO;
	}

	public void setCouponDTO(CouponDTO couponDTO) {
		this.couponDTO = couponDTO;
	}

	public String getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}
	
}
