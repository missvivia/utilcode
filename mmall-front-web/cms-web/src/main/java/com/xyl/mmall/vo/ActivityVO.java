/*
 * @(#) 2014-9-26
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.vo;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.ActivitySchedule;
import com.xyl.mmall.promotion.activity.Label;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * ActivityVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-26
 * @since      1.0
 */
public class ActivityVO extends Promotion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 省份列表
	 */
	private List<AreaDTO> provinceList = new ArrayList<>();
	
	private List<ActivitySchedule> schedules = new ArrayList<>();
	
	private List<Activation> itemList;
	
	private List<Label> labelList;
	
	private List<Long> provinceIds;
	
	private String applyUserName;
	
	private String auditUserName;
	
	private String allProvince;
	
	private String allPo;
	
	
	public ActivityVO() {}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public ActivityVO(Promotion obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public List<AreaDTO> getProvinceList() {
		return provinceList;
	}


	public void setProvinceList(List<AreaDTO> provinceList) {
		this.provinceList = provinceList;
	}


	public List<ActivitySchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ActivitySchedule> schedules) {
		this.schedules = schedules;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public List<Activation> getItemList() {
		return itemList;
	}

	public void setItemList(List<Activation> itemList) {
		this.itemList = itemList;
	}

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public List<Long> getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(List<Long> provinceIds) {
		this.provinceIds = provinceIds;
	}

	public String getAllProvince() {
		return allProvince;
	}

	public void setAllProvince(String allProvince) {
		this.allProvince = allProvince;
	}

	public String getAllPo() {
		return allPo;
	}

	public void setAllPo(String allPo) {
		this.allPo = allPo;
	}
}
