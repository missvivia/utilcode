package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.util.Collection;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * @author dingmingliang
 * 
 */
public class SetPackageToOutTimeParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20141201L;

	private long orderId;

	private long userId;

	/**
	 * 相关的SkuId列表
	 */
	private Collection<Long> skuIdColl;

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

	public Collection<Long> getSkuIdColl() {
		return skuIdColl;
	}

	public void setSkuIdColl(Collection<Long> skuIdColl) {
		this.skuIdColl = skuIdColl;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
