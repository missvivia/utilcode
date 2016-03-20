/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.vo;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public class CouponVO extends Coupon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Activation> itemList;
	
	private String applyUserName;
	
	private String auditUserName;
	
	private List<AreaDTO> areaList;
	
	public CouponVO() {}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CouponVO(Coupon obj) {
		ReflectUtil.convertObj(this, obj, false);
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

	public List<AreaDTO> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<AreaDTO> areaList) {
		this.areaList = areaList;
	}

	public int getCodeTypeValue() {
		if (this.getCodeType() != null) {
			return this.getCodeType().getIntValue();
		}
		return 1;
	}

	public int getBinderTypeValue() {
		if (this.getBinderType() != null) {
			return this.getBinderType().getIntValue();
		}
		return 3;
	}
}
