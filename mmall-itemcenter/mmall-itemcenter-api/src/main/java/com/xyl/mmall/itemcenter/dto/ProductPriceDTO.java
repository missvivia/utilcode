package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.ProductPrice;

/**
 * 商品价格区间表dto
 * @author yydx811
 *
 */
public class ProductPriceDTO extends ProductPrice {

	/** 序列化id. */
	private static final long serialVersionUID = 2947103679548634386L;
	
	public ProductPriceDTO() {
	}

	public ProductPriceDTO(ProductPrice obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
