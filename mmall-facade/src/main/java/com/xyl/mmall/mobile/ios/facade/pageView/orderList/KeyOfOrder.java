package com.xyl.mmall.mobile.ios.facade.pageView.orderList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xyl.mmall.framework.enums.SpSource;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;

public class KeyOfOrder implements Serializable {

	private static final long serialVersionUID = 6336022061333879778L;

	@JsonIgnore
	private long orderId;
	@JsonIgnore
	private long parentId;
	
	private BigDecimal totalPrice = BigDecimal.ZERO;
	private SpSource spSource = SpSource.MMALL;
	
	private boolean isCombined;
	
	public boolean isCombined() {
		return isCombined;
	}

	public void setCombined(boolean isCombined) {
		this.isCombined = isCombined;
	}

	public SpSource getSpSource() {
		return spSource;
	}

	public void setSpSource(SpSource spSource) {
		this.spSource = spSource;
	}

	/**
	 * 订单状态
	 */
	private OrderFormState orderFormState;

	private long payCloseCD = 0;

	public OrderFormState getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(OrderFormState orderFormState) {
		this.orderFormState = orderFormState;
	}

	public long getPayCloseCD() {
		return payCloseCD;
	}

	public void setPayCloseCD(long orderTime) {
		setPayCloseCDAndOrderForm(orderTime);
	}

	private List<IosOrderForm> subOrder;

	private boolean isOnpay;

	public boolean isOnpay() {
		return isOnpay;
	}

	public void setOnpay(OrderFormPayMethod orderFormPayMethod) {
		this.isOnpay = OrderFormPayMethod.isOnlinePayMethod(orderFormPayMethod);
	}

	public List<IosOrderForm> getSubOrder() {
		return subOrder;
	}

	public void setSubOrder(List<IosOrderForm> subOrder) {
		if (subOrder != null && !subOrder.isEmpty()) {
			BigDecimal bigDecimal = BigDecimal.ZERO;
			for (IosOrderForm iosOrderForm : subOrder) {
				iosOrderForm.getCartRPrice();
				bigDecimal = bigDecimal.add(iosOrderForm.getCartRPrice());
			}
			this.totalPrice = bigDecimal;
		}
		this.subOrder = subOrder;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime * result + (int) (parentId ^ (parentId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyOfOrder other = (KeyOfOrder) obj;
		if (orderId != other.orderId)
			return false;
		if (parentId != other.parentId)
			return false;
		return true;
	}

	public void setPayCloseCDAndOrderForm(long orderTime) {
		long currTime = System.currentTimeMillis();
		if (orderTime + ConstValueOfOrder.MAX_PAY_TIME > currTime
				&& this.getOrderFormState() == OrderFormState.WAITING_PAY) {
			payCloseCD = orderTime + ConstValueOfOrder.MAX_PAY_TIME - currTime;
			payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
		}
		// 6.设置订单状态
		if (payCloseCD <= 0 && this.orderFormState == OrderFormState.WAITING_PAY) {
			this.orderFormState = OrderFormState.CANCEL_ED;
		}
	}

}
