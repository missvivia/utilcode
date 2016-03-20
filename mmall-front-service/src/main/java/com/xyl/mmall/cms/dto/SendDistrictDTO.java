package com.xyl.mmall.cms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.SendDistrict;

public class SendDistrictDTO extends SendDistrict{
	
	/**
	 * 区名称
	 */
	private String distName;

	/**
	 * 城市名称
	 */
	private String cityName;

	/**
	 * 省名称
	 */
	private String provinceName;

	
	/**
	 * 是否可编辑
	 */
	private boolean isEdit = false;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6058469223335358329L;
	
	public SendDistrictDTO(){
	}


	public SendDistrictDTO(SendDistrict obj){
		ReflectUtil.convertObj(this, obj, false);
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}


	public String getDistName() {
		return distName;
	}


	public void setDistName(String distName) {
		this.distName = distName;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getProvinceName() {
		return provinceName;
	}


	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	
	

}
