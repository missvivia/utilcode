/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.promotion.aop.ActionSerialization;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.RedPacketLockDao;
import com.xyl.mmall.promotion.dao.RedPacketOrderDao;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.dto.RedPacketOrderDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.enums.RedPacketWayType;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.meta.RedPacketLock;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.service.RedPacketDetailService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.RedPacketShareRecordService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.utils.DateUtils;

/**
 * RedPacketOrderServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-28
 * @since 1.0
 */
@Service("redPacketOrderService")
public class RedPacketOrderServiceImpl implements RedPacketOrderService {

	@Autowired
	private RedPacketOrderDao redPacketOrderDao;

	@Autowired
	private RedPacketDetailService redPacketDetailService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private RedPacketService redPacketService;

	@Autowired
	private RedPacketShareRecordService redPacketShareRecordService;

	@Autowired
	private RedPacketLockDao redPacketLockDao;

	@Override
	public boolean addRedPacketOrder(RedPacketOrder redPacketOrder) {
		return redPacketOrderDao.addRedPacketOrder(redPacketOrder);
	}

	@Override
	@Transaction
	public int doSuccOrderOfRedPacketReturn(List<RedPacketOrder> orders) {
		if (CollectionUtils.isEmpty(orders)) {
			return 2;
		}

		boolean isSucc = true;

		long orderId = 0;
		long userId = 0;

		for (RedPacketOrder order : orders) {
			if (!isSucc) {
				break;
			}
			// 不是返红包状态不处理
			if (order.getRedPacketOrderType() != RedPacketOrderType.RETURN_RED_PACKET) {
				continue;
			}

			// 不是初始状态不处理
			if (order.getRedPacketHandlerType() != ActivationHandlerType.DEFAULT) {
				continue;
			}

			RedPacket redPacket = redPacketService.getRedPacketById(order.getRedPacketId());
			if (redPacket == null) {
				continue;
			}

			// 红包失效
			if (!redPacket.isValid()) {
				continue;
			}

			// 可领个数无效
			if (redPacket.getCount() <= 0) {
				continue;
			}

			// 分享
			if (redPacket.isShare()) {
				shareRedPacket(redPacket.getId(), order.getOrderId());
				continue;
			}
			// 返红包与用户绑定
			RedPacketDetail detail = redPacketDetailService.getRamdomDetailByRedPacketId(order.getRedPacketId(), 0, Boolean.TRUE);
			if (detail == null) {
				continue;
			}

			if (orderId <= 0) {
				orderId = order.getOrderId();
			}

			if (userId <= 0) {
				userId = order.getUserId();
			}

			UserRedPacket userRedPacket = new UserRedPacket();
			userRedPacket.setCash(detail.getCash());
			userRedPacket.setRedPacketDetailId(detail.getId());
			userRedPacket.setRemainCash(detail.getCash());
			userRedPacket.setUsedCash(BigDecimal.ZERO);
			userRedPacket.setUserId(userId);
			userRedPacket.setRedPacketWayType(RedPacketWayType.RED_PACKET_REBATE);

			long current = DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT);
			long interval = detail.getValidEndTime() - detail.getValidStartTime();

			long startTime = current + 1000 * 60 * 60 * 24 * 7L;
			long endTime = startTime + interval;
			userRedPacket.setValidStartTime(startTime);
			userRedPacket.setValidEndTime(endTime);
			userRedPacket.setVisible(true);
			
			isSucc = isSucc && userRedPacketService.addUserRedPacket(userRedPacket) != null;

			order.setUserRedPacketId(userRedPacket.getId());
			order.setRedPacketHandlerType(ActivationHandlerType.GRANT);
			updateRedPacketOrder(order);

		}
		if (!isSucc) {
			throw new ServiceException("orderId=" + orderId);
		}
		return 1;
	}

	/**
	 * 分享红包处理
	 * 
	 * @param id
	 */
	private void shareRedPacket(long redPacketId, long orderId) {
		boolean isSucc = true;
		RedPacketLock packetLock = new RedPacketLock();
		packetLock.setRedPacketId(redPacketId);
		// 红包获取一个锁，事务提交后自动释放
		packetLock = redPacketLockDao.getLockByKey(packetLock);
		// 获取红包
		RedPacket redPacket = redPacketService.getRedPacketById(redPacketId);
		// 如果红包可领数为0，不处理
		if (redPacket.getCount() <= 0) {
			return;
		}
		RedPacketShareRecord record = redPacketShareRecordService.getByTypeAndValue(ShareChannel.BY_ORDER, orderId);
		// 记录不为空，已经分享过，不做处理
		if (record != null) {
			return;
		}

		record = new RedPacketShareRecord();
		record.setRedPacketId(redPacketId);
		// 设置分组id为当前可领数
		record.setGroupId(redPacket.getCount());
		// 通过下单分享
		record.setShareChannel(ShareChannel.BY_ORDER);
		// 分享的订单值
		record.setShareChannelValue(String.valueOf(orderId));
		record.setStartTime(redPacket.getStartTime());
		isSucc = isSucc && redPacketShareRecordService.addRedPacketShareRecord(record);
		if (!isSucc) {
			throw new ServiceException("添加分享记录异常");
		}

		// 设置可领分数减1
		redPacket.setCount(redPacket.getCount() - 1);
		isSucc = isSucc && redPacketService.updateRedPacket(new RedPacketDTO(redPacket));
		if (!isSucc) {
			throw new ServiceException("更新红包记录异常");
		}
	}

	@Override
	public RedPacketOrder getRedPacketOrderByIds(long userId, long orderId, long userRedPacketId) {
		return redPacketOrderDao.getRedPacketOrderByIds(userId, orderId, userRedPacketId);
	}

	@Override
	@ActionSerialization
	@Transaction
	public boolean recycleRedPacketOrder(RedPacketOrder redPacketOrder, PromotionLock promotionLock) {
		// 1.参数判断
		if (redPacketOrder == null) {
			return true;
		}

		// 2.判断优惠券是否需要回收
		// 使用优惠券，回收时使用次数加1(使用失败，需要将使用的次数添加回去)
		boolean isUse = redPacketOrder.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET
				|| redPacketOrder.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS;
		
		if (isUse) {
			// 只能处理初始状态
			if (redPacketOrder.getRedPacketHandlerType() != ActivationHandlerType.DEFAULT) {
				return true;
			}
		} else {
			//只处理已经发放的状态
			if (redPacketOrder.getRedPacketHandlerType() != ActivationHandlerType.GRANT) {
				return true;
			}
		}

		// 判断红包
		UserRedPacket userRedPacket = userRedPacketService.getUserRedPacketById(redPacketOrder.getUserRedPacketId(),
				redPacketOrder.getUserId());
		// 回收用户下的红包
		if (userRedPacket == null) {
			return false;
		}

		// 红包过期不做处理
		if (!userRedPacket.isValid() && isUse) {
			return true;
		}

		// 使用红包，回收变成可用
		if (isUse) {
			userRedPacket.setRemainCash(userRedPacket.getRemainCash().add(redPacketOrder.getCash()));
			userRedPacket.setUsedCash(userRedPacket.getUsedCash().subtract(redPacketOrder.getCash()).setScale(2));
			userRedPacket.setState(ActivationConstants.STATE_CAN_USE);
		} else {
			// 返的红包，变成失效
			userRedPacket.setState(ActivationConstants.STATE_INACTIVE);
		}
		
		if (isUse) {
			RedPacketOrder order = new RedPacketOrder();
			order.setCash(redPacketOrder.getCash());
			order.setOrderId(redPacketOrder.getOrderId());
			order.setRedPacketHandlerType(ActivationHandlerType.GRANT);
			order.setRedPacketId(redPacketOrder.getRedPacketId());
			order.setRedPacketOrderType(RedPacketOrderType.RETURN_RED_PACKET);
			order.setUserId(redPacketOrder.getUserId());
			order.setUserRedPacketId(redPacketOrder.getUserRedPacketId());
			order.setUsedTime(System.currentTimeMillis());
			addRedPacketOrder(order);
		}
		return userRedPacketService.recycleUserRedPacket(userRedPacket, promotionLock);
	}

	@Override
	public boolean updateRedPacketOrder(RedPacketOrder redPacketOrder) {
		return redPacketOrderDao.updateRedPacketOrder(redPacketOrder);
	}

	@Override
	public List<RedPacketOrderDTO> getUserRedPacketOrderList(long userId, long userRedPacketId) {
		List<RedPacketOrder> orders = redPacketOrderDao.getUserRedPacketOrderList(userId, userRedPacketId);
		return convertRedPacketOrderList(orders);
	}

	private List<RedPacketOrderDTO> convertRedPacketOrderList(List<RedPacketOrder> orders) {
		if (CollectionUtils.isEmpty(orders)) {
			return null;
		}

		List<RedPacketOrderDTO> dtos = new ArrayList<>(orders.size());
		for (RedPacketOrder order : orders) {
			RedPacketOrderDTO dto = new RedPacketOrderDTO(order);
			dto.setOrderSerial(String.valueOf(order.getOrderId()));
			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public List<RedPacketOrder> getRedPacketOrderList(long userId, long orderId) {
		return redPacketOrderDao.getRedPacketOrderList(userId, orderId);
	}

	@Override
	public List<RedPacketOrder> getRedPacketOrderListByDate(long minId, String time, DDBParam param) {
		return redPacketOrderDao.getRedPacketOrderListByDate(minId, time, param);
	}

	@Override
	public boolean deleteByRedPacketId(long redPacketId) {
		return redPacketOrderDao.deleteByRedPacketId(redPacketId);
	}

}
