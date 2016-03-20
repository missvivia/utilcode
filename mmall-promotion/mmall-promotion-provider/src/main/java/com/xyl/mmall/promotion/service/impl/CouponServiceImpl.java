/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.dao.CouponConfigDao;
import com.xyl.mmall.promotion.dao.CouponDao;
import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * CouponServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("couponService")
public class CouponServiceImpl implements CouponService {
	
	private static Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

	@Autowired
	private CouponDao couponDao;

	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private CouponConfigDao couponConfigDao;
	
	@Override
	public List<Coupon> getCouponList(List<Long> ids, int state, String qvalue, int limit, int offset) {
		return couponDao.getCouponList(ids, state, qvalue, limit, offset);
	}

	@Override
	public int getCouponCount(List<Long> ids, int state, String qvalue) {
		return couponDao.getCouponCount(ids, state, qvalue);
	}

	@Override
	public Coupon addCoupon(Coupon coupon) {
		try {
			Coupon c = getCouponByCode(coupon.getCouponCode(), false);
			if (c != null) {
				return null;
			}
			coupon = couponDao.addCoupon(coupon);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return coupon;
	}

	@Override
	@Transaction
	public boolean updateCoupon(CouponDTO couponDTO) {
		if (couponDTO == null) {
			return false;
		}
		
		if (!couponDao.updateCoupon(couponDTO)) {
			throw new ServiceException("优惠券更新异常");
		}
		
		return true;
	}

	@Override
	public Coupon getCouponByCode(String couponCode, boolean isValidOnly) {
		return couponDao.getCouponByCode(couponCode, isValidOnly);
	}

	@Override
	public Coupon getCouponById(long id, boolean isValidOnly) {
		return couponDao.getCouponById(id, isValidOnly);
	}

	@Override
	public List<Coupon> getRandomCoupons(String rootCode, int count) {
		return couponDao.getRandomCoupons(rootCode, count);
	}

	@Override
	@Transaction
	public boolean discardCoupon(CouponDTO couponDTO) {
		if (!updateCoupon(couponDTO)) {
			return false;
		}
		
		//删除母券对应的随机券
		deleteRamdomCoupon(couponDTO.getCouponCode());
		
		if (BinderType.USER_BINDER == couponDTO.getBinderType()) {
			List<Long> ids = couponDTO.getBinderUserList();
			if (CollectionUtils.isEmpty(ids)) {
				return true;
			}
			for (long id : ids) {
				userCouponService.deleteUserCoupon(id, couponDTO.getCouponCode());
			}
			return true;
		} else {
			if (!userCouponService.batchDeleteBindUserCouponByCode(couponDTO.getCouponCode())) {
				throw new ServiceException("Discard coupon error! Update bindusercoupon failed!");
			}
		}
		return true;
	}

	@Override
	public int getObjectLockByCode(String couponCode) {
		return couponDao.getObjectLockByCode(couponCode);
	}

	@Override
	public boolean deleteRamdomCoupon(String rootCode) {
		return couponDao.deleteRamdomCoupon(rootCode);
	}

	@Override
	public CouponConfigDTO getCouponConfigByType(long siteId, int type) {
		return new CouponConfigDTO(couponConfigDao.getCouponConfigByType(siteId, type));
	}

	@Override
	public int addCouponConfig(CouponConfigDTO couponConfigDTO) {
		int res = 0;
		try {
			res = couponConfigDao.addObject(couponConfigDTO) == null ? 0 : 1;
		} catch (DBSupportRuntimeException e) {
			res = -1;
			logger.error("Add couponConfig error! siteId : " + couponConfigDTO.getSiteId() 
					+ ", type : " + couponConfigDTO.getType() + ".", e);
		}
		return res;
	}

	@Override
	public int updateCouponConfig(CouponConfigDTO couponConfigDTO) {
		return couponConfigDao.updateCouponConfig(couponConfigDTO);
	}
}
