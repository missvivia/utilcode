/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.cms.vo.AreaVO;
import com.xyl.mmall.cms.vo.CouponConfigVO;
import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public interface CouponFacade {
	/**
	 * 获取优惠券列表
	 * @param state
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<CouponDTO> getCouponList(long userId, int state, String qvalue, int limit, int offset);
	
	/**
	 * 获取优惠券总数
	 * @param state
	 * @return
	 */
	int getCouponCount(long userId, int state, String qvalue);
	
	/**
	 * 添加一条优惠券记录
	 * @param coupon
	 * @return
	 */
	CouponDTO addCoupon(CouponDTO couponDTO);
	
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
	CouponDTO getCouponByCode(String couponCode, boolean isValidOnly);
	
	/**
	 * 获取指定ID的优惠券
	 * @param id
	 * @param isValidOnly
	 * @return
	 */
	CouponDTO getCouponById(long id, boolean isValidOnly);

	/**
	 * 获取随机券列表
	 * @param couponCode
	 * @return
	 */
	List<Coupon> getRamdomCoupons(String couponCode);
	
	/**
	 * 撤销优惠券
	 * @param couponDTO
	 * @return
	 */
	boolean discardCoupon(CouponDTO couponDTO);
	
	/**
	 * 获取可选区域列表
	 * @param userId
	 * @return
	 */
	public List<AreaVO> getAreaList(long userId);
	
	/**
	 * 根据站点id，配置类型，获取优惠券配置
	 * @param siteId
	 * @param type
	 * @return
	 */
	public CouponConfigVO getCouponConfigByType(long siteId, int type);
	
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
	
	/**
	 * 获取有用的优惠券
	 * @param couponCodes
	 * @param couponList
	 * @return
	 */
	public Map<String, String> getUseFulCouponList(String couponCodes, List<CouponDTO> couponList);
}
