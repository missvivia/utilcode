package com.xyl.mmall.backend.vo;

import java.util.ArrayList;
import java.util.List;

public class ProductParamVO {
	private String id;

	private String name;

	private String value;
	
	/** 1:选择 2:文本框 3: 文本域 4: 多选 */
	private int type;

	private boolean isRequired;

	private List<ProdParamOption> list;

	public ProductParamVO() {
		list = new ArrayList<ProdParamOption>();
		value = "";
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



	public boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public List<ProdParamOption> getList() {
		return list;
	}

	public void setList(List<ProdParamOption> list) {
		this.list = list;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
