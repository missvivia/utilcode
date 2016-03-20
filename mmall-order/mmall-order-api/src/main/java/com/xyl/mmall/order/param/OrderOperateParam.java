package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 订单操作参数
 * @author author:lhp
 *
 * @version date:2015年6月16日下午1:27:07
 */
public class OrderOperateParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2874270711057383552L;
	
	/**
	 * 用户Id
	 */
	private long userId;
	
	/**
	 * 订单Id
	 */
	private long orderId;
	
	/**
	 * 订单parentId
	 */
	private long parentId;
	
	/**
	 * 商家Id
	 */
	private long businessId;
	
	/**
	 * 操作人Id
	 */
	private long agentId;
	
	
	/**
	 * 新的订单状态
	 */
	private OrderFormState newState;
	
	/**
	 * 老的订单状态
	 */
	private OrderFormState[] oldStateArray;
	
	/**
	 * 操作备注
	 */
	private String comment;
	
	/**
	 * 操作用户类型
	 */
	private OperateUserType operateUserType;
	
	/**
	 * 操作日志类型
	 */
	private OperateLogType operateLogType;
	
	/**
	 * 总金额
	 */
	private BigDecimal totalCash;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public OrderFormState getNewState() {
		return newState;
	}

	public void setNewState(OrderFormState newState) {
		this.newState = newState;
	}

	public OrderFormState[] getOldStateArray() {
		return oldStateArray;
	}

	public void setOldStateArray(OrderFormState[] oldStateArray) {
		this.oldStateArray = oldStateArray;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	public BigDecimal getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(BigDecimal totalCash) {
		this.totalCash = totalCash;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	public OperateLogType getOperateLogType() {
		return operateLogType;
	}

	public void setOperateLogType(OperateLogType operateLogType) {
		this.operateLogType = operateLogType;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	
	
	

}
