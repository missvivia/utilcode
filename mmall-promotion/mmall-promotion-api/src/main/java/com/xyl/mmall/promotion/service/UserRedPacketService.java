/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.UserRedPacket;

/**
 * UserRedPacketService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface UserRedPacketService {
	
	/**
	 * 添加一个用户红包
	 * @param userRedPacket
	 */
	UserRedPacket addUserRedPacket(UserRedPacket userRedPacket);
	
	/**
	 * 获取用户的红包
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<UserRedPacketDTO> getUserRedPacketList(long userId, int state, int limit, int offset);
	
	/**
	 * 更新用户的红包记录
	 * @param userRedPacket
	 * @return
	 */
	boolean updateUserRedPacket(UserRedPacket userRedPacket);
	
	/**
	 * 获取指定id的用户红包
	 * @param id
	 * @param userId
	 * @return
	 */
	UserRedPacket getUserRedPacketById(long id, long userId);
	
	/**
	 * 手机端获取红包
	 * @param userId
	 * @param timestamp
	 * @param count
	 * @return
	 */
	List<UserRedPacketDTO> getUserRedPacketList(long userId, long timestamp, int count);
	
	/**
	 * 回收红包
	 * @param userRedPacket
	 * @param isAdd
	 */
	boolean recycleUserRedPacket(UserRedPacket userRedPacket, PromotionLock promotionLock);
	
	/**
	 * 获取用户红包总数
	 * @param userId
	 * @param b
	 * @return
	 */
	int getUserRedPacketCount(long userId, int state);
	
	/**
	 * 获取用户可用红包总额
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalCash(long userId, PromotionLock promotionLock);

	/**
	 * 删除detailid对用用户红包
	 * @param detailId
	 * @return
	 */
	boolean deleteByDetailId(long detailId);
	
	/**
	 * 退货红包返还
	 * @param cash
	 * @param userId
	 * @param orderId
	 * @param packageId
	 * @return
	 */
	boolean refundUserRedpackets(BigDecimal cash, long userId, long orderId, long packageId);
	
	/**
	 * 退货补偿
	 * @param userId
	 * @param orderId
	 * @param couponCode
	 * @return
	 */
	boolean refundCompensateUserRP(long userId, long orderId, long packageId, BigDecimal cash);
	
	/**
	 * 根据detailid获取列表
	 * @param detailId
	 * @return
	 */
	List<UserRedPacket> getUserRedPacketListByDetailId(long detailId);
}
