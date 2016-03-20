/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.meta.RedPacket;

/**
 * RedPacketService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface RedPacketService {
	
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
	 * 添加一个红包
	 * @param redPacket
	 * @return
	 */
	RedPacket addRedPacket(RedPacket redPacket);
	
	/**
	 * 更新红包记录
	 * @param redPacket
	 * @return
	 */
	boolean updateRedPacket(RedPacketDTO redPacketDTO);

	/**
	 * 根据状态获取红包总数
	 * @param state
	 * @return
	 */
	int getRedPacketCount(long userId, int state, String qvalue);
	
	/**
	 * 获取随机的一个红包用于分享
	 * @return
	 */
	RedPacket getRandomOneToShare(BigDecimal cash);

	/**
	 * 撤销红包
	 * @param redPacketDTO
	 * @return
	 */
	boolean discardRedPacket(RedPacketDTO redPacketDTO);
	
}
