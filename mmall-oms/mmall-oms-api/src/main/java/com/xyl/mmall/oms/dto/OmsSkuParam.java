package com.xyl.mmall.oms.dto;

import java.io.Serializable;

/**
 * 组单使用的Sku信息(包含购买数量)
 * 
 * @author dingmingliang
 * 
 */
public class OmsSkuParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * SkuId
	 */
	private long skuId;

	/**
	 * 购买个数
	 */
	private int count;

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
}
