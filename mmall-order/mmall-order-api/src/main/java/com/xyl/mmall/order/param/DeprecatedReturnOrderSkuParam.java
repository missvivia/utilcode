package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退货使用的OrderSku信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月17日 下午12:37:08
 *
 */
@Deprecated
public class DeprecatedReturnOrderSkuParam implements Serializable {

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
	private int count;
	
	/**
	 * 退款总额
	 */
	private BigDecimal totalReturnPrice;
	
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getTotalReturnPrice() {
		return totalReturnPrice;
	}

	public void setTotalReturnPrice(BigDecimal totalReturnPrice) {
		this.totalReturnPrice = totalReturnPrice;
	}
}
