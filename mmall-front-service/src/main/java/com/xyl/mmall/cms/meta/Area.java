package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "销售区域表", tableName = "TB_CMS_Area")
public class Area implements Serializable {
	
	private static final long serialVersionUID = 20140918L;
	
	@AnnonOfField(desc = "销售区域id", primary = true,autoAllocateId =true)
	private long id;
	
	@AnnonOfField(desc = "区域名称")
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
	
	
	
}
