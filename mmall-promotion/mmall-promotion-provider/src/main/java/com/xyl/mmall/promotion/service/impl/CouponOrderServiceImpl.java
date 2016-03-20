/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.CouponOrderDao;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.CouponOrderDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.CodeType;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * CouponOrderServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("couponOrderService")
public class CouponOrderServiceImpl implements CouponOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(CouponOrderServiceImpl.class);

	@Autowired
	private CouponOrderDao couponOrderDao;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private CouponService couponService;

	@Override
	public boolean addCouponOrder(CouponOrder order) {
		return couponOrderDao.addCouponOrder(order) != null;
	}
	
	@Override
	@Transaction
	public boolean recycleCouponOrderList(List<? extends CouponOrder> couponOrders) {
		if (CollectionUtils.isEmpty(couponOrders)) {
			return true;
		}
		
		boolean isSucc = true;
		
		for (CouponOrder couponOrder : couponOrders) {
			System.out.println("#####ffgfggm#######"+couponOrder.getOrderId());
			isSucc = isSucc && recycleCouponOrder(couponOrder);
			if (!isSucc) {
				logger.error("try recycle coupon order error:"+couponOrder.getOrderId());
				throw new ServiceNoThrowException("回收异常");
			}
		}
		
		return isSucc;
	}
	
	@Override
	@Transaction
	public boolean recycleCouponOrder(CouponOrder couponOrder) {
		// 1.参数判断
		if (couponOrder == null) {
			return true;
		}

		// 2.判断优惠券是否需要回收
		// 使用优惠券，回收时使用次数加1(使用失败，需要将使用的次数添加回去)
		boolean isUse = couponOrder.getCouponOrderType() == CouponOrderType.USE_COUPON;

		
		// 判断UserCoupon是否回收(UserCoupon可能不存在)
		if (isUse) {
			// 只能处理初始状态
			if (couponOrder.getCouponHandlerType() != ActivationHandlerType.DEFAULT) {
				return true;
			}
		} else {
			//只处理已经发放的状态
			if (couponOrder.getCouponHandlerType() != ActivationHandlerType.GRANT) {
				return true;
			}
		}
		UserCoupon userCoupon = userCouponService.getUserCouponById(couponOrder.getUserId(), couponOrder.getUserCouponId());
		// 回收用户下的优惠券
		if (userCoupon == null) {
			return false;
		}
		
		//优惠券过期不处理
		if (!userCoupon.isValid()) {
			return true;
		}
		
		userCouponService.recycleUserCoupon(userCoupon, isUse);

		if (isUse) {
			couponOrder.setCouponHandlerType(ActivationHandlerType.CANCEL_RESET);
		} else {
			couponOrder.setCouponHandlerType(ActivationHandlerType.RECYCLE);
		}
		return updateCouponOrder(couponOrder);
	}

	@Override
	@Transaction
	public int doSuccOrderOfCouponReturn(List<CouponOrder> couponOrders) {
		if (CollectionUtils.isEmpty(couponOrders)) {
			return 2;
		}

		long orderId = 0;
		long userId = 0;

		boolean isSucc = true;
		for (CouponOrder couponOrder : couponOrders) {
			if (!isSucc)
				break;
			// 判断处理状态: 只能处理初始状态
			if (couponOrder.getCouponHandlerType() != ActivationHandlerType.DEFAULT) {
				continue;
			}
			// 读取优惠券信息
			Coupon coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
			if (coupon == null) {
				isSucc = false;
				break;
			}

			if (orderId <= 0) {
				orderId = couponOrder.getOrderId();
			}

			if (userId <= 0) {
				userId = couponOrder.getUserId();
			}

			Coupon c = null;
			if (coupon.getCodeType() == CodeType.RANDOM) {
				//rootcode为空，此券为母券,获取子券绑定到用户
				if (StringUtils.isBlank(coupon.getRootCode())) {
					List<Coupon> randomCoupons = couponService.getRandomCoupons(coupon.getCouponCode(), 1);
					if (CollectionUtils.isEmpty(randomCoupons)) {
						continue;
					}
					c = randomCoupons.get(0);
				} else {
					c = coupon;
				}
			} else {
				c = coupon;
			}

			// 添加一个用户优惠券
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setCouponCode(c.getCouponCode());
			userCoupon.setUserId(userId);
			userCoupon.setState(ActivationConstants.STATE_CAN_USE);
			// 设置有效期
			long interval = coupon.getEndTime() - coupon.getStartTime();
			if (interval > 0) {
				// 动态有效期设置
				long current = System.currentTimeMillis();
				// 有效时间当前时间加10天
				long startTime = current + 1000 * 60 * 60 * 24 * 10L;
				long endTime = startTime + interval;
				userCoupon.setValidStartTime(startTime);
				userCoupon.setValidEndTime(endTime);
			}

			isSucc = isSucc && userCouponService.addUserCoupon(userCoupon) != null;

			// 修改coupon order为已经处理
			couponOrder.setUserCouponId(userCoupon.getId());
			couponOrder.setCouponHandlerType(ActivationHandlerType.GRANT);
			isSucc = isSucc && updateCouponOrder(couponOrder);
		}

		if (!isSucc) {
			throw new ServiceException("orderId=" + orderId);
		}
		return 1;
	}

	@Override
	public boolean updateCouponOrder(CouponOrder couponOrder) {
		return couponOrderDao.updateCouponOrder(couponOrder);
	}

	@Override
	public CouponOrder getCouponOrderByOrderIdOfUseType(long userId, long orderId) {
		Map<CouponOrderType, List<CouponOrder>> map = getMapByOrderId(userId, orderId);
		return CollectionUtil.getFirstObjectOfList(CollectionUtil.getValueOfMap(map, CouponOrderType.USE_COUPON));
	}

	@Override
	public Map<CouponOrderType, List<CouponOrder>> getMapByOrderId(long userId, long orderId) {
		Map<CouponOrderType, List<CouponOrder>> map = couponOrderDao.getMapByOrderId(userId, orderId);
		return map;
	}

	@Override
	public List<CouponOrderDTO> getListByOrderId(long userId, long orderId) {
		Map<CouponOrderType, List<CouponOrder>> map = couponOrderDao.getMapByOrderId(userId, orderId);
		List<CouponOrderDTO> retList = new ArrayList<>();
		for (CouponOrderType type : CouponOrderType.values()) {
			List<CouponOrder> coList = CollectionUtil.getValueOfMap(map, type);
			if (CollectionUtil.isEmptyOfCollection(coList))
				continue;
			// 设置Promotion字段
			for (CouponOrder co : coList) {
				String couponCode = co.getCouponCode();
				Coupon coupon = couponService.getCouponByCode(couponCode, false);
				if (coupon == null)
					continue;
				CouponOrderDTO couponOrderDTO = new CouponOrderDTO(co);
				couponOrderDTO.setCouponDTO(new CouponDTO(coupon));
				retList.add(couponOrderDTO);
			}
		}
		return retList;
	}

	@Override
	public List<CouponOrder> getCouponOrderByCodeOfUseType(long userId, String couponCode,
			CouponOrderType couponOrderType) {
		return couponOrderDao.getCouponOrderByCodeOfUseType(userId, couponCode, couponOrderType);
	}

	@Override
	public List<CouponOrder> getCouponOrderByUserId(long userId) {
		return couponOrderDao.getCouponOrderByUserId(userId);
	}

	@Override
	public List<CouponOrder> getCouponOrderByUserAndCouponId(long userId, long usercouponId) {
		return couponOrderDao.getCouponOrderByUserAndCouponId(userId, usercouponId);
	}

	@Override
	public List<CouponOrder> getCouponOrdersByUserIdAndOrderIds(long userId,
			List<Long> orderIds) {
		return couponOrderDao.getCouponOrdersByUserIdAndOrderIds(userId, orderIds);
	}
}
