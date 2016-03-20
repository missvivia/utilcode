package com.xyl.mmall.order.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单运费退款记录
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(tableName = "Mmall_Order_OrderRefundExp", desc = "订单运费退款记录")
public class OrderRefundExp implements Serializable {

	private static final long serialVersionUID = 20140909L;

	@AnnonOfField(desc = "订单Id", primary = true)
	private long orderId;

	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;

	@AnnonOfField(desc = "创建时间")
	private long ctime;

	@AnnonOfField(desc = "退款的运费金额")
	private BigDecimal expPrice;

	@AnnonOfField(desc = "退款的包裹Id")
	private long packageId;

	/**
	 * 将OrderForm转换成OrderRefundExp对象
	 * 
	 * @param order
	 * @return
	 */
	public static OrderRefundExp genInstance(OrderForm order) {
		long currTime = System.currentTimeMillis();
		OrderRefundExp orderRefundExp = new OrderRefundExp();
		orderRefundExp.setCtime(currTime);
		orderRefundExp.setOrderId(order.getOrderId());
		orderRefundExp.setUserId(order.getUserId());
		orderRefundExp.setExpPrice(order.getExpUserPrice());
		return orderRefundExp;
	}

	public BigDecimal getExpPrice() {
		return expPrice;
	}

	public void setExpPrice(BigDecimal expPrice) {
		this.expPrice = expPrice;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
}
