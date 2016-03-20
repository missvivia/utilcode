package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;

/**
 * 尺码模板/自定义模板/默认模板中尺码字段DTO
 * 
 * @author hzhuangluqian
 *
 */
public class SizeColumnDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6019921164454912290L;

	/** 尺码字段id */
	private long columnId;

	/** 尺码字段名 */
	private String columnName;

	/** 模板中 该字段的次序 */
	private int colIndex;

	/** 是否必要 */
	private boolean isRequired;

	public long getColumnId() {
		return columnId;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean getIsRequired() {
		return isRequired;
	}

	public boolean IsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
}
