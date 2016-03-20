/*
 * 2014-9-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.xyl.mmall.promotion.enums.PlatformType;

/**
 * FavorCaculateParams.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-11
 * @since 1.0
 */
public class FavorCaculateParamDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 优惠券couponcode，手动输入
	 */
	private String couponCode;
	
	/**
	 * 用户优惠券的Id
	 */
	private long userCouponId;

	/**
	 * 站点
	 */
	private int province;

	/**
	 * 下单用户
	 */
	private long userId;
	
	/**
	 * 是否计算优惠券
	 */
	private boolean caculateCoupon;
	
	/**
	 * 使用的红包金额
	 */
	private BigDecimal hbCash = BigDecimal.ZERO;
	
	/**
	 * po对应选中的优惠券，兼容优惠券关联po
	 */
	private Map<Long, CouponDTO> poCouponMap = new HashMap<>();
	
	/**
	 * po对应选中的活动
	 */
	private Map<Long, PromotionDTO> poPromotionMap = new HashMap<>();
	
	/**
	 * 是否退款计算
	 */
	private boolean isRefund;
	
	private long areaPermission;
	
	private PlatformType platformType;
	
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isCaculateCoupon() {
		return caculateCoupon;
	}

	public void setCaculateCoupon(boolean caculateCoupon) {
		this.caculateCoupon = caculateCoupon;
	}

	public Map<Long, CouponDTO> getPoCouponMap() {
		return poCouponMap;
	}

	public void setPoCouponMap(Map<Long, CouponDTO> poCouponMap) {
		this.poCouponMap = poCouponMap;
	}

	public Map<Long, PromotionDTO> getPoPromotionMap() {
		return poPromotionMap;
	}

	public void setPoPromotionMap(Map<Long, PromotionDTO> poPromotionMap) {
		this.poPromotionMap = poPromotionMap;
	}

	public long getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(long userCouponId) {
		this.userCouponId = userCouponId;
	}

	public BigDecimal getHbCash() {
		return hbCash;
	}

	public void setHbCash(BigDecimal hbCash) {
		this.hbCash = hbCash;
	}

	public boolean isRefund() {
		return isRefund;
	}

	public void setRefund(boolean isRefund) {
		this.isRefund = isRefund;
	}

	public long getAreaPermission() {
		return areaPermission;
	}

	public void setAreaPermission(long areaPermission) {
		this.areaPermission = areaPermission;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}
}
