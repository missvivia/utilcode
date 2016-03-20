package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileAreaOpVO extends MobileAreaBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -487700508495212338L;

	//操作类型
	//string	“ADD”,”DELETE”,”UPDATE”
	private int opType;
	private int areaType;
	private long parentCode;
	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}
	
}
