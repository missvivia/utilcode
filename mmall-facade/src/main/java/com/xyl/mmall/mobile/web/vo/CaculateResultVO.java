/*
 * 2014-9-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mobile.web.vo;

import java.util.List;
import java.util.Map;

/**
 * CaculateResultVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-24
 * @since      1.0
 */
public class CaculateResultVO {
	
	/**
	 * 是否包邮
	 */
	private boolean isFreeExpPrice;
	
	/**
	 * 购物车活动
	 */
	List<CartActivationVO> activations;
	
	/**
	 * 不满足条件的po列表
	 */
	private Map<Long, List<CartSkuItemVO>> notSatisfySkuList;

	public boolean isFreeExpPrice() {
		return isFreeExpPrice;
	}

	public void setFreeExpPrice(boolean isFreeExpPrice) {
		this.isFreeExpPrice = isFreeExpPrice;
	}

	public List<CartActivationVO> getActivations() {
		return activations;
	}

	public void setActivations(List<CartActivationVO> activations) {
		this.activations = activations;
	}

	public Map<Long, List<CartSkuItemVO>> getNotSatisfySkuList() {
		return notSatisfySkuList;
	}

	public void setNotSatisfySkuList(Map<Long, List<CartSkuItemVO>> notSatisfySkuList) {
		this.notSatisfySkuList = notSatisfySkuList;
	}
}
