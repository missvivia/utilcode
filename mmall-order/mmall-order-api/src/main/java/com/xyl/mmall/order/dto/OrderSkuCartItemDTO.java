package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderSkuCartItem;

/**
 * 订单明细-普通商品Sku
 * 
 * @author dingmingliang
 * 
 */
public class OrderSkuCartItemDTO extends OrderSkuCartItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * @param obj
	 */
	public static OrderSkuCartItemDTO genInstance(OrderSkuCartItem obj) {
		if (obj == null)
			return null;
		OrderSkuCartItemDTO dto = new OrderSkuCartItemDTO();
		ReflectUtil.convertObj(dto, obj, false);
		return dto;
	}

	/**
	 * 构造函数
	 */
	public OrderSkuCartItemDTO() {
	}
}
