package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 面向用户的快递费率表
 * 
 * @author hzzhaozhenzuo
 * 
 */
@AnnonOfClass(tableName = "Mmall_Oms_ExpressFee", desc = "面向用户的快递费率")
public class ExpressFee implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "物流公司code")
	private String expressCompanyCode;

	@AnnonOfField(desc = "物流公司名称")
	private String expressCompanyName;

	@AnnonOfField(desc = "站点id", policy = true)
	private long siteId;

	@AnnonOfField(desc = "站点名称,如浙江省")
	private String siteName;

	@AnnonOfField(desc = "服务方式code", notNull = false)
	private String serviceModeCode;

	@AnnonOfField(desc = "服务方式名称", notNull = false)
	private String serviceModeName;

	@AnnonOfField(desc = "目的省id", notNull = false)
	private long targetProvinceId;

	@AnnonOfField(desc = "目的省名称")
	private String targetProvinceName;

	@AnnonOfField(desc = "是否COD服务")
	private boolean codService;

	@AnnonOfField(desc = "价格", defa = "0")
	private BigDecimal price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
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

	public long getTargetProvinceId() {
		return targetProvinceId;
	}

	public void setTargetProvinceId(long targetProvinceId) {
		this.targetProvinceId = targetProvinceId;
	}

	public String getTargetProvinceName() {
		return targetProvinceName;
	}

	public void setTargetProvinceName(String targetProvinceName) {
		this.targetProvinceName = targetProvinceName;
	}

	public boolean isCodService() {
		return codService;
	}

	public void setCodService(boolean codService) {
		this.codService = codService;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
