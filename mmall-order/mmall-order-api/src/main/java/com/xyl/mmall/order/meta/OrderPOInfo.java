package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单和PO的关系
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderPOInfo", desc = "订单和PO的关系")
public class OrderPOInfo implements Serializable {

	private static final long serialVersionUID = 20140101L;

	@AnnonOfField(primary = true, desc = "id", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单Id")
	private long orderId;

	@AnnonOfField(desc = "poId", policy = true)
	private long poId;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "下单时间")
	private long orderTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
}
