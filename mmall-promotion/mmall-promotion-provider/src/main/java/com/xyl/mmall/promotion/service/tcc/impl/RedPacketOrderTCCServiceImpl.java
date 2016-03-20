/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.aop.ActionSerialization;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.tcc.RedPacketOrderTCCDao;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.service.tcc.RedPacketOrderTCCService;

/**
 * RedPacketOrderTCCServiceImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
@Service("redPacketOrderTCCService")
public class RedPacketOrderTCCServiceImpl implements RedPacketOrderTCCService {
	
	@Autowired
	private RedPacketOrderTCCDao redPacketOrderTCCDao;
	
	@Autowired
	private RedPacketOrderService redPacketOrderService;
	
	@Autowired
	private UserRedPacketService userRedPacketService;
	
	@Override
	public RedPacketOrderTCC tryAddRedPacketOrderTCC(long tranId, RedPacketOrderTCC redPacketOrderTCC) {
		if (redPacketOrderTCC == null) {
			return null;
		}
		
		redPacketOrderTCC.setTranId(tranId);
		redPacketOrderTCC.setUsedTime(System.currentTimeMillis());
		redPacketOrderTCC.setCtimeOfTCC(System.currentTimeMillis());
		return redPacketOrderTCCDao.addRedPacketOrderTCC(redPacketOrderTCC);
	}

	@Override
	public List<RedPacketOrderTCC> getRedPacketOrderTCCListByTranId(long tranId) {
		return redPacketOrderTCCDao.getRedPacketOrderTCCListByTranId(tranId);
	}

	@Override
	public boolean confirmAddRedPacketOrderTCC(long tranId) {
		List<RedPacketOrderTCC> redPacketOrderTCCs = getRedPacketOrderTCCListByTranId(tranId);
		if (CollectionUtils.isEmpty(redPacketOrderTCCs)) {
			return true;
		}
		
		long userId = redPacketOrderTCCs.get(0).getUserId();
		
		boolean ret = handleRedPacketOrderTCCList(redPacketOrderTCCs, new PromotionLock(userId));
		
		cancelAddRedPacketOrderTCC(tranId);
		
		return ret;
	}

	@ActionSerialization
	@Transaction
	private boolean handleRedPacketOrderTCCList(List<RedPacketOrderTCC> redPacketOrderTCCs, PromotionLock promotionLock) {
		boolean ret = true;
		
		for (RedPacketOrderTCC redPacketOrderTCC : redPacketOrderTCCs) {
			//添加到红包订单列表
			ret = ret && redPacketOrderService.addRedPacketOrder(redPacketOrderTCC.cloneObject());
			//非使用红包不处理
			if (redPacketOrderTCC.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET
					&& redPacketOrderTCC.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS) {
				continue;
			}
			
			//使用红包时对红包进行处理
			long userRedPacketId = redPacketOrderTCC.getUserRedPacketId();
			long userId = redPacketOrderTCC.getUserId();
			UserRedPacket userRedPacket = userRedPacketService.getUserRedPacketById(userRedPacketId, userId);
			if (userRedPacket == null) {
				throw new ServiceNoThrowException("指定用户红包不存在，id:" + userRedPacketId);
			}
			
			if (userRedPacket.getRemainCash().compareTo(redPacketOrderTCC.getCash()) > 0) {
				//设置使用红包金额
				userRedPacket.setUsedCash(userRedPacket.getUsedCash().add(redPacketOrderTCC.getCash()));
				//设置剩余金额
				userRedPacket.setRemainCash(userRedPacket.getRemainCash().subtract(redPacketOrderTCC.getCash()));
			} else if (userRedPacket.getRemainCash().compareTo(redPacketOrderTCC.getCash()) == 0) {
				userRedPacket.setUsedCash(userRedPacket.getUsedCash().add(redPacketOrderTCC.getCash()));
				userRedPacket.setRemainCash(BigDecimal.ZERO);
				userRedPacket.setState(ActivationConstants.STATE_HAS_BEAN_USED);
			} else {
				throw new ServiceNoThrowException("指定用户红包金额不足，id:" + userRedPacketId);
			}
			
			ret = ret && userRedPacketService.updateUserRedPacket(userRedPacket);
			
			if (!ret) {
				throw new ServiceNoThrowException("添加订单红包异常，orderid:" + redPacketOrderTCC.getOrderId());
			}
		}
		return ret;
	}

	@Override
	public boolean cancelAddRedPacketOrderTCC(long tranId) {

		List<RedPacketOrderTCC> list = getRedPacketOrderTCCListByTranId(tranId);

		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		return deleteRedPacketOrderTCC(tranId);
	}
	
	@Override
	public boolean deleteRedPacketOrderTCC(long tranId) {
		return redPacketOrderTCCDao.deleteRedPacketOrderTCC(tranId);
	}

}
