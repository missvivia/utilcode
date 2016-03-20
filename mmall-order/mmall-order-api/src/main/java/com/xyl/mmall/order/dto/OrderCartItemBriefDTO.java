package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderCartItem;

/**
 * 订单明细(简洁版)
 * 
 * @author dingmingliang
 * 
 */
public class OrderCartItemBriefDTO extends OrderCartItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderCartItemBriefDTO(OrderCartItem obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderCartItemBriefDTO() {
	}
}
