package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * 成功下单后的订单DTO(简洁版)
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormBriefDTO extends OrderForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 是否可以取消订单
	 */
	private boolean canCancel;

	/**
	 * 
	 */
	private OrderFormExtInfoDTO extDTO;
	
	private String userName;//用户账号
	
	private String businessAccount;//商家账号

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderFormBriefDTO(OrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
		extDTO = OrderFormExtInfoDTO.genOrderFormExtInfoDTOByOrder(this);
	}

	/**
	 * 构造函数
	 */
	public OrderFormBriefDTO() {
	}

	public OrderFormExtInfoDTO getExtDTO() {
		return extDTO;
	}

	public void setExtDTO(OrderFormExtInfoDTO extDTO) {
		this.extDTO = extDTO;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}
	
	
}
