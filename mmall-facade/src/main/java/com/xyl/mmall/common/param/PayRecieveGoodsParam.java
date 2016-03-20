package com.xyl.mmall.common.param;

import com.xyl.mmall.order.enums.OperateUserType;

/**
 * 支付确认收货参数
 * @author author:lhp
 *
 * @version date:2015年8月21日上午9:51:15
 */
public class PayRecieveGoodsParam {
	

	/**
	 * 订单号
	 */
	private long orderId;
	
	/**
	 * 订单userId
	 */
	private long userId;
	
	/**
	 * 操作者Id
	 */
	private long agentId;
	
	/**
	 * 商家ID
	 */
	private long businessId;
	
	/**
	 * 操作者类型
	 */
	private OperateUserType operateUserType;
	
	public PayRecieveGoodsParam(){
		
	}
	
	public PayRecieveGoodsParam(long orderId, long userId,long businessId, long agentId,
			OperateUserType operateUserType) {
		this.orderId = orderId;
		this.userId = userId;
		this.businessId = businessId;
		this.agentId = agentId;
		this.operateUserType = operateUserType;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	
	
	

}
