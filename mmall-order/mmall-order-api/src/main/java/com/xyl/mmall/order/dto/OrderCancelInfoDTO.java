package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.meta.OrderCancelInfo;

/**
 * 订单取消信息
 * 
 * @author dingmingliang
 *
 */
public class OrderCancelInfoDTO extends OrderCancelInfo {
	
	
	/**
	 * 操作用户类型
	 */
	private OperateUserType operateUserType;
	
	/**
	 * 操作用户Id
	 */
	private long agentId = 0l;
	
	/**
	 * 商家Id
	 */
	private long businessId;

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;
	
	/**
	 * 构造函数
	 */
	public OrderCancelInfoDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderCancelInfoDTO(OrderCancelInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
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

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}
	
	
	
}
