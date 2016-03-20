package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 尺码字段meta类
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SizeColumn", desc = "尺寸字段库")
public class SizeColumn implements Serializable {

	private static final long serialVersionUID = 8035170212857116557L;

	/** 主键，尺码字段id */
	@AnnonOfField(desc = "主键，尺寸字段id", primary = true, primaryIndex = 1, autoAllocateId = true, policy = true)
	private long id;

	/** 尺码字段名 */
	@AnnonOfField(desc = "尺码字段名")
	private String name;

	/** 单位 */
	@AnnonOfField(desc = "单位", notNull = false)
	private String unit;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
