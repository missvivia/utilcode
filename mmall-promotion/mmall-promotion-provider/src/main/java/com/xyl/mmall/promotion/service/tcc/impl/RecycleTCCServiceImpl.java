/*
 * @(#) 2014-10-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.dto.CouponOrderDTO;
import com.xyl.mmall.promotion.dto.TCCParamDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.tcc.CouponOrderTCCService;
import com.xyl.mmall.promotion.service.tcc.RecycleTCCService;
import com.xyl.mmall.promotion.service.tcc.RedPacketOrderTCCService;

/**
 * RecycleTCCServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-10
 * @since 1.0
 */
@Service("recycleTCCService")
public class RecycleTCCServiceImpl implements RecycleTCCService {

	private static final Logger logger = LoggerFactory.getLogger(RecycleTCCServiceImpl.class);

	@Autowired
	private CouponOrderTCCService couponOrderTCCService;

	@Autowired
	private RedPacketOrderTCCService redPacketOrderTCCService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Autowired
	private CouponService couponService;

	@Override
	@Transaction
	public boolean tryAddRecycleTCC(long tranId, TCCParamDTO tccParamDTO) {
		if (tccParamDTO == null) {
			return false;
		}

		long orderId = tccParamDTO.getOrderId();
		long userId = tccParamDTO.getUserId();

		boolean isSucc = true;
		List<CouponOrderDTO> couponOrders = null;
		// 构建优惠券回收tcc
		if (CollectionUtil.isNotEmptyOfList(tccParamDTO.getOrderIdList())) {
			couponOrders = new ArrayList<CouponOrderDTO>();
			for (Long orderId2 : tccParamDTO.getOrderIdList()) {
				couponOrders.addAll(couponOrderService.getListByOrderId(userId, orderId2));
			}
		} else {
			couponOrders = couponOrderService.getListByOrderId(userId, orderId);
		}
		if (!CollectionUtils.isEmpty(couponOrders)) {
			for (CouponOrderDTO orderDTO : couponOrders) {
				// 优惠券被撤销的不回收
				Coupon coupon = couponService.getCouponByCode(orderDTO.getCouponCode(), true);
				if (coupon == null) {
					continue;
				}
				CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
				couponOrderTCC.setOrderId(orderDTO.getOrderId());
				couponOrderTCC.setUserId(userId);
				couponOrderTCC.setCouponCode(orderDTO.getCouponCode());
				couponOrderTCC.setUserCouponId(orderDTO.getUserCouponId());
				couponOrderTCC.setCouponHandlerType(orderDTO.getCouponHandlerType());
				couponOrderTCC.setCouponOrderType(orderDTO.getCouponOrderType());
				isSucc = isSucc && couponOrderTCCService.tryAddCouponOrderTCC(tranId, couponOrderTCC) != null;
				if (!isSucc) {
					String errorOrderId = tccParamDTO.getOrderIdList() != null ? tccParamDTO.getOrderIdList()
							.toString() : orderId + "";
					logger.error("try recycle coupon tcc error:" + errorOrderId);
					throw new ServiceException("添加TCC异常");
				}
			}
		}

		// 构建红包回收tcc
		/*
		 * List<RedPacketOrder> list =
		 * redPacketOrderService.getRedPacketOrderList(userId, orderId); if
		 * (!CollectionUtils.isEmpty(list)) { for (RedPacketOrder redPacketOrder
		 * : list) { RedPacketOrderTCC redPacketOrderTCC = new
		 * RedPacketOrderTCC(); redPacketOrderTCC.setOrderId(orderId);
		 * redPacketOrderTCC.setUserId(userId);
		 * redPacketOrderTCC.setRedPacketHandlerType
		 * (redPacketOrder.getRedPacketHandlerType());
		 * redPacketOrderTCC.setRedPacketOrderType
		 * (redPacketOrder.getRedPacketOrderType());
		 * redPacketOrderTCC.setCash(redPacketOrder.getCash());
		 * redPacketOrderTCC.setRedPacketId(redPacketOrder.getRedPacketId());
		 * redPacketOrderTCC
		 * .setUserRedPacketId(redPacketOrder.getUserRedPacketId());
		 * 
		 * isSucc = isSucc &&
		 * redPacketOrderTCCService.tryAddRedPacketOrderTCC(tranId,
		 * redPacketOrderTCC) != null; if (!isSucc) { throw new
		 * ServiceException("添加TCC异常"); } } }
		 */

		return isSucc;

	}

	@Override
	@Transaction
	public boolean confirmAddRecycleTCC(long tranId) {
		List<CouponOrderTCC> couponOrderTCCList = couponOrderTCCService.getCouponOrderTCCListByTranId(tranId);
		// List<RedPacketOrderTCC> redPacketOrderTCCs =
		// redPacketOrderTCCService.getRedPacketOrderTCCListByTranId(tranId);
		boolean isSucc = true;
		Set<Long> orderIdSet = new HashSet<Long>();
		try {
			// 回收优惠券
			if (!CollectionUtils.isEmpty(couponOrderTCCList)) {
				for (CouponOrderTCC couponOrderTCC : couponOrderTCCList) {
					orderIdSet.add(couponOrderTCC.getOrderId());
				}
				List<CouponOrder> couponOrders = couponOrderService.getCouponOrdersByUserIdAndOrderIds(
						couponOrderTCCList.get(0).getUserId(), new ArrayList<Long>(orderIdSet));
				isSucc = couponOrderService.recycleCouponOrderList(couponOrders);
			}

			// 回收红包
			/*
			 * if (!CollectionUtils.isEmpty(redPacketOrderTCCs)) { for
			 * (RedPacketOrderTCC redPacketOrderTCC : redPacketOrderTCCs) {
			 * isSucc = isSucc &&
			 * redPacketOrderService.recycleRedPacketOrder(redPacketOrderTCC,
			 * new PromotionLock( redPacketOrderTCC.getUserId())); } }
			 */
		} finally {
			isSucc = isSucc && cancelAddRecycleTCC(tranId);
		}

		return isSucc;
	}

	@Override
	public boolean cancelAddRecycleTCC(long tranId) {
		// 优惠券订单确定
		boolean status = couponOrderTCCService.cancelAddCouponOrderTCC(tranId);

		if (!status) {
			throw new ServiceNoThrowException("删除CouponOrderTCC异常");
		}

		// 红包确认
		// status = redPacketOrderTCCService.cancelAddRedPacketOrderTCC(tranId);
		//
		// if (!status) {
		// throw new ServiceNoThrowException("删除RedPacketOrderTCC异常");
		// }

		return status;
	}

}
