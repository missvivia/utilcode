/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.vo.order;

import java.io.Serializable;

/**
 * OrderBriefInfoVO.java created by yydx811 at 2015年11月23日 下午1:03:57
 * 订单简略信息
 *
 * @author yydx811
 */
public class OrderBriefInfoVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 7352482863213784133L;

	/** 订单id. */
	private long orderId;
	
	/** 购买数. */
	private int num;
	
	/** 下单时间 yyyy-MM-dd HH:mm:ss. */
	private String orderDateStr;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getOrderDateStr() {
		return orderDateStr;
	}

	public void setOrderDateStr(String orderDateStr) {
		this.orderDateStr = orderDateStr;
	}
}
