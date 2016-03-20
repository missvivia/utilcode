/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.RedPacketDetailFacade;
import com.xyl.mmall.promotion.meta.RedPacketDetail;
import com.xyl.mmall.promotion.service.RedPacketDetailService;

/**
 * RedPacketDetailFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
@Facade
public class RedPacketDetailFacadeImpl implements RedPacketDetailFacade {
	
	@Autowired
	private RedPacketDetailService redPacketDetailService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.RedPacketDetailFacade#takeRedPacketDetail(long,
	 *      long)
	 */
	@Override
	public RedPacketDetail takeRedPacketDetail(long userId, long redPacketId, int groupId) {
		return redPacketDetailService.takeRedPacketDetail(userId, redPacketId, groupId);
	}

}
