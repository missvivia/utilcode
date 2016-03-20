package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.common.meta.BaseVersion;

@AnnonOfClass(desc = "配送区域表", tableName = "Mmall_CMS_SendDistrict", dbCreateTimeName = "CreateTime")
public class SendDistrict extends BaseVersion implements Serializable {

	private static final long serialVersionUID = 4096156416616994475L;

	/** 主键id */
	@AnnonOfField(desc = "id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "区域Id")
	private long districtId;

	@AnnonOfField(desc = "城市Id")
	private long cityId;

	@AnnonOfField(desc = "省Id")
	private long provinceId;

	@AnnonOfField(desc = "商家Id", policy = true)
	private long businessId;

	@AnnonOfField(desc = "配送区域名称", type = "VARCHAR(32)")
	private String districtName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

}
