package com.xyl.mmall.common.param;

public class ProductAttr {
	private long id;

	private boolean isRequired;

	private String attrName;

	public ProductAttr(long id, boolean isRequired, String attrName) {
		this.id = id;
		this.isRequired = isRequired;
		this.attrName = attrName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

}
