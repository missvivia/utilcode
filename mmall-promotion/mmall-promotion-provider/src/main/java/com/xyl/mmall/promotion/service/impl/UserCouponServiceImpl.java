/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.UserCouponDao;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * UserCouponServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("userCouponService")
public class UserCouponServiceImpl implements UserCouponService {

	@Autowired
	private UserCouponDao userCouponDao;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Override
	public List<CouponDTO> getUserCouponListByState(long userId, int state, int limit, int offset) {
		List<UserCoupon> userCoupons = userCouponDao.getUserCouponListByState(userId, state, limit, offset);
		List<CouponDTO> coupons = transferUserCoupons(userCoupons);

		return coupons;
	}

	private List<CouponDTO> transferUserCoupons(List<UserCoupon> userCoupons) {
		if (CollectionUtils.isEmpty(userCoupons)) {
			return null;
		}

		List<CouponDTO> coupons = new ArrayList<>(userCoupons.size());
		for (UserCoupon userCoupon : userCoupons) {
			long userId = userCoupon.getUserId();
			Coupon coupon = couponService.getCouponByCode(userCoupon.getCouponCode(), false);
			if (coupon == null) {
				continue;
			}

			// 设置我的优惠券时间
			coupon.setStartTime(userCoupon.getValidStartTime());
			coupon.setEndTime(userCoupon.getValidEndTime());

			CouponDTO couponDTO = new CouponDTO(coupon);
			// 设置用户的优惠券id
			couponDTO.setUserCouponId(userCoupon.getId());
			couponDTO.setCouponState(userCoupon.getState());

			if (userCoupon.getState() == ActivationConstants.STATE_HAS_BEAN_USED) {
				// 有效的看看是否使用过
				List<CouponOrder> orders = couponOrderService.getCouponOrderByUserAndCouponId(userId,
						userCoupon.getId());
				CouponOrder order = filterUsedCouponOrder(orders);
				// 没有交易订单，未使用过
				if (order == null) {
					if (!userCoupon.isValid()) {
						if (userCoupon.getValidStartTime() > System.currentTimeMillis()) {
							couponDTO.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
						} else {
							couponDTO.setCouponState(ActivationConstants.STATE_EXPIRED);
						}
					}
					coupons.add(couponDTO);
					continue;
				}

				// 已经使用
				couponDTO.getOrderSerial().add(String.valueOf(order.getOrderId()));
				coupons.add(couponDTO);
				continue;
			}

			// 非有效情况
			if (!userCoupon.isValid()) {
				if (userCoupon.getValidStartTime() > System.currentTimeMillis()) {
					// 优惠券可用设置成未生效，其他状态不做处理
					if (userCoupon.getState() == ActivationConstants.STATE_CAN_USE) {
						couponDTO.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
					}
				} else {
					couponDTO.setCouponState(ActivationConstants.STATE_EXPIRED);
				}
				coupons.add(couponDTO);
				continue;
			}

			coupons.add(couponDTO);
		}
		return coupons;
	}

	private CouponOrder filterUsedCouponOrder(List<CouponOrder> orders) {
		if (CollectionUtils.isEmpty(orders)) {
			return null;
		}

		for (CouponOrder order : orders) {
			if (order == null) {
				continue;
			}
			if (order.getCouponOrderType() == CouponOrderType.USE_COUPON
					&& order.getCouponHandlerType() == ActivationHandlerType.DEFAULT) {
				return order;
			}
		}

		return null;
	}

	@Override
	public int getUserCouponCount(long userId, int state) {
		return userCouponDao.getUserCouponCount(userId, state);
	}

	@Override
	public UserCoupon addUserCoupon(UserCoupon userCoupon) {
		return userCouponDao.addUserCoupon(userCoupon);
	}

	@Override
	public UserCoupon getUserCoupon(long userId, long userCouponId, int state, boolean isValid) {
		return getUserCouponsByState(userId, userCouponId, state, isValid ? isValid : null);
	}
	
    public CouponDTO getUserCouponDTO(long userId, long userCouponId, int state, boolean isValid)
    {
        UserCoupon userCoupon = getUserCouponsByState(userId, userCouponId, state,
                isValid ? isValid : null);
        if (userCoupon == null)
        {
            return null;
        }
        List<UserCoupon> list = new ArrayList<UserCoupon>();
        list.add(userCoupon);
        List<CouponDTO> result = transferUserCoupons(list);
        if (result.size() > 0)
        {
            return result.get(0);
        }
        else
        {
            return null;
        }
    }

	@Override
	public boolean updateUserCoupon(UserCoupon userCoupon) {
		return userCouponDao.updateUserCoupon(userCoupon);
	}

