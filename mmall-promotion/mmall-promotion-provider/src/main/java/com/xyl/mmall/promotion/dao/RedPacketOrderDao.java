/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.promotion.meta.RedPacketOrder;

/**
 * RedPacketOrderDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public interface RedPacketOrderDao {

	boolean addRedPacketOrder(RedPacketOrder redPacketOrder);

	RedPacketOrder getRedPacketOrderByIds(long userId, long orderId, long userRedPacketId);

	boolean updateRedPacketOrder(RedPacketOrder redPacketOrder);

	List<RedPacketOrder> getUserRedPacketOrderList(long userId, long userRedPacketId);

	List<RedPacketOrder> getRedPacketOrderList(long userId, long orderId);

	List<RedPacketOrder> getRedPacketOrderListByDate(long minId, String time, DDBParam param);

	boolean deleteByRedPacketId(long redPacketId);
}
