package com.xyl.mmall.backend.param;

import java.math.BigDecimal;

public class ModifiedOrderCashParam {
	
	/**
	 * 订单Id
	 */
	private long orderId;
	
	/**
	 * 用户Id
	 */
	private long userId;
	
	/**
	 * 金额
	 */
	private BigDecimal cash;

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

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	
}
