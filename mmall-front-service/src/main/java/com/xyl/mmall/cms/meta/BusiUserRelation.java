package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.common.meta.BaseVersion;

@AnnonOfClass(desc = "商家指定用户表", tableName = "Mmall_CMS_BusiUserRelation",dbCreateTimeName = "CreateTime")
public class BusiUserRelation extends BaseVersion implements Serializable {

	private static final long serialVersionUID = 4096156416616994475L;

	/** 主键id */
	@AnnonOfField(desc = "id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "商家Id", policy = true)
	private long businessId;
	
	@AnnonOfField(desc = "用户Id")
	private long userId;
	
	@AnnonOfField(desc = "用户名称")
	private String userName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
}
