/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.promotion.CouponFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.UserCouponFacade;
import com.xyl.mmall.mainsite.vo.CouponVO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * UserCouponFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */

@Facade
public class UserCouponFacadeImpl implements UserCouponFacade {
	
	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private CouponFacade couponFacade;

	@Override
	public List<CouponVO> getUserCouponList(long userId, int state,
			int limit, int offset) {
		List<CouponDTO> coupons = userCouponService.getUserCouponListByState(userId, state, limit, offset);
		List<CouponVO> couponVOs = assembleUserCouponDTO(coupons);
		
		return couponVOs;
	}

	@Override
	public int getUserCouponCount(long userId, int state) {
		return userCouponService.getUserCouponCount(userId, state);
	}
	
	@Override
	public List<CouponVO> getUserCouponList(long userId, long timestamp, int count) {
		List<CouponDTO> coupons = userCouponService.getUserCouponList(userId, timestamp, count);
		List<CouponVO> couponVOs = assembleUserCouponDTO(coupons);
		
		return couponVOs;
		
	}

	private List<CouponVO> assembleUserCouponDTO(List<CouponDTO> coupons) {
		if (CollectionUtils.isEmpty(coupons)) {
			return null;
		}
		
		List<CouponVO> couponVOs = new ArrayList<>(coupons.size());
		for (CouponDTO coupon : coupons) {
			couponVOs.add(new CouponVO(coupon));
		}
		return couponVOs;
	}

	@Override
	public boolean bindUserCouponWithFixDay(long userId, String couponCode, int fixDay, boolean useCouponValidity) {
		return userCouponService.bindUserCouponWithFixDay(userId, couponCode, fixDay, useCouponValidity);
	}
}
