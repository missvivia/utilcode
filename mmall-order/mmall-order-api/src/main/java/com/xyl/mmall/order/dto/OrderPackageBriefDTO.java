package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderPackage;

/**
 * 订单包裹(简洁版)
 * 
 * @author dingmingliang
 * 
 */
public class OrderPackageBriefDTO extends OrderPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderPackageBriefDTO(OrderPackage obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderPackageBriefDTO() {
	}
}
