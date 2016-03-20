package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.itemcenter.intf.Size;

/**
 * 尺码模板的字段集合类
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_TemplateSize", desc = "尺码模板字段表")
public class TemplateSize implements Size, Serializable {

	private static final long serialVersionUID = 1063922464479223847L;

	/** 尺码模板id */
	@AnnonOfField(desc = "尺码模板id", primary = true, primaryIndex = 1, policy = true)
	private long sizeTemplateId;

	/** 尺码字段id */
	@AnnonOfField(desc = "尺码字段id", primary = true, primaryIndex = 2)
	private long columnId;

	/** 该字段在尺码模板中显示的次序 */
	@AnnonOfField(desc = "该字段在尺码模板中显示的次序")
	private long colIndex;

	/** 是否是必填项 */
	@AnnonOfField(desc = "是否是必填项")
	private boolean isRequired;

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

	public long getColIndex() {
		return colIndex;
	}

	public void setColIndex(long colIndex) {
		this.colIndex = colIndex;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
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
