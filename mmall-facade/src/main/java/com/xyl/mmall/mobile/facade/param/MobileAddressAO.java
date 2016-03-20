package com.xyl.mmall.mobile.facade.param;

import java.io.Serializable;

import com.xyl.mmall.mobile.facade.vo.MobileConsigneeAddressVO;

public class MobileAddressAO extends MobileConsigneeAddressVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3521515569407374880L;
	//3:删除，2：修改，1：新增
	private int type;
	private String address;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
