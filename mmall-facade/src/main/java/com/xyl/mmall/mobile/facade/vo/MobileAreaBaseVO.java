package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileAreaBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -487700508495212338L;
	//icon image 连接
	protected long code;
	//名称
	protected String name;
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
