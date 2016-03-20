/*
 * @(#) 2014-12-29
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import com.xyl.mmall.promotion.meta.RedPacketRefundDetail;

/**
 * RedPacketRefundDetailDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-29
 * @since      1.0
 */
public interface RedPacketRefundDetailDao {

	RedPacketRefundDetail addRedPacketRefundDetail(RedPacketRefundDetail detail);

	boolean updateRedPacketRefundDetail(RedPacketRefundDetail detail);

	RedPacketRefundDetail getRedPacketRefundDetail(long userId, long orderId, long packageId);

}
