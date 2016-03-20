package com.xyl.mmall.order.param;

import java.io.Serializable;

import com.xyl.mmall.order.meta.OrderForm;

/**
 * @author dingmingliang
 * 
 */
public class OrderSkuQueryParam1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20141201L;

	private long orderId;

	private long userId;

	/**
	 * 根据OrderForm,生成OrderSkuQueryParam1
	 * 
	 * @param order
	 * @return
	 */
	public static OrderSkuQueryParam1 genInstance(OrderForm order) {
		if (order == null)
			return null;
		OrderSkuQueryParam1 obj = new OrderSkuQueryParam1();
		obj.setOrderId(order.getOrderId());
		obj.setUserId(order.getUserId());
		return obj;
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
}
