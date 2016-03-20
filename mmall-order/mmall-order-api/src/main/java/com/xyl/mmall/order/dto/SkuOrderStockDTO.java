package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.SkuOrderStock;

/**
 * 订单服务的Sku销售数据
 * 
 * @author dingmingliang
 * 
 */
public class SkuOrderStockDTO extends SkuOrderStock {

	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public SkuOrderStockDTO(SkuOrderStock obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 构造函数
	 */
	public SkuOrderStockDTO() {
	}

	/**
	 * 生成SkuOrderStockDTO对象
	 * 
	 * @param obj
	 * @return
	 */
	public static SkuOrderStockDTO genSkuOrderStockDTO(SkuOrderStock obj) {
		if (obj == null)
			return null;
		return new SkuOrderStockDTO(obj);
	}
}
