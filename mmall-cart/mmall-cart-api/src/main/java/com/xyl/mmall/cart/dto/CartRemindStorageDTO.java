package com.xyl.mmall.cart.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 返回要提醒用户对应sku已有库存对象
 *
 */
public class CartRemindStorageDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//要提醒的用户列表
	private List<Long> userIdList;

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	} 

}
