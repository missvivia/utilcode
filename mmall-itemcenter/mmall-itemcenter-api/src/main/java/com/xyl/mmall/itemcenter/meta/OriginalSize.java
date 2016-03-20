package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.Size;

/**
 * 生成尺码模板或者商品尺码自定义的原始尺码对象
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_OriginalSize", desc = "原始尺码表")
public class OriginalSize implements Size, Serializable {
	
	private static final long serialVersionUID = 5247524190673556633L;

	/** 原始尺码id */
	@AnnonOfField(desc = "原始尺码id", primary = true, primaryIndex = 1, policy = true)
	private long id;

	/** 所属尺码下的一个字段id */
	@AnnonOfField(desc = "所属尺码下的一个字段id", primary = true, primaryIndex = 2)
	private long columnId;

	/** 字段在尺码中显示的次序 */
	@AnnonOfField(desc = "字段在尺码中显示的次序")
	private long colIndex;

	/** 该字段是否必填 */
	@AnnonOfField(desc = "该字段是否必填")
	private boolean isRequired;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getColumnId() {
		return columnId;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	public long getColIndex() {
		return colIndex;
	}

	public void setColIndex(long colIndex) {
		this.colIndex = colIndex;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isRequired() {
		return isRequired;
	}

	@Override
	public long getTemplateKey() {
		return id;
	}

	@Override
	public void setTemplateKey(long templateKey) {
		this.id = templateKey;
	}
}
