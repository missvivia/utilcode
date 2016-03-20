/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * ERPAccount.java created by yydx811 at 2015年8月3日 上午11:18:00
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "erp appid关系表", tableName = "Mmall_ERP_Account")
public class ERPAccount implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 6587158100583106544L;

	@AnnonOfField(desc = "主键id", primary = true, autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "应用id", uniqueKey = true)
	private String appId;

	@AnnonOfField(desc = "应用id对应key", uniqueKey = true)
	private String appIdKey;

	@AnnonOfField(desc = "商家id，逗号分隔", defa = "")
	private String businessIds;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppIdKey() {
		return appIdKey;
	}

	public void setAppIdKey(String appIdKey) {
		this.appIdKey = appIdKey;
	}

	public String getBusinessIds() {
		return businessIds;
	}

	public void setBusinessIds(String businessIds) {
		this.businessIds = businessIds;
	}
}
