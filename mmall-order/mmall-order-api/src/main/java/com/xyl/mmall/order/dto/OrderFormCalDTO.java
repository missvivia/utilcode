package com.xyl.mmall.order.dto;

import java.math.BigDecimal;

import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * 下单过程中的订单DTO(未完成下单,包含一些组单过程中的中间结果字段)
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormCalDTO extends OrderFormDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderFormCalDTO(OrderForm obj) {
		super(obj);
	}

	/**
	 * 构造函数
	 */
	public OrderFormCalDTO() {
		super();
	}

	/**
	 * 下单时,可选的快递方式
	 */
	private ExpressCompany[] expressArray;

	/**
	 * 下单时,可选的支付方式
	 */
	private OrderFormPayMethodDTO[] paymethodArray;

	/**
	 * 用户需要支付的总金额(红包+现金)
	 */
	private BigDecimal totalCash;

	/**
	 * 用户需要支付的金额(现金:允许EPAY或者COD)
	 */
	private BigDecimal realCash;

	/**
	 * 订单是否满足全场包邮
	 */
	private boolean isFreeExp;

	public boolean isFreeExp() {
		return isFreeExp;
	}

	public void setFreeExp(boolean isFreeExp) {
		this.isFreeExp = isFreeExp;
	}

	/**
	 * 用户需要支付的金额(现金:允许EPAY或者COD)
	 * 
	 * @return
	 */
	public BigDecimal getRealCash() {
		return realCash;
	}

	/**
	 * 用户需要支付的金额(现金:允许EPAY或者COD)
	 * 
	 * @param realCash
	 */
	public void setRealCash(BigDecimal realCash) {
		this.realCash = realCash;
	}

	/**
	 * 用户需要支付的总金额(红包+现金)
	 * 
	 * @return
	 */
	public BigDecimal getTotalCash() {
		return totalCash;
	}

	/**
	 * 用户需要支付的总金额(红包+现金)
	 * 
	 * @param totalCash
	 */
	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}

	public OrderFormPayMethodDTO[] getPaymethodArray() {
		return paymethodArray;
	}

	public void setPaymethodArray(OrderFormPayMethodDTO[] paymethodArray) {
		this.paymethodArray = paymethodArray;
	}

	public ExpressCompany[] getExpressArray() {
		return expressArray;
	}

	public void setExpressArray(ExpressCompany[] expressArray) {
		this.expressArray = expressArray;
	}

}
