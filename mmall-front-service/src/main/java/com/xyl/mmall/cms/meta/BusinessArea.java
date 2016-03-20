package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "商家对应站点信息", tableName = "TB_CMS_BusinessArea")
public class BusinessArea implements Serializable {

	private static final long serialVersionUID = 20140915L;
	
	@AnnonOfField(desc = "表id", primary = true,autoAllocateId =true)
	private long id;
	
	@AnnonOfField(desc = "商家类型")
	private int type;
	
	@AnnonOfField(desc = "商家id")
	private long supplierId;
	
	@AnnonOfField(desc = "商家帐号")
	private String businessAccount;
	
	@AnnonOfField(desc = "站点id")
	private long areaId;
	
	@AnnonOfField(desc = "品牌id")
	private long actingBrandId;
	
	@AnnonOfField(desc = "站点名称")
	private String areaName;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;
	
	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getActingBrandId() {
		return actingBrandId;
	}

	public void setActingBrandId(long actingBrandId) {
		this.actingBrandId = actingBrandId;
	}

}