	@Override
	public boolean recycleUserCoupon(UserCoupon userCoupon, boolean isUse) {
		// 查找状态时可使用的用户优惠券
		if (userCoupon == null) {
			return false;
		}
		if (isUse) {
			// 使用的回收，变成可用
			userCoupon.setState(ActivationConstants.STATE_CAN_USE);
		} else {
			// 返券的回收变成已经失效
			userCoupon.setState(ActivationConstants.STATE_INACTIVE);
		}

		return updateUserCoupon(userCoupon);
	}

	@Override
	public List<CouponDTO> getUserCouponList(long userId, long timestamp, int count) {
		List<UserCoupon> userCoupons = userCouponDao.getUserCouponList(userId, timestamp, count);
		List<CouponDTO> coupons = transferUserCoupons(userCoupons);

		return coupons;
	}

	@Override
	public int getUserCouponCountByCode(String couponCode) {
		return userCouponDao.getUserCouponCountByCode(couponCode);
	}

	@Override
	public boolean refundCompensateUserCoupon(long userId, long orderId, String couponCode) {
		boolean succ = true;
		if (StringUtils.isNotBlank(couponCode)) {
			succ = succ && bindCouponToAssignUser(userId, couponCode, 30, false);
		}

		if (!succ) {
			return succ;
		}

		// 添加一个couponorder记录
		CouponOrder couponOrder = new CouponOrder();
		couponOrder.setOrderId(orderId);
		couponOrder.setCouponCode(couponCode);
		couponOrder.setCouponHandlerType(ActivationHandlerType.GRANT);
		couponOrder.setUserId(userId);
		couponOrder.setCouponOrderType(CouponOrderType.RETURN_COUPON);
		succ = succ && couponOrderService.addCouponOrder(couponOrder);

		return succ;
	}

	private boolean bindCouponToAssignUser(long userId, String couponCode, int fixDay, boolean useCouponValidity) {
		// 获取指定的优惠券
		boolean succ = true;
		Coupon coupon = couponService.getCouponByCode(couponCode, false);
		if (coupon != null) {
			int count = getUserCouponCountByCode(userId, couponCode);
			if (count >= coupon.getTimes()) {
				return true;
			}
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setCouponCode(couponCode);
			userCoupon.setUserId(userId);
			if (useCouponValidity) {
				userCoupon.setValidStartTime(coupon.getStartTime());
				userCoupon.setValidEndTime(coupon.getEndTime());
			} else {
				long current = System.currentTimeMillis();
				long interval = 0;
				if (fixDay > 0) {
					interval = 1000L * 60 * 60 * 24 * fixDay;
				} else {
					interval = coupon.getEndTime() - coupon.getStartTime();
				}
				userCoupon.setValidStartTime(current);
				userCoupon.setValidEndTime(current + interval);
			}
			
			succ = succ && addUserCoupon(userCoupon) != null;
		}
		return succ;
	}

	@Override
	public UserCoupon getUserCouponsByState(long userId, long userCouponId, int state, Boolean isValid) {
		return userCouponDao.getUserCouponsByState(userId, userCouponId, state, isValid);
	}

	@Override
	public int getUserCouponCountByCode(long userId, String couponCode) {
		return getUserCouponCountByCode(userId, couponCode, null, null);
	}
	
	@Override
	public int getUserCouponCountByCode(long userId, String couponCode, Date startDate, Date endDate) {
		return userCouponDao.getUserCouponCountByCode(userId, couponCode, startDate, endDate);
	}
	
	@Override
	public boolean deleteUserCoupon(long userId, String couponCode) {
		return userCouponDao.deleteUserCoupon(userId, couponCode);
	}

	@Override
	public UserCoupon getUserCouponById(long userId, long userCouponId) {
		return userCouponDao.getObjectByIdAndUserId(userCouponId, userId);
	}

	@Override
	public int getUserCouponCountBycreateTime(long userId, long time) {
		return userCouponDao.getUserCouponCountBycreateTime(userId, time);
	}

	@Override
	public boolean bindUserCouponWithFixDay(long userId, String couponCode, int fixDay, boolean useCouponValidity) {
		return bindCouponToAssignUser(userId, couponCode, fixDay, useCouponValidity);
	}

	@Override
	@Transaction
	public boolean batchBindUserCouponWithFixDay(long userId, List<String> couponCodeList, int fixDay,
			boolean useCouponValidity) {
		if (CollectionUtils.isEmpty(couponCodeList)) {
			return false;
		}
		
		boolean isSucc = true;
		
		for (String couponCode : couponCodeList) {
			isSucc = isSucc && bindUserCouponWithFixDay(userId, couponCode, fixDay, useCouponValidity);
			if (!isSucc) {
				throw new ServiceNoThrowException("绑定异常");
			}
		}
		
		return isSucc;
	}

	@Override
	public boolean batchUpdateBindUserCouponByCode(String couponCode) {
		return userCouponDao.batchUpdateBindUserCouponByCode(couponCode);
	}

	@Override
	public boolean batchDeleteBindUserCouponByCode(String couponCode) {
		return userCouponDao.batchDeleteBindUserCouponByCode(couponCode);
	}
}
