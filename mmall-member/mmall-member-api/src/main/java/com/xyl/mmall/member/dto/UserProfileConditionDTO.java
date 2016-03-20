package com.xyl.mmall.member.dto;

import com.netease.print.daojar.meta.base.DDBParam;

public class UserProfileConditionDTO extends DDBParam{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2767135941267477548L;
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
