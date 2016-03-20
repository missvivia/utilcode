/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade;

import com.xyl.mmall.promotion.meta.RedPacketDetail;

/**
 * RedPacketDetailFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
public interface RedPacketDetailFacade {
	
	/**
	 * 用户领取红包。
	 * 
	 * @param userId
	 *            用户Id
	 * @param redPacketId
	 *            红包Id
	 * @return
	 */
	RedPacketDetail takeRedPacketDetail(long userId, long redPacketId, int groupId);

}
