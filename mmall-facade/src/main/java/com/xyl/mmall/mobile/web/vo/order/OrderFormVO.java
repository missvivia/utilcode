package com.xyl.mmall.mobile.web.vo.order;

import java.math.BigDecimal;

import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.order.dto.OrderFormDTO;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年5月20日下午3:20:58
 */
public class OrderFormVO {
	
	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 收货人
	 */
	private String consignee;
	
	/** 
	 * 订单状态
	 */
	private int orderFormState;
	
	/** 
	 * 支付方式
     */
	private int orderFormPayMethod;
	
	/**
	 *  支付状态
	 */
	private int payState;
	
	/**
	 * 总金额
	 */
	private BigDecimal cartRPrice;
	
	/**
	 * 买家Id
	 */
	private long userId;
	
	/**
	 * 下单支付时间
	 */
	private long payTime;
	
	/**
	 * 下单时间
	 */
	private String orderCreateTime;
	
	public OrderFormVO(){
		
	};

	public OrderFormVO(OrderFormDTO orderFormDTO) {
		this.orderId = orderFormDTO.getOrderId();
		this.consignee = orderFormDTO.getConsignee();
		this.orderFormState = orderFormDTO.getOrderFormState().getIntValue();
		this.orderFormPayMethod = orderFormDTO.getOrderFormPayMethod().getIntValue();
		this.payState = orderFormDTO.getPayState().getIntValue();
		this.cartRPrice = orderFormDTO.getCartRPrice();
		this.userId = orderFormDTO.getUserId();
		this.payTime = orderFormDTO.getPayTime();
		this.orderCreateTime = DateUtil.dateToString(orderFormDTO.getCreateTime(), DateUtil.LONG_PATTERN);
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public int getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(int orderFormState) {
		this.orderFormState = orderFormState;
	}

	public int getOrderFormPayMethod() {
		return orderFormPayMethod;
	}

	public void setOrderFormPayMethod(int orderFormPayMethod) {
		this.orderFormPayMethod = orderFormPayMethod;
	}

	public int getPayState() {
		return payState;
	}

	public void setPayState(int payState) {
		this.payState = payState;
	}

	public BigDecimal getCartRPrice() {
		return cartRPrice;
	}

	public void setCartRPrice(BigDecimal cartRPrice) {
		this.cartRPrice = cartRPrice;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	
	
	
}
