package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.util.Map;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 设置网易宝支付成功,并更新订单+交易状态的参数
 * 
 * @author dingmingliang
 * 
 */
public class OrderServiceSetStateToEPayedParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 订单Id
	 */
	private long orderId;

	/**
	 * 交易Id(EPay交易)
	 */
	private long tradeId;

	/**
	 * 用户Id
	 */
	private long userId;

	/**
	 * 
	 */
	private String orderSn;

	/**
	 * 
	 */
	private String payOrderSn;
	
	
	private Map<Long, Long> orderIdMap;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}

	public Map<Long, Long> getOrderIdMap() {
		return orderIdMap;
	}

	public void setOrderIdMap(Map<Long, Long> orderIdMap) {
		this.orderIdMap = orderIdMap;
	}
	
	
}
