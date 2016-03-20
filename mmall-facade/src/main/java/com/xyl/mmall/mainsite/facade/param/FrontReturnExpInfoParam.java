package com.xyl.mmall.mainsite.facade.param;

import java.io.Serializable;

/**
 * 前端"退货申请"的参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月9日 下午5:31:36
 *
 */
@Deprecated
public class FrontReturnExpInfoParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5637171798216221866L;

	private long orderId;
	
	private long returnId;
	
	private String returnExpInfoId;
	
	private int expValue;	// ExpressCompany
	
	private String expNO;
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getReturnId() {
		return returnId;
	}

	public void setReturnId(long returnId) {
		this.returnId = returnId;
	}

	public String getReturnExpInfoId() {
		return returnExpInfoId;
	}

	public void setReturnExpInfoId(String returnExpInfoId) {
		this.returnExpInfoId = returnExpInfoId;
	}

	public int getExpValue() {
		return expValue;
	}

	public void setExpValue(int expValue) {
		this.expValue = expValue;
	}

	public String getExpNO() {
		return expNO;
	}

	public void setExpNO(String expNO) {
		this.expNO = expNO;
	}
}
