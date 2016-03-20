/*
 * @(#) 2014-9-25
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.meta.Coupon;

/**
 * PromotionCartDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-25
 * @since      1.0
 */
public class PromotionCartDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 使用的活动
	 */
	private PromotionDTO promotionDTO;
	
	/**
	 * 使用的优惠券
	 */
	private CouponDTO couponDTO;
	
	/**
	 * 满足条件的po列表
	 */
	private Map<Long, List<PromotionSkuItemDTO>> poSkuMap = new HashMap<>();
	
	/**
	 * 满足条件的可选优惠券列表，兼容优惠券关联po
	 */
	private List<Coupon> userCoupons = new ArrayList<>();
	
	/**
	 * 活动提示
	 */
	private List<String> activityTips = new ArrayList<>();
	
	/**
	 * 梯度提示
	 */
	private String stepTip;
	
	/**
	 * 去凑单url
	 */
	private String coudanUrl;
	
	/**
	 * 使用活动中的第几个优惠
	 */
	private int index = -1;

	public PromotionDTO getPromotionDTO() {
		return promotionDTO;
	}

	public void setPromotionDTO(PromotionDTO promotionDTO) {
		this.promotionDTO = promotionDTO;
	}

	public Map<Long, List<PromotionSkuItemDTO>> getPoSkuMap() {
		return poSkuMap;
	}

	public void setPoSkuMap(Map<Long, List<PromotionSkuItemDTO>> poSkuMap) {
		this.poSkuMap = poSkuMap;
	}

	public CouponDTO getCouponDTO() {
		return couponDTO;
	}

	public void setCouponDTO(CouponDTO couponDTO) {
		this.couponDTO = couponDTO;
	}

	public List<Coupon> getUserCoupons() {
		return userCoupons;
	}

	public void setUserCoupons(List<Coupon> userCoupons) {
		this.userCoupons = userCoupons;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<String> getActivityTips() {
		return activityTips;
	}

	public void setActivityTips(List<String> activityTips) {
		this.activityTips = activityTips;
	}

	public String getStepTip() {
		return stepTip;
	}

	public void setStepTip(String stepTip) {
		this.stepTip = stepTip;
	}

	public String getCoudanUrl() {
		return coudanUrl;
	}

	public void setCoudanUrl(String coudanUrl) {
		this.coudanUrl = coudanUrl;
	}
}
