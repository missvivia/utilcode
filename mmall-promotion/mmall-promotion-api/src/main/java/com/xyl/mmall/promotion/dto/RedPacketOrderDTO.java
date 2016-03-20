/*
 * @(#) 2014-10-13
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.RedPacketOrder;

/**
 * RedPacketOrderDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-13
 * @since      1.0
 */
public class RedPacketOrderDTO extends RedPacketOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单序列号
	 */
	private String orderSerial;
	
	
	public RedPacketOrderDTO() {}
	
	public RedPacketOrderDTO(RedPacketOrder redPacketOrder) {
		ReflectUtil.convertObj(this, redPacketOrder, false);
	}

	public String getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}
}
