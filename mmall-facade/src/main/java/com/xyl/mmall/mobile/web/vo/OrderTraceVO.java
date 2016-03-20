/**
 * 
 */
package com.xyl.mmall.mobile.web.vo;

import com.xyl.mmall.oms.meta.OrderTrace;

/**
 * @author lihui
 *
 */
public class OrderTraceVO {

	private OrderTrace orderTrace;

	public OrderTraceVO(OrderTrace orderTrace) {
		this.orderTrace = orderTrace;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return orderTrace.getId();
	}

	/**
	 * @return the expressNO
	 */
	public String getExpressNO() {
		return orderTrace.getExpressNO();
	}

	/**
	 * @return the expressCompany
	 */
	public String getExpressCompany() {
		return orderTrace.getExpressCompany();
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return orderTrace.getTime();
	}

	/**
	 * @return the operater
	 */
	public String getOperater() {
		return orderTrace.getOperater();
	}

	/**
	 * @return the operaterPhone
	 */
	public String getOperaterPhone() {
		return orderTrace.getOperaterPhone();
	}

	/**
	 * @return the operate
	 */
	public String getOperate() {
		return orderTrace.getOperate();
	}
	
	/**
	 * @return the operate
	 */
	public String getOperateDesc() {
		return orderTrace.getOperateDesc();
	}

	/**
	 * @return the operateOrg
	 */
	public String getOperateOrg() {
		return orderTrace.getOperateOrg();
	}

	/**
	 * @return the desc
	 */
	public String getNote() {
		return orderTrace.getNote();
	}

}
