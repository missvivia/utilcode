/*
 * 2014-12-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

/**
 * PromotionQueryBeanDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-24
 * @since      1.0
 */
@SuppressWarnings("serial")
public class PromotionQueryBeanDTO extends PromotionDTO {
	
	/**
	 * 最大权限
	 */
	private long fullPermission;
	
	
	private int limit;
	
	private int offset;

	public long getFullPermission() {
		return fullPermission;
	}

	public void setFullPermission(long fullPermission) {
		this.fullPermission = fullPermission;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
