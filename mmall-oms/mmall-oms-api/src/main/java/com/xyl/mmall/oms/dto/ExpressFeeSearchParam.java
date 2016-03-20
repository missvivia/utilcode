package com.xyl.mmall.oms.dto;

import java.io.Serializable;

public class ExpressFeeSearchParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private String expressCompanyCode;

	private String expressCompanyName;

	private Long siteId;

	private String siteName;

	private String serviceModeCode;

	private String serviceModeName;

	private Long targetProvinceId;

	private String targetProvinceName;

	private Boolean codService;

	public String getExpressCompanyCode() {
		return expressCompanyCode;
	}

	public void setExpressCompanyCode(String expressCompanyCode) {
		this.expressCompanyCode = expressCompanyCode;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getServiceModeCode() {
		return serviceModeCode;
	}

	public void setServiceModeCode(String serviceModeCode) {
		this.serviceModeCode = serviceModeCode;
	}

	public String getServiceModeName() {
		return serviceModeName;
	}

	public void setServiceModeName(String serviceModeName) {
		this.serviceModeName = serviceModeName;
	}

	public Long getTargetProvinceId() {
		return targetProvinceId;
	}

	public void setTargetProvinceId(Long targetProvinceId) {
		this.targetProvinceId = targetProvinceId;
	}

	public String getTargetProvinceName() {
		return targetProvinceName;
	}

	public void setTargetProvinceName(String targetProvinceName) {
		this.targetProvinceName = targetProvinceName;
	}

	public Boolean getCodService() {
		return codService;
	}

	public void setCodService(Boolean codService) {
		this.codService = codService;
	}

	public String toString() {
		return expressCompanyCode + "," + siteId + "," + serviceModeCode + ","
				+ targetProvinceId + "," + codService;
	}

}
