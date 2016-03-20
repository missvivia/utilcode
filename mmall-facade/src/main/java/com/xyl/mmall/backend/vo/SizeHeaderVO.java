package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.itemcenter.param.SizeColumnParam;

public class SizeHeaderVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -670984165844094256L;

	private String id;

	private String name;

	private boolean isRequired;

	private String unit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public SizeColumnParam transfer() {
		SizeColumnParam param = new SizeColumnParam();
		param.setId(Long.valueOf(id));
		param.setIsRequired(isRequired);
		param.setName(name);
		param.setUnit(unit);
		return param;
	}
}
