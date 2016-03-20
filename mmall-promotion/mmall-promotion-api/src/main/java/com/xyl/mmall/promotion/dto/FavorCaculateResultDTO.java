/*
 * 2014-9-11
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;

/**
 * FavorCaculateResultDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-11
 * @since      1.0
 */
public class FavorCaculateResultDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 是否包邮
	 */
	private boolean isFreeExpPrice;
	
	/**
	 * 购物车活动
	 */
	List<PromotionCartDTO> activations;
	
	/**
	 * 商品list
	 */
	private List<PromotionSkuItemDTO> skuList;
	
	/**
	 * 不满足条件的po列表
	 */
	private Map<Long, List<PromotionSkuItemDTO>> notSatisfySkuList = new HashMap<Long, List<PromotionSkuItemDTO>>();
	
	/**
	 * 选中的优惠券
	 * @return
	 */
	private CouponDTO couponDTO;
	
	/**
	 * 优惠券状态
	 */
	private int couponState;
	
	/**
	 * 红包状态
	 */
	private int rpState;
	
	/**
	 * 用户可选优惠券
	 */
	private List<CouponDTO> userCoupons = new ArrayList<>();
	
	/**
	 * 订单上的优惠券使用记录
	 */
	private List<CouponOrderTCC> couponOrderTCCList = new ArrayList<>();
	
	/**
	 * 订单上红包的使用明细
	 */
	private List<RedPacketOrderTCC> redPacketOrderTCCList = new ArrayList<>();
	
	/**
	 * 活动总的优惠价
	 */
	private BigDecimal totalPromotionDiscount = BigDecimal.ZERO;
	
	/**
	 * 优惠券总的优惠价
	 */
	private BigDecimal totalCouponDiscount = BigDecimal.ZERO;
	
	/**
	 * 购物车销售总价
	 */
	private BigDecimal totalRetailPrice = BigDecimal.ZERO;
	
	/**
	 * 可用的红包金额
	 */
	private BigDecimal canUseRedPackets = BigDecimal.ZERO;
	
	/**
	 * 优惠券使用的效果索引
	 */
	private int index = -1;

	public boolean isFreeExpPrice() {
		return isFreeExpPrice;
	}

	public void setFreeExpPrice(boolean isFreeExpPrice) {
		this.isFreeExpPrice = isFreeExpPrice;
	}

	public List<PromotionCartDTO> getActivations() {
		return activations;
	}

	public void setActivations(List<PromotionCartDTO> activations) {
		this.activations = activations;
	}

	public Map<Long, List<PromotionSkuItemDTO>> getNotSatisfySkuList() {
		return notSatisfySkuList;
	}

	public void setNotSatisfySkuList(Map<Long, List<PromotionSkuItemDTO>> notSatisfySkuList) {
		this.notSatisfySkuList = notSatisfySkuList;
	}

	public CouponDTO getCouponDTO() {
		return couponDTO;
	}

	public void setCouponDTO(CouponDTO couponDTO) {
		this.couponDTO = couponDTO;
	}

	public int getCouponState() {
		return couponState;
	}

	public void setCouponState(int couponState) {
		this.couponState = couponState;
	}

	public int getRpState() {
		return rpState;
	}

	public void setRpState(int rpState) {
		this.rpState = rpState;
	}

	public List<CouponDTO> getUserCoupons() {
		return userCoupons;
	}

	public void setUserCoupons(List<CouponDTO> userCoupons) {
		this.userCoupons = userCoupons;
	}

	public List<CouponOrderTCC> getCouponOrderTCCList() {
		return couponOrderTCCList;
	}

	public void setCouponOrderTCCList(List<CouponOrderTCC> couponOrderTCCList) {
		this.couponOrderTCCList = couponOrderTCCList;
	}

	public List<RedPacketOrderTCC> getRedPacketOrderTCCList() {
		return redPacketOrderTCCList;
	}

	public void setRedPacketOrderTCCList(List<RedPacketOrderTCC> redPacketOrderTCCList) {
		this.redPacketOrderTCCList = redPacketOrderTCCList;
	}

	public BigDecimal getTotalPromotionDiscount() {
		return totalPromotionDiscount;
	}

	public void setTotalPromotionDiscount(BigDecimal totalPromotionDiscount) {
		this.totalPromotionDiscount = totalPromotionDiscount;
	}

	public BigDecimal getTotalCouponDiscount() {
		return totalCouponDiscount;
	}

	public void setTotalCouponDiscount(BigDecimal totalCouponDiscount) {
		this.totalCouponDiscount = totalCouponDiscount;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public BigDecimal getTotalRetailPrice() {
		return totalRetailPrice;
	}

	public void setTotalRetailPrice(BigDecimal totalRetailPrice) {
		this.totalRetailPrice = totalRetailPrice;
	}

	public BigDecimal getCanUseRedPackets() {
		return canUseRedPackets;
	}

	public void setCanUseRedPackets(BigDecimal canUseRedPackets) {
		this.canUseRedPackets = canUseRedPackets;
	}

	public List<PromotionSkuItemDTO> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<PromotionSkuItemDTO> skuList) {
		this.skuList = skuList;
	}
	
	
}
