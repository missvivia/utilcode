/*
 * @(#) 2014-10-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * TCCParamDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-10
 * @since      1.0
 */
@SuppressWarnings("serial")
public class TCCParamDTO implements Serializable {
	
	/**
	 * 订单id
	 */
	private long orderId;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	
	private List<Long>orderIdList;
	

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

	public List<Long> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<Long> orderIdList) {
		this.orderIdList = orderIdList;
	}
	
	
}
