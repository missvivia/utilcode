package com.xyl.mmall.cms.facade.param;

import java.io.Serializable;

import com.xyl.mmall.order.param.OrderExpInfoChangeParam;

public class FrontOrderExpInfoUpdateParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2609110082524312818L;

	private long orderId;
	
	private long userId;
	
	private OrderExpInfoChangeParam chgParam = new OrderExpInfoChangeParam();

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

	public OrderExpInfoChangeParam getChgParam() {
		return chgParam;
	}

	public void setChgParam(OrderExpInfoChangeParam chgParam) {
		this.chgParam = chgParam;
	}

	
}
