package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 类目和原始尺码映射表
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_CategoryOSizeMap", desc = "类目和原始尺码映射表", dbCreateTimeName = "CreateTime")
public class CategoryOSizeMap implements Serializable {

	private static final long serialVersionUID = 1115737046865733257L;

	/** 类目id */
	@AnnonOfField(desc = "类目id", primary = true, primaryIndex = 1, policy = true)
	private long categoryId;

	/** 该类目下原始尺码模板id */
	@AnnonOfField(desc = "该类目下原始尺码模板id")
	private long originalSizeId;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getOriginalSizeId() {
		return originalSizeId;
	}

	public void setOriginalSizeId(long originalSizeId) {
		this.originalSizeId = originalSizeId;
	}
}
