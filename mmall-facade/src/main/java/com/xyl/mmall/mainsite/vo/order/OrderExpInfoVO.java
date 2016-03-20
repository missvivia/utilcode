package com.xyl.mmall.mainsite.vo.order;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderExpInfo;

/**
 * 订单快递地址信息
 * 
 * @author dingmingliang
 * 
 */
public class OrderExpInfoVO extends OrderExpInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderExpInfoVO() {
		super();
	}

	public OrderExpInfoVO(OrderExpInfo expInfo) {
		ReflectUtil.convertObj(this, expInfo, false);
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