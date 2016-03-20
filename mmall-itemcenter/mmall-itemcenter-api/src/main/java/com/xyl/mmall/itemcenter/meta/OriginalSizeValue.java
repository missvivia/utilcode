package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.SizeValue;

@AnnonOfClass(tableName = "Mmall_ItemCenter_OriginalSizeValue", desc = "默认模板字段值表")
public class OriginalSizeValue implements SizeValue, Serializable {

	private static final long serialVersionUID = 5764707283092591720L;

	/** 原始尺码id */
	@AnnonOfField(desc = "原始尺码id", primary = true, primaryIndex = 1, policy = true)
	private long id;

	/** 所属尺码下的一个字段id */
	@AnnonOfField(desc = "所属尺码下的一个字段id", primary = true, primaryIndex = 2)
	private long columnId;

	/** 属于第几条值记录 */
	@AnnonOfField(desc = "属于第几条值记录", primary = true, primaryIndex = 3)
	private long recordIndex;

	/** 尺码字段的值 */
	@AnnonOfField(desc = "尺码字段的值",type="VARCHAR(12)")
	private String value;

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

	public long getRecordIndex() {
		return recordIndex;
	}

	public void setRecordIndex(long recordIndex) {
		this.recordIndex = recordIndex;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
