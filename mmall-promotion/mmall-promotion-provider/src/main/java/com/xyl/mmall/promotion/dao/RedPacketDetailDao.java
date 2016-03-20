/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.xyl.mmall.promotion.meta.RedPacketDetail;

/**
 * RedPacketDetailDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface RedPacketDetailDao {
	/**
	 * 添加一个具体红包记录
	 * @param detail
	 * @return
	 */
	RedPacketDetail addRedPacketDetail(RedPacketDetail detail);
	
	/**
	 * 根据红包id随机获取一个有效红包
	 * @param redPacketId
	 * @return
	 */
	RedPacketDetail getRamdomDetailByRedPacketId(long redPacketId, int groupId, Boolean visible);
	
	/**
	 * copies减1
	 * @param detail
	 */
	boolean setCopiesMinusOne(RedPacketDetail detail);

	/**
	 * 获取一个红包
	 * @param redPacketDetailId
	 * @return
	 */
	RedPacketDetail getDetailById(long redPacketDetailId);

	List<RedPacketDetail> getDetailListByPacketId(long redPacketId);

	boolean deleteDetailById(long id);

	boolean updateRedPacketDetail(RedPacketDetail detail);
}
