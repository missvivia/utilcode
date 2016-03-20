/*
 * @(#) 2014-12-29
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.promotion.dao.RedPacketRefundDetailDao;
import com.xyl.mmall.promotion.meta.RedPacketRefundDetail;
import com.xyl.mmall.promotion.service.RedPacketRefundDetailService;

/**
 * RedPacketRefundDetailServiceImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-12-29
 * @since      1.0
 */

@Service("redPacketRefundDetailService")
public class RedPacketRefundDetailServiceImpl implements RedPacketRefundDetailService {
	
	@Autowired
	private RedPacketRefundDetailDao redPacketRefundDetailDao;
	
	@Override
	public RedPacketRefundDetail addRedPacketRefundDetail(RedPacketRefundDetail detail) {
		return redPacketRefundDetailDao.addRedPacketRefundDetail(detail);
	}

	@Override
	public boolean updateRedPacketRefundDetail(RedPacketRefundDetail detail) {
		return redPacketRefundDetailDao.updateRedPacketRefundDetail(detail);
	}

	@Override
	public RedPacketRefundDetail getRedPacketRefundDetail(long userId, long orderId, long packageId) {
		return redPacketRefundDetailDao.getRedPacketRefundDetail(userId, orderId, packageId);
	}

}
