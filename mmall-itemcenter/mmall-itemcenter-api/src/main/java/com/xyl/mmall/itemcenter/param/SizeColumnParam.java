package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;

/**
 * 尺码字段信息接收类
 * 
 * @author hzhuangluqian
 *
 */
public class SizeColumnParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7586866959472042586L;

	/** 尺码字段id */
	private long id;

	/** 尺码字段名 */
	private String name;

	/** 字段是否必须 */
	private boolean isRequired;

	/** 字段单位 */
	private String unit;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

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
}
