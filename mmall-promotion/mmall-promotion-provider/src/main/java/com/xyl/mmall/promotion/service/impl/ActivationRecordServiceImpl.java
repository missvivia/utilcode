/*
 * @(#) 2015-1-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.promotion.aop.ActionSerialization;
import com.xyl.mmall.promotion.dao.ActivationRecordDao;
import com.xyl.mmall.promotion.meta.ActivationRecord;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.service.ActivationRecordService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * ActivationRecordServiceImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-1-23
 * @since      1.0
 */
@Service("activationRecordService")
public class ActivationRecordServiceImpl implements ActivationRecordService {
	
	@Autowired
	private ActivationRecordDao activationRecordDao;
	
	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private CouponService couponService;

	@Override
	public ActivationRecord getActivationRecordByUserIdAndState(long userId, boolean free) {
		return activationRecordDao.getActivationRecordByUserIdAndState(userId, free);
	}

	@Override
	@ActionSerialization
	@Transaction
	public Map<String, Object> dispatchCouponCode(long userId, boolean free, String couponCode, String couponCodes, PromotionLock lock) {
		boolean status = true;
		Map<String, Object> map = new HashMap<>();
		
		ActivationRecord activationRecord = getActivationRecordByUserIdAndState(userId, free);
		if (activationRecord != null) {
			map.put("state", 1);
			map.put("msg", "领取成功");
			return map;
		} 
		
		activationRecord = new ActivationRecord();
		activationRecord.setFree(free);
		activationRecord.setUserId(userId);
		
		//免单
		if (free) {
			Coupon coupon = couponService.getCouponByCode(couponCode, false);
			if (coupon == null) {
				map.put("state", -3);
				map.put("msg", "优惠券不存在!");
				return map;
			}
			
			int count = userCouponService.getUserCouponCountByCode(userId, couponCode);
			if (count > 0) {
				map.put("state", 1);
				map.put("msg", "领取成功");
				return map;
			}
			
			status = status && userCouponService.bindUserCouponWithFixDay(userId, couponCode, 0, false);
			
			if (!status) {
				map.put("state", -8);
				map.put("msg", "添加优惠券异常!");
				return map;
			}
			activationRecord.setCouponCode(couponCode);
		} else {
			activationRecord.setCouponCode(couponCodes);
			if (StringUtils.isNotBlank(couponCodes)) {
				String[] couponList = couponCodes.split(",");
				if (couponList != null && couponList.length > 0) {
					for (String coupon : couponList) {
						if (StringUtils.isBlank(coupon)) {
							continue;
						}
						Coupon c = couponService.getCouponByCode(coupon, false);
						if (c == null) {
							continue;
						}
						
						int count = userCouponService.getUserCouponCountByCode(userId, couponCode);
						if (count > 0) {
							map.put("state", 1);
							map.put("msg", "领取成功");
							return map;
						}
						
						status = userCouponService.bindUserCouponWithFixDay(userId, coupon, 0, false);
						
						if (!status) {
							map.put("state", -8);
							map.put("msg", "添加优惠券异常!");
							return map;
						}
					}
				}
			}
		}
		
		activationRecordDao.addObject(activationRecord);
		map.put("state", 1);
		map.put("msg", "领取成功");
		return map;
	}
	
}
