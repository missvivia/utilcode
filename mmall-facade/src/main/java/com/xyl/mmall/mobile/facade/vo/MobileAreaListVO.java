package com.xyl.mmall.mobile.facade.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class MobileAreaListVO extends MobileAreaBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -487700508495212338L;

	//子城市
	private List<MobileAreaBaseVO> subArea;

	public List<MobileAreaBaseVO> getSubArea() {
		return subArea;
	}

	public void setSubArea(List<MobileAreaBaseVO> subArea) {
		this.subArea = subArea;
	}


	
}
