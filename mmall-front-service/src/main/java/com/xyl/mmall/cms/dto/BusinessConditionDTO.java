package com.xyl.mmall.cms.dto;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;

/**
 * 商家管理搜索条件
 * @author lihongpeng
 *
 */
public class BusinessConditionDTO extends DDBParam {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1664699319474259651L;

	private long siteId;//站点
	
	private String account;
	
	private String businessId;
	
	private String companyName;
	
	private String storeName;
	
	private int isActive = -1; //0激活,1冻结
	
	/**
	 * 管理员ids
	 */
	private List<Long>agentIds;
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public List<Long> getAgentIds() {
		return agentIds;
	}

	public void setAgentIds(List<Long> agentIds) {
		this.agentIds = agentIds;
	}
}