package com.xyl.mmall.order.dto;

import java.io.Serializable;

import com.xyl.mmall.order.enums.OrderFormPayMethod;

/**
 * 支付方式DTO
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormPayMethodDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 支付方式
	 */
	private OrderFormPayMethod payMethod;

	/**
	 * 当前支付方式是否有效
	 */
	private boolean isValid = true;

	/**
	 * 支付方式无效时的文案
	 */
	private String invalidMess = "";

	public OrderFormPayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(OrderFormPayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getInvalidMess() {
		return invalidMess;
	}

	public void setInvalidMess(String invalidMess) {
		this.invalidMess = invalidMess;
	}
}
