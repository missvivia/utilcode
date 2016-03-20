package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderPackageRefund;

/**
 * 包裹退款总计(不包含订单取消和退货导致的退款)
 * 
 * @author dingmingliang
 * 
 */
public class OrderPackageRefundDTO extends OrderPackageRefund {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * @param obj
	 * @return
	 */
	public static OrderPackageRefundDTO genInstance(OrderPackageRefund obj) {
		OrderPackageRefundDTO dto = new OrderPackageRefundDTO();
		ReflectUtil.convertObj(dto, obj, false);
		return dto;
	}

	/**
	 * 构造函数
	 */
	public OrderPackageRefundDTO() {
	}
}
