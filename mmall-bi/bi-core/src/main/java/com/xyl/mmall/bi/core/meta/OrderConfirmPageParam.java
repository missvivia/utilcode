package com.xyl.mmall.bi.core.meta;

import java.io.Serializable;

/**
 * 订单填写页参数
 * 
 * @author dingmingliang
 * 
 */
public class OrderConfirmPageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	private boolean hasCa;

	private String cartIds;

	public boolean isHasCa() {
		return hasCa;
	}

	public void setHasCa(boolean hasCa) {
		this.hasCa = hasCa;
	}

	public String getCartIds() {
		return cartIds;
	}

	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}
}
