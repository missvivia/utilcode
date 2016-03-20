/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion;

import java.util.List;

import com.xyl.mmall.promotion.dto.RedPacketDTO;

/**
 * RedPacketFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public interface RedPacketFacade {
	/**
	 * 获取红包列表
	 * @param state
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<RedPacketDTO> getRedPacketList(long userId, int state, String qvalue, int limit, int offset);
	
	
	/**
	 * 根据id获取红包
	 * @param id
	 * @return
	 */
	RedPacketDTO getRedPacketById(long id);
	/**
	 * 添加一个红包
	 * @param redPacket
	 * @return
	 */
	RedPacketDTO addRedPacket(RedPacketDTO redPacket);
	
	/**
	 * 更新红包记录
	 * @param redPacket
	 * @return
	 */
	boolean updateRedPacket(RedPacketDTO redPacket);

	/**
	 * 获取红包总数
	 * @param state
	 * @return
	 */
	int getRedPacketCount(long userId, int state, String qvalue);

	/**
	 * 撤销红包
	 * @param redPacketDTO
	 * @return
	 */
	boolean discardRedPacket(RedPacketDTO redPacketDTO);
}
