/*
 * @(#) 2014-10-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.RebateService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.tcc.CouponOrderTCCService;
import com.xyl.mmall.promotion.service.tcc.RedPacketOrderTCCService;

/**
 * RebateTCCServiceImpl.java 返券和返红包处理
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-10
 * @since 1.0
 */
@Service("rebateService")
public class RebateServiceImpl implements RebateService {
	@Autowired
	private CouponOrderTCCService couponOrderTCCService;

	@Autowired
	private RedPacketOrderTCCService redPacketOrderTCCService;

	@Autowired
	private CouponOrderService couponOrderService;

	@Autowired
	private RedPacketOrderService redPacketOrderService;
	@Autowired
	private RedPacketService redPacketService;
	

	@Override
	@Transaction
	public boolean rebate(long userId, long orderId) {
		boolean isSucc = true;
		// 返优惠券
		Map<CouponOrderType, List<CouponOrder>> map = couponOrderService.getMapByOrderId(userId, orderId);
		if (!CollectionUtils.isEmpty(map)) {
			// 获取返券的优惠券订单
			List<CouponOrder> couponOrders = map.get(CouponOrderType.RETURN_COUPON);
			isSucc = isSucc && couponOrderService.doSuccOrderOfCouponReturn(couponOrders) > 0;
		}
		
		// 返红包
		List<RedPacketOrder> redPacketOrderList = redPacketOrderService.getRedPacketOrderList(userId, orderId);
		if (!CollectionUtils.isEmpty(redPacketOrderList)) {
			isSucc = isSucc && redPacketOrderService.doSuccOrderOfRedPacketReturn(redPacketOrderList) > 0;
		}
		return isSucc;
	}

	@Override
	public RetArg rebateAndReturnObjcet(long userId, long orderId) {
		boolean isSucc = rebate(userId, orderId);
		CouponOrder couponOrder = null;
		RedPacket redPacket = null;

		Map<CouponOrderType, List<CouponOrder>> map = couponOrderService.getMapByOrderId(userId, orderId);
		if (!CollectionUtils.isEmpty(map)) {
			List<CouponOrder> couponOrders = map.get(CouponOrderType.RETURN_COUPON);
			if (couponOrders != null) {
				for (CouponOrder coupon : couponOrders) {
					if (coupon.getCouponOrderType() == CouponOrderType.RETURN_COUPON) {
						couponOrder = coupon;
						break;
					}
				}
			}
		}
		List<RedPacketOrder> redPacketOrderList = redPacketOrderService.getRedPacketOrderList(userId, orderId);
		if (redPacketOrderList != null) {
			for (RedPacketOrder redpacket : redPacketOrderList) {
				if (redpacket.getRedPacketOrderType() == RedPacketOrderType.RETURN_RED_PACKET) {
					redPacket = redPacketService.getRedPacketById(redpacket.getRedPacketId());
					break;
				}
			}
		}
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, isSucc);
		if (!isSucc)
			return retArg;
		RetArgUtil.put(retArg, couponOrder);
		RetArgUtil.put(retArg, redPacket);
		return retArg;
	}
}
