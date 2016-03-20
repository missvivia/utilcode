/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mobile.web.facade;

import java.math.BigDecimal;
import java.util.List;

import com.xyl.mmall.promotion.dto.UserRedPacketDTO;

/**
 * UserRedPacketFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public interface MobileUserRedPacketFacade {
	
	/**
	 * 获取用户的红包
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<UserRedPacketDTO> getUserRedPacketList(long userId, int state, int limit, int offset);
	
	/**
	 * mobile获取用户的红包
	 * @param isValid
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<UserRedPacketDTO> getUserRedPacketList(long userId, long timestamp, int count);
	
	/**
	 * 退红包
	 * @param cash 需要退的红包金额
	 * @param userId 用户id
	 * @param orderId 退红包的订单id
	 * @return
	 */
	boolean refundUserRedpackets(BigDecimal cash, long userId, long orderId, long packageId);
	
	/**
	 * 修改红包有效期
	 */
	void modifyRpValidity();
}
