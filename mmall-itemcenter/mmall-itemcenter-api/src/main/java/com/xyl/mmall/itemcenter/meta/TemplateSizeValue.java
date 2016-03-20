package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.SizeValue;

/**
 * 尺码模板每个字段的值
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_TemplateSizeValue", desc = "尺寸模板字段值表")
public class TemplateSizeValue implements Serializable, SizeValue {

	private static final long serialVersionUID = -4552973028407008666L;

	/** 尺码模板id */
	@AnnonOfField(desc = "尺码模板id", primary = true, primaryIndex = 1, policy = true)
	private long sizeTemplateId;

	/** 尺码字段id */
	@AnnonOfField(desc = "尺码字段id", primary = true, primaryIndex = 2)
	private long columnId;

	/** 属于第几条值记录 */
	@AnnonOfField(desc = "属于第几条值记录", primary = true, primaryIndex = 3)
	private long recordIndex;

	/** 尺码字段的值 */
	@AnnonOfField(desc = "尺码字段的值")
	private String value;

	public long getSizeTemplateId() {
		return sizeTemplateId;
	}

	public void setSizeTemplateId(long sizeTemplateId) {
		this.sizeTemplateId = sizeTemplateId;
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
		return sizeTemplateId;
	}

	@Override
	public void setTemplateKey(long templateKey) {
		this.sizeTemplateId = templateKey;
	}
}
