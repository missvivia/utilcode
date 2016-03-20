package com.xyl.mmall.mainsite.vo.order;


import com.xyl.mmall.framework.enums.ExpressCompany;

/**
 * 物流VO
 * @author author:lhp
 *
 * @version date:2015年6月3日下午1:01:44
 */
public class OrderLogisticsVO {
	
	/**
	 * 物流Id
	 */
	private long id;

	/**
	 * 订单号
	 */
	private long orderId;

	/**
	 * 物流单号
	 */
	private String mailNO;

	/**
	 * 物流公司
	 */
	private ExpressCompany expressCompany;
	
	/**
	 * 发货日期
	 */
	private String deliverDate;  

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

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}
	
	
}
