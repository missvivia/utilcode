package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.meta.OrderLogistics;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月1日上午10:48:51
 */
public class OrderLogisticsDTO extends OrderLogistics{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6821730904629015507L;
	
	
	private long businessId;
	
	private long userId;
	
	private OperateUserType operateUserType;
	
	/**
	 * 备注
	 */
	private String comment;
	
	public OrderLogisticsDTO(){
	}
	
	public OrderLogisticsDTO(OrderLogistics obj){
		ReflectUtil.convertObj(this, obj, false);
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	
	
}
