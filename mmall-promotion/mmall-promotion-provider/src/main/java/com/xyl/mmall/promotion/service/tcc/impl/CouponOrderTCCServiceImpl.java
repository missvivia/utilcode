/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.tcc.CouponOrderTCCDao;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.tcc.CouponOrderTCCService;

/**
 * CouponOrderServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("couponOrderTCCService")
public class CouponOrderTCCServiceImpl implements CouponOrderTCCService {

	@Autowired
	private CouponOrderTCCDao couponOrderTCCDao;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private UserCouponService userCouponService;

	@Override
	@Transaction
	public boolean confirmAddCouponOrderTCC(long tranId) {
		List<CouponOrderTCC> couponOrderTCCs = getCouponOrderTCCListByTranId(tranId);
		if (CollectionUtils.isEmpty(couponOrderTCCs)) {
			return true;
		}
		boolean ret = true;
		try {
		    int temp = 1;
			for (CouponOrderTCC couponOrderTCC : couponOrderTCCs) {
				ret = ret && couponOrderService.addCouponOrder(couponOrderTCC.cloneObject());

				// 非使用优惠券不处理
				if (couponOrderTCC.getCouponOrderType() != CouponOrderType.USE_COUPON) {
					continue;
				}
				
				//获取可用的用户优惠券
				UserCoupon uc = userCouponService.getUserCoupon(couponOrderTCC.getUserId(),
						couponOrderTCC.getUserCouponId(), ActivationConstants.STATE_CAN_USE, true);
				if (uc == null) {
					throw new ServiceException("用户优惠券不存在，userId:" + couponOrderTCC.getUserId() + ", code:"
							+ couponOrderTCC.getCouponCode());
				}
				
				//设置为已使用
				if(temp == couponOrderTCCs.size())
				{
				    uc.setState(ActivationConstants.STATE_HAS_BEAN_USED);
	                ret = ret && userCouponService.updateUserCoupon(uc);
				}

				if (!ret) {
					throw new ServiceException("添加订单优惠券异常，orderid:" + couponOrderTCC.getOrderId() + ", couponCode:"
							+ couponOrderTCC.getCouponCode());
				}
				temp++;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cancelAddCouponOrderTCC(tranId);
		}

		return ret;
	}

	@Override
	public boolean cancelAddCouponOrderTCC(long tranId) {
		List<CouponOrderTCC> list = couponOrderTCCDao.getCouponOrderTCCListByTranId(tranId);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		return deleteCouponOrderTCC(tranId);
	}

	@Override
	public boolean deleteCouponOrderTCC(long tranId) {
		List<CouponOrderTCC> list = getCouponOrderTCCListByTranId(tranId);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		return couponOrderTCCDao.deleteCouponOrderTCC(tranId);
	}

	@Override
	public CouponOrderTCC tryAddCouponOrderTCC(long tranId, CouponOrderTCC couponOrderTCC) {
		if (couponOrderTCC == null) {
			return null;
		}
		couponOrderTCC.setTranId(tranId);
		return couponOrderTCCDao.addCouponOrderTCC(couponOrderTCC);
	}

	@Override
	public List<CouponOrderTCC> getCouponOrderTCCListByTranId(long tranId) {
		return couponOrderTCCDao.getCouponOrderTCCListByTranId(tranId);
	}

}
