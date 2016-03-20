package com.xyl.mmall.order.param;

import java.io.Serializable;

/**
 * 退货使用的OrderSku信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月17日 下午12:37:08
 *
 */
public class ReturnOrderSkuParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4300445217972963169L;

	/**
	 * orderSkuId
	 */
	private long orderSkuId;

	/**
	 * skuId
	 */
	private long skuId;
	
	/**
	 * 退货个数
	 */
	private int retCount;
	
	/**
	 * 退货原因
	 */
	private String reason;

	public long getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(long orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getRetCount() {
		return retCount;
	}

	public void setRetCount(int count) {
		this.retCount = count;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
