/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.aop.ActionSerialization;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.UserRedPacketDao;
import com.xyl.mmall.promotion.dto.RedPacketOrderDTO;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.enums.RedPacketWayType;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.meta.RedPacketOrder;
import com.xyl.mmall.promotion.meta.RedPacketRefundDetail;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.service.RedPacketDetailService;
import com.xyl.mmall.promotion.service.RedPacketOrderService;
import com.xyl.mmall.promotion.service.RedPacketRefundDetailService;
import com.xyl.mmall.promotion.service.RedPacketService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.utils.DateUtils;

/**
 * UserRedPacketServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@Service("userRedPacketService")
public class UserRedPacketServiceImpl implements UserRedPacketService {

	@Autowired
	private UserRedPacketDao userRedPacketDao;

	@Autowired
	private RedPacketOrderService redPacketOrderService;

	@Autowired
	private RedPacketRefundDetailService redPacketRefundDetailService;

	@Autowired
	private RedPacketDetailService redPacketDetailService;

	@Autowired
	private RedPacketService redPacketService;

	@Override
	public UserRedPacket addUserRedPacket(UserRedPacket userRedPacket) {
		return userRedPacketDao.addUserRedPacket(userRedPacket);
	}

	@Override
	public List<UserRedPacketDTO> getUserRedPacketList(long userId, int state, int limit, int offset) {
		List<UserRedPacket> list = userRedPacketDao.getUserRedPacketList(userId, state, limit, offset);
		List<UserRedPacketDTO> dtos = convertUserRedPacketList(list);
		return dtos;
	}

	@Override
	public boolean updateUserRedPacket(UserRedPacket userRedPacket) {
		return userRedPacketDao.updateUserRedPacket(userRedPacket);
	}

	@Override
	public UserRedPacket getUserRedPacketById(long id, long userId) {
		return userRedPacketDao.getUserRedPacketById(id, userId);
	}

	@Override
	public List<UserRedPacketDTO> getUserRedPacketList(long userId, long timestamp, int count) {
		List<UserRedPacket> list = userRedPacketDao.getUserRedPacketList(userId, timestamp, count);
		List<UserRedPacketDTO> dtos = convertUserRedPacketList(list);
		return dtos;
	}

	@Override
	@ActionSerialization
	public boolean recycleUserRedPacket(UserRedPacket userRedPacket, PromotionLock promotionLock) {
		if (userRedPacket == null) {
			return false;
		}

		return updateUserRedPacket(userRedPacket);
	}

	private List<UserRedPacketDTO> convertUserRedPacketList(List<UserRedPacket> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		List<UserRedPacketDTO> dtos = new ArrayList<>(list.size());

		for (UserRedPacket packet : list) {
			UserRedPacketDTO dto = new UserRedPacketDTO(packet);

			if (packet.getRedPacketDetailId() <= 0) {
				dto.setName("退货运费补贴");
			} else {
				RedPacketDetail rpd = redPacketDetailService.getDetailById(packet.getRedPacketDetailId());
				if (rpd != null) {
					RedPacket rp = redPacketService.getRedPacketById(rpd.getRedPacketId());
					dto.setName(rp != null ? rp.getName() : "");
				}
			}

			List<RedPacketOrderDTO> packetOrderDTOs = redPacketOrderService.getUserRedPacketOrderList(
					packet.getUserId(), packet.getId());
			dto.setDtos(packetOrderDTOs);
			if (dto.getState() == ActivationConstants.STATE_HAS_BEAN_USED) {
				boolean hsUse = filterReturnOrders(packetOrderDTOs);
				// 没有交易订单，未使用过
				if (!hsUse) {
					if (!dto.isValid()) {
						if (dto.getValidStartTime() > System.currentTimeMillis()) {
							dto.setState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
						} else {
							dto.setState(ActivationConstants.STATE_EXPIRED);
						}
					}
					continue;
				}

				// 已经使用
				dtos.add(dto);
				continue;
			}

			// 非有效情况
			if (!dto.isValid()) {
				if (dto.getValidStartTime() > System.currentTimeMillis()) {
					// 优惠券可用设置成未生效，其他状态不做处理
					if (dto.getState() == ActivationConstants.STATE_CAN_USE) {
						dto.setState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
					}
				} else {
					dto.setState(ActivationConstants.STATE_EXPIRED);
				}
				dtos.add(dto);
				continue;
			}

			dtos.add(dto);
		}

		return dtos;
	}

	/**
	 * 过滤返券的红包记录
	 * 
	 * @param packetOrderDTOs
	 */
	private boolean filterReturnOrders(List<RedPacketOrderDTO> packetOrderDTOs) {
		if (CollectionUtils.isEmpty(packetOrderDTOs)) {
			return false;
		}

		Iterator<RedPacketOrderDTO> iterator = packetOrderDTOs.iterator();
		while (iterator.hasNext()) {
			RedPacketOrderDTO dto = iterator.next();
			// 不是使用红包删除
			if ((dto.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET || dto.getRedPacketOrderType() == RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS)
					&& dto.getRedPacketHandlerType() == ActivationHandlerType.DEFAULT) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getUserRedPacketCount(long userId, int state) {
		return userRedPacketDao.getUserRedPacketCount(userId, state);
	}

	@Override
	@ActionSerialization
	public BigDecimal getTotalCash(long userId, PromotionLock promotionLock) {
		return userRedPacketDao.getTotalCash(userId);
	}

	@Override
	public boolean deleteByDetailId(long detailId) {
		return userRedPacketDao.deleteByDetailId(detailId);
	}

	@Override
	@Transaction
	public boolean refundUserRedpackets(BigDecimal cash, long userId, long orderId, long packageId) {
		// 已经退过的红包不在处理
 		RedPacketRefundDetail detail = redPacketRefundDetailService
				.getRedPacketRefundDetail(userId, orderId, packageId);
		if (detail != null && detail.isRefund()) {
			return true;
		}
		
		boolean isAdd = false;
		if (detail == null) {
			detail = new RedPacketRefundDetail();
			isAdd = true;
		}
		List<RedPacketOrder> dtos = redPacketOrderService.getRedPacketOrderList(userId, orderId);
		if (CollectionUtils.isEmpty(dtos)) {
			return false;
		}

		List<UserRedPacketDTO> list = new ArrayList<>();
		Map<Long, BigDecimal> map = new HashMap<>();
		Map<Long, Long> tmpMap = new HashMap<>();
		BigDecimal willRefundCash = BigDecimal.ZERO;
		for (RedPacketOrder order : dtos) {
			if (willRefundCash.compareTo(cash) >= 0) {
				break;
			}
			// 不是使用红包的不处理
			if (order.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET
					&& order.getRedPacketOrderType() != RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS) {
				continue;
			}

			// 处理过的不处理
			if (order.getRedPacketHandlerType() != ActivationHandlerType.DEFAULT) {
				continue;
			}

			// 获取对应的红包
			UserRedPacket packet = getUserRedPacketById(order.getUserRedPacketId(), userId);
			if (packet == null) {
				continue;
			}

			willRefundCash = willRefundCash.add(order.getCash());

			tmpMap.put(order.getUserRedPacketId(), order.getRedPacketId());
			if (map.containsKey(order.getUserRedPacketId())) {
				BigDecimal total = map.get(order.getUserRedPacketId()).add(order.getCash());
				map.put(order.getUserRedPacketId(), total);
			} else {
				map.put(order.getUserRedPacketId(), order.getCash());
			}

			list.add(new UserRedPacketDTO(packet));
		}

		Collections.sort(list);
		//反转
		Collections.reverse(list);
		
		for (UserRedPacketDTO dto : list) {
			if (cash.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
			BigDecimal refundCash = map.get(dto.getId());
			if (refundCash == null || refundCash.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			RedPacketOrder order = new RedPacketOrder();
			order.setOrderId(orderId);
			order.setRedPacketHandlerType(ActivationHandlerType.GRANT);
			order.setRedPacketOrderType(RedPacketOrderType.RETURN_RED_PACKET);
			order.setUserId(userId);
			order.setUserRedPacketId(dto.getId());
			if (tmpMap.get(dto.getId()) != null) {
				order.setRedPacketId(tmpMap.get(dto.getId()));
			}
			order.setUsedTime(System.currentTimeMillis());
			
			if (cash.compareTo(refundCash) >= 0) {
				dto.setRemainCash(dto.getRemainCash().add(refundCash));
				dto.setUsedCash(dto.getUsedCash().subtract(refundCash));
				cash = cash.subtract(refundCash);
				order.setCash(refundCash);
			} else {
				dto.setRemainCash(dto.getRemainCash().add(cash));
				dto.setUsedCash(dto.getUsedCash().subtract(cash));
				order.setCash(cash);
				// 设置退款金额为0
				cash = BigDecimal.ZERO;
			}

			redPacketOrderService.addRedPacketOrder(order);
			dto.setState(ActivationConstants.STATE_CAN_USE);
			updateUserRedPacket(dto);
		}

		detail.setOrderId(orderId);
		detail.setPackageId(packageId);
		detail.setRefund(true);
		detail.setUserId(userId);
		
		if (isAdd) {
			return redPacketRefundDetailService.addRedPacketRefundDetail(detail) != null;
		}
		
		return redPacketRefundDetailService.updateRedPacketRefundDetail(detail);
	}

	@Override
	@Transaction
	public boolean refundCompensateUserRP(long userId, long orderId, long packageId, BigDecimal cash) {
		boolean succ = true;
		if (cash == null) {
			return false;
		}

		boolean isAdd = false;

		RedPacketRefundDetail detail = redPacketRefundDetailService
				.getRedPacketRefundDetail(userId, orderId, packageId);
		if (detail == null) {
			detail = new RedPacketRefundDetail();
			detail.setCompensateCash(cash);
			detail.setOrderId(orderId);
			detail.setPackageId(packageId);
			detail.setUserId(userId);
			isAdd = true;
		}

		if (detail.isCompensate()) {
			return true;
		}

		UserRedPacket userRedPacket = new UserRedPacket();
		long current = DateUtils.formatDate(new Date(), DateUtils.DATE_FORMAT);
		userRedPacket.setCash(cash);
		userRedPacket.setRemainCash(cash);
		userRedPacket.setRedPacketWayType(RedPacketWayType.RED_PACKET_EXP_BONUS);
		userRedPacket.setUserId(userId);
		userRedPacket.setUsedCash(BigDecimal.ZERO);
		userRedPacket.setVisible(true);
		long interval = 1000 * 60 * 60 * 24 * 30L;

		userRedPacket.setValidStartTime(current);
		userRedPacket.setValidEndTime(current + interval);
		succ = succ && addUserRedPacket(userRedPacket) != null;

		if (!succ) {
			throw new ServiceNoThrowException();
		}

		// 添加一个couponorder记录
		RedPacketOrder redPacketOrder = new RedPacketOrder();
		redPacketOrder.setOrderId(orderId);
		redPacketOrder.setCash(cash);
		redPacketOrder.setRedPacketHandlerType(ActivationHandlerType.GRANT);
		redPacketOrder.setUserId(userId);
		redPacketOrder.setUserRedPacketId(userRedPacket.getId());
		redPacketOrder.setRedPacketOrderType(RedPacketOrderType.RETURN_RED_PACKET_COMPOSE);
		redPacketOrder.setUsedTime(System.currentTimeMillis());
		succ = succ && redPacketOrderService.addRedPacketOrder(redPacketOrder);

		if (!succ) {
			throw new ServiceNoThrowException();
		}

		detail.setCompensate(true);
		if (isAdd) {
			succ = succ && redPacketRefundDetailService.addRedPacketRefundDetail(detail) != null;
		} else {
			succ = succ && redPacketRefundDetailService.updateRedPacketRefundDetail(detail);
		}

		if (!succ) {
			throw new ServiceNoThrowException();
		}

		return succ;
	}

	@Override
	public List<UserRedPacket> getUserRedPacketListByDetailId(long detailId) {
		return userRedPacketDao.getUserRedPacketListByDetailId(detailId);
	}
}
