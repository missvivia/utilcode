package com.xyl.mmall.mobile.web.vo.order;

import java.io.Serializable;

import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.OrderFormPayMethod;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
@Deprecated
public class DeprecatedReturnOrderSkuInfoListVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078794042536610483L;

	private DeprecatedReturnOrderSkuInfoList returns = new DeprecatedReturnOrderSkuInfoList();
	
	private OrderFormPayMethod payMethod;

	private String orderId;
	
	private DeprecatedReturnState returnState;
	
	public DeprecatedReturnOrderSkuInfoList getReturns() {
		return returns;
	}

	public void setReturns(DeprecatedReturnOrderSkuInfoList returns) {
		this.returns = returns;
	}

	public OrderFormPayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(OrderFormPayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public DeprecatedReturnState getReturnState() {
		return returnState;
	}

	public void setReturnState(DeprecatedReturnState retState) {
		this.returnState = retState;
	}
	
}
