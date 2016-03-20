package com.xyl.mmall.mainsite.vo.order;

import java.io.Serializable;

import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
public class ReturnOrderSkuInfoListVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078794042536610483L;

	private ReturnOrderSkuInfoList returns = new ReturnOrderSkuInfoList();
	
	private OrderFormPayMethod payMethod;

	private String orderId;
	
	private String ordPkgId;
	
	private ReturnPackageState returnState;
	
	public ReturnOrderSkuInfoList getReturns() {
		return returns;
	}

	public void setReturns(ReturnOrderSkuInfoList returns) {
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

	public ReturnPackageState getReturnState() {
		return returnState;
	}

	public void setReturnState(ReturnPackageState retState) {
		this.returnState = retState;
	}

	public String getOrdPkgId() {
		return ordPkgId;
	}

	public void setOrdPkgId(String orderPackageId) {
		this.ordPkgId = orderPackageId;
	}
	
}
