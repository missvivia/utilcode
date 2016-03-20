/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.promotion.meta.UserRedPacket;

/**
 * UserRedPacketDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface UserRedPacketDao {
	
	/**
	 * 添加用户的红包
	 * @param userRedPacket
	 * @return
	 */
	UserRedPacket addUserRedPacket(UserRedPacket userRedPacket);

	/**
	 * 获取用户红包
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<UserRedPacket> getUserRedPacketList(long userId, int state, int limit, int offset);

	/**
	 * 更新用户红包
	 * @param userRedPacket
	 * @return
	 */
	boolean updateUserRedPacket(UserRedPacket userRedPacket);
	
	/**
	 * 获取指定id的红包
	 * @param id
	 * @param userId
	 * @return
	 */
	UserRedPacket getUserRedPacketById(long id, long userId);
	
	/**
	 * mobile获取红包列表
	 * @param userId
	 * @param timestamp
	 * @param count
	 * @return
	 */
	List<UserRedPacket> getUserRedPacketList(long userId, long timestamp, int count);
	
	/**
	 * 获取用户红包总数
	 * @param userId
	 * @param isValid
	 * @return
	 */
	int getUserRedPacketCount(long userId, int state);
	
	/**
	 * 获取用户红包总额
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalCash(long userId);

	boolean deleteByDetailId(long detailId);

	List<UserRedPacket> getUserRedPacketListByDetailId(long detailId);

}
