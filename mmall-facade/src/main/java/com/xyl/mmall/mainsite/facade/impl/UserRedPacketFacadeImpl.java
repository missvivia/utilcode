/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.UserRedPacketFacade;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.utils.DateUtils;

/**
 * UserRedPacketFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
@Facade
public class UserRedPacketFacadeImpl implements UserRedPacketFacade {
	
	@Autowired
	private UserRedPacketService userRedPacketService;
	
	@Autowired
	private RedPacketOrderService redPacketOrderService;
	
	@Autowired
	private OrderService orderService;

	@Override
	public List<UserRedPacketDTO> getUserRedPacketList(long userId, int state, int limit, int offset) {
		List<UserRedPacketDTO> list = userRedPacketService.getUserRedPacketList(userId, state, limit, offset);
		return list;
	}

	@Override
	public List<UserRedPacketDTO> getUserRedPacketList(long userId, long timestamp, int count) {
		return userRedPacketService.getUserRedPacketList(userId, timestamp, count);
	}

	@Override
	public boolean refundUserRedpackets(BigDecimal cash, long userId, long orderId, long packageId) {
		return userRedPacketService.refundUserRedpackets(cash, userId, orderId, packageId);
	}

	@Override
	public void modifyRpValidity() {
		long current = DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT);
		
		long minId = 0;
		DDBParam param = DDBParam.genParam500();
		param.setOrderColumn("Id");
		param.setAsc(true);
		String startTime = DateUtils.parseDateToString(DateUtils.DATE_FORMAT, new Date());
		
		List<RedPacketOrder> list = redPacketOrderService.getRedPacketOrderListByDate(minId, startTime, param);
		while (!CollectionUtils.isEmpty(list)) {
			minId = list.get(list.size() - 1).getId();
			for (RedPacketOrder order : list) {
				OrderFormDTO dto = orderService.queryOrderFormByOrderId(order.getOrderId());
				if (OrderFormState.FINISH_DELIVE == dto.getOrderFormState()) {
					UserRedPacket urp = userRedPacketService.getUserRedPacketById(order.getUserRedPacketId(), order.getUserId());
					if (urp == null) {
						continue;
					}
					if (urp.getState() != ActivationConstants.STATE_CAN_USE) {
						continue;
					}
					if (urp.isValid()) {
						continue;
					}
					
					if (urp.getValidStartTime() > current) {
						long interval = urp.getValidEndTime() - urp.getValidStartTime();
						urp.setValidStartTime(current);
						urp.setValidEndTime(current + interval);
						userRedPacketService.updateUserRedPacket(urp);
					}
				}
			}
			
			list = redPacketOrderService.getRedPacketOrderListByDate(minId, startTime, param);
		}
	}
}
