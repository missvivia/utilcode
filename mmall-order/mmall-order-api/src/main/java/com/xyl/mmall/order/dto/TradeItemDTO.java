package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * @author dingmingliang
 * 
 */
public class TradeItemDTO extends TradeItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public TradeItemDTO(TradeItem obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public TradeItemDTO() {
	}
}