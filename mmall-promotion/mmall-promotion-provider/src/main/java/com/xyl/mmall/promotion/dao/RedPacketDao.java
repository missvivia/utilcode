/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.RedPacket;

/**
 * RedPacketDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface RedPacketDao extends AbstractDao<RedPacket> {
	
	/**
	 * 更新红包数据库记录
	 * @param redPacket
	 * @return
	 */
	boolean updateRedPacket(RedPacket redPacket);

	/**
	 * 新增一条红包记录
	 * @param redPacket
	 * @return
	 */
	RedPacket addRedPacket(RedPacket redPacket);
	
	/**
	 * 获取红包列表
	 * @param state
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<RedPacket> getRedPacketList(long userId, int state, String qvalue, int limit, int offset);
	
	/**
	 * 根据id获取红包
	 * @param id
	 * @return
	 */
	RedPacket getRedPacketById(long id);
	
	/**
	 * 根据状态获取总数
	 * @param state
	 * @return
	 */
	int getRedPacketCount(long userId, int state, String qvalue);
	
	/**
	 * 获取一个分享的红包
	 * @return
	 */
	RedPacket getRandomOneToShare(BigDecimal cash);

}
