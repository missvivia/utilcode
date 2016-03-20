package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单明细-普通商品Sku
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderSkuCartItem", desc = "订单明细-普通商品Sku")
public class OrderSkuCartItem implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(primary = true, desc = "订单明细Id")
	private long orderCartItemId;

	@AnnonOfField(desc = "UserId", policy = true)
	private long userId;

	@AnnonOfField(desc = "SkuId")
	private long skuId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderCartItemId() {
		return orderCartItemId;
	}

	public void setOrderCartItemId(long orderCartItemId) {
		this.orderCartItemId = orderCartItemId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

}
