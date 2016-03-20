/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;

import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface CouponService {
	
	/**
	 * 获取优惠券列表
	 * @param ids
	 * @param state
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<Coupon> getCouponList(List<Long> ids, int state, String qvalue, int limit, int offset);
	
	/**
	 * 获取优惠券总数
	 * @param state
	 * @return
	 */
	int getCouponCount(List<Long> ids, int state, String qvalue);
	
	/**
	 * 添加一条优惠券记录
	 * @param coupon
	 * @return
	 */
	Coupon addCoupon(Coupon coupon);
	
	/**
	 * 更新优惠券
	 * @param coupon
	 * @return
	 */
	boolean updateCoupon(CouponDTO coupon);
	
	/**
	 * 获取指定code的优惠券
	 * @param couponCode
	 * @param isValidOnly
	 * @return
	 */
	Coupon getCouponByCode(String couponCode, boolean isValidOnly);
	
	/**
	 * 获取指定ID的优惠券
	 * @param id
	 * @param isValidOnly
	 * @return
	 */
	Coupon getCouponById(long id, boolean isValidOnly);

	/**
	 * 获取随机券
	 * @param rootCode 随机券母券
	 * @param noBinder 不为空，获取未被绑定的随机券
	 * @param count 一次性获取的总数
	 * @return
	 */
	List<Coupon> getRandomCoupons(String rootCode, int count);

	/**
	 * 优惠券撤销
	 * @param couponDTO
	 * @return
	 */
	boolean discardCoupon(CouponDTO couponDTO);

	/**
	 * 获取所
	 * @param couponCode
	 */
	int getObjectLockByCode(String couponCode);
	
	/**
	 * 删除随机券
	 * @param rootCode
	 * @return
	 */
	boolean deleteRamdomCoupon(String rootCode);
	
	/**
	 * 根据站点id，配置类型，获取优惠券配置
	 * @param siteId
	 * @param type
	 * @return
	 */
	public CouponConfigDTO getCouponConfigByType(long siteId, int type);

	/**
	 * 添加优惠券配置
	 * @param couponConfigDTO
	 * @return
	 */
	public int addCouponConfig(CouponConfigDTO couponConfigDTO);

	/**
	 * 更新优惠券配置
	 * @param couponConfigDTO
	 * @return
	 */
	public int updateCouponConfig(CouponConfigDTO couponConfigDTO);
}
