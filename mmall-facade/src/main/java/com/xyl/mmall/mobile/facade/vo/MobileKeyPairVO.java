package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileKeyPairVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7803477082604287969L;
	//id(通用，所以对应String)
	private long id;
	//名称,对应描述
	private String name;

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
