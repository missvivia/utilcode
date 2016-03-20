/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.promotion.dto.RedPacketOrderDTO;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.RedPacketOrder;

/**
 * RedPacketOrderService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public interface RedPacketOrderService {

	boolean addRedPacketOrder(RedPacketOrder redPacketOrder);

	int doSuccOrderOfRedPacketReturn(List<RedPacketOrder> orders);

	RedPacketOrder getRedPacketOrderByIds(long userId, long orderId, long userRedPacketId);

	boolean recycleRedPacketOrder(RedPacketOrder redPacketOrder, PromotionLock promotionLock);

	boolean updateRedPacketOrder(RedPacketOrder redPacketOrder);
	
	List<RedPacketOrderDTO> getUserRedPacketOrderList(long userId, long userRedPacketId);

	List<RedPacketOrder> getRedPacketOrderList(long userId, long orderId);

	List<RedPacketOrder> getRedPacketOrderListByDate(long minId, String time, DDBParam param);

	boolean deleteByRedPacketId(long id);

}
