package com.xyl.mmall.bi.core.meta;

import java.io.Serializable;

/**
 * 修改付款方式
 * 
 * @author dingmingliang
 * 
 */
public class ChangePaymentParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	private long orderId;

	private int changeTo;

	private int changeFrom;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getChangeTo() {
		return changeTo;
	}

	public void setChangeTo(int changeTo) {
		this.changeTo = changeTo;
	}

	public int getChangeFrom() {
		return changeFrom;
	}

	public void setChangeFrom(int changeFrom) {
		this.changeFrom = changeFrom;
	}
}
