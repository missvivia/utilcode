/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderReplenish;

/**
 * OrderReplenishDTO.java created by yydx811 at 2015年6月5日 下午3:41:19
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class OrderReplenishDTO extends OrderReplenish {

	/** 序列化id. */
	private static final long serialVersionUID = -2673936454113542027L;

	public OrderReplenishDTO() {
	}
	
	public OrderReplenishDTO(OrderReplenish obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
