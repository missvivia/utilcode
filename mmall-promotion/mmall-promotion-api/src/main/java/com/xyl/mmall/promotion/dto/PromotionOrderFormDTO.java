/*
 * 2014-9-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.util.List;

/**
 * OrderFormDTO.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-11
 * @since 1.0
 */
public class PromotionOrderFormDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long userId;

	private List<PromotionSkuItemDTO> skuItems;

	public PromotionOrderFormDTO() {
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<PromotionSkuItemDTO> getSkuItems() {
		return skuItems;
	}

	public void setSkuItems(List<PromotionSkuItemDTO> skuItems) {
		this.skuItems = skuItems;
	}
}
