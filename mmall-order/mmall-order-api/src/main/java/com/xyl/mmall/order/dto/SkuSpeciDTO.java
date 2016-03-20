package com.xyl.mmall.order.dto;

import java.io.Serializable;


/**
 * 商品属性快照
 * @author author:lhp
 *
 * @version date:2015年6月4日下午4:47:12
 */
public class SkuSpeciDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -943412698925555891L;

	/** 规格选项名. */
	private String speciOptionName;
	
	/** 规格名称. */
	private String specificationName;

	public String getSpeciOptionName() {
		return speciOptionName;
	}

	public void setSpeciOptionName(String speciOptionName) {
		this.speciOptionName = speciOptionName;
	}

	public String getSpecificationName() {
		return specificationName;
	}

	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}
	
	

}
