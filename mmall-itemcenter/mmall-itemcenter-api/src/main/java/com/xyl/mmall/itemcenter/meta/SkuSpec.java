package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 产品规格
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SkuSpec", desc = "产品规格")
public class SkuSpec implements Serializable {

	private static final long serialVersionUID = 5791575043968754109L;

	/** 主键值，产品规格id */
	@AnnonOfField(desc = "主键值，产品规格id", primary = true, primaryIndex = 1, autoAllocateId = true)
	private long id;

	/** 规格名称 */
	@AnnonOfField(desc = "规格名称 ")
	private String name;

	/** 前端描述 */
	@AnnonOfField(desc = "前端描述")
	private String frontDesc;

	/** 修改时间 */
	@AnnonOfField(desc = "修改时间")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrontDesc() {
		return frontDesc;
	}

	public void setFrontDesc(String frontDesc) {
		this.frontDesc = frontDesc;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}