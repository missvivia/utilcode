package com.xyl.mmall.cms.vo;


/**
 * 
 *
 * @author hzchaizhf
 * @version 2014-9-17
 */
public class BusinessConditionAO extends PagerConditionAO {

	private int type;
	
	private long province;
	
	private String account;
	
	private String businessId;
	
	private String companyName;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getProvince() {
		return province;
	}

	public void setProvince(long province) {
		this.province = province;
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

}
