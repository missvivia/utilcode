/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;

import com.xyl.mmall.promotion.meta.RedPacketDetail;

/**
 * RedPacketDetailService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface RedPacketDetailService {
	
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
	 * 用户领取一个具体的红包
	 * @param userId
	 * @return
	 */
	RedPacketDetail takeRedPacketDetail(long userId, long redPacketId, int groupId);
	
	/**
	 * 获取一个红包
	 * @param redPacketDetailId
	 * @return
	 */
	RedPacketDetail getDetailById(long redPacketDetailId);
	
	/**
	 * 获取detail列表
	 * @param id
	 * @return
	 */
	List<RedPacketDetail> getDetailListByPacketId(long id);

	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	boolean deleteDetailById(long id);
	
	/**
	 * 更新记录
	 * @param detail
	 * @return
	 */
	boolean updateRedPacketDetail(RedPacketDetail detail);
}
