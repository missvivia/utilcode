/*
 * @(#) 2014-11-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import com.xyl.mmall.promotion.meta.UserReceiveRecord;

/**
 * UserReceiveRecordService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-19
 * @since      1.0
 */
public interface UserReceiveRecordService {
	
	/**
	 * 添加一条领取记录
	 * @param record
	 * @return
	 */
	UserReceiveRecord addReceiveRecord(UserReceiveRecord record);
	
	/**
	 * 获取记录
	 * @param userId 用户Id
	 * @param redPacketId 分享的红包id
	 * @param groupId 分组红包的组别id
	 */
	UserReceiveRecord getUserReceiveRecord(long userId, long redPacketId, long groupId);
	
	/**
	 * 根据主键获取记录
	 * @param id
	 * @return
	 */
	UserReceiveRecord getUserReceiveRecordById(long id);
}
