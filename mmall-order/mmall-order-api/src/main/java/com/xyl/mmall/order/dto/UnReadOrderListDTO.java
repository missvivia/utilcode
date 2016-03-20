package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.UnReadOrderList;

/**
 * 未读订单服务
 * 
 * @author hzjiangww
 * 
 */
public class UnReadOrderListDTO extends UnReadOrderList {

	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public UnReadOrderListDTO(UnReadOrderList obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public UnReadOrderListDTO() {
	}

	/**
	 * 生成UnReadOrderListDTO对象
	 * 
	 * @param obj
	 * @return
	 */
	public static UnReadOrderListDTO genUnReadOrderListDTO(UnReadOrderList obj) {
		if (obj == null)
			return null;
		return new UnReadOrderListDTO(obj);
	}
}
