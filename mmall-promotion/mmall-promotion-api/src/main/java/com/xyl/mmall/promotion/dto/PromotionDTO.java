/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * PromotionDto.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public class PromotionDTO extends Promotion {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applyUserName;
	
	private String auditUserName;
	
	private String start;
	
	private String end;
	
	/**
	 * 优惠用的索引
	 */
	private int index = -1;
	
	public PromotionDTO(){}
	
	public PromotionDTO(Promotion promotion) {
		ReflectUtil.convertObj(this, promotion, false);
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

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
