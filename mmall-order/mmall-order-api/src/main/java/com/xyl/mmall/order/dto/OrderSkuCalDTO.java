package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * OrderCartItem上显示用的基本单位
 * 
 * @author dingmingliang
 * 
 */
public class OrderSkuCalDTO extends OrderSkuDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderSkuCalDTO(OrderSkuDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderSkuCalDTO() {
	}

	/**
	 * 最大可售卖的数量
	 */
	private int maxSaleCount;

	public int getMaxSaleCount() {
		return maxSaleCount;
	}

	public void setMaxSaleCount(int maxSaleCount) {
		this.maxSaleCount = maxSaleCount;
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
