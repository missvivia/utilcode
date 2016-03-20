package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderPOInfo;

/**
 * 订单和PO的关系
 * 
 * @author dingmingliang
 * 
 */
public class OrderPOInfoBriefDTO extends OrderPOInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 20141201L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderPOInfoBriefDTO(OrderPOInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public OrderPOInfoBriefDTO() {
	}

	/**
	 * 
	 */
	private OrderFormBriefDTO orderBDTO;

	public OrderFormBriefDTO getOrderBDTO() {
		return orderBDTO;
	}

	public void setOrderBDTO(OrderFormBriefDTO orderBDTO) {
		this.orderBDTO = orderBDTO;
	}
}
