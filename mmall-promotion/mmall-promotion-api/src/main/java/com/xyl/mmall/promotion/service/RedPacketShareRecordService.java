/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;

/**
 * RedPacketShareRecordService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
public interface RedPacketShareRecordService {
	
	/**
	 * 根据id和值获取红包分享记录
	 * @param redPacketId
	 * @param orderId
	 * @return
	 */
	RedPacketShareRecord getByIdAndValue(long redPacketId, long orderId);
	
	/**
	 * 添加一条分享记录
	 * @param record
	 * @return
	 */
	boolean addRedPacketShareRecord(RedPacketShareRecord record);
	
	/**
	 * 根据type和值获取
	 * @param byOrder
	 * @param orderId
	 * @return
	 */
	RedPacketShareRecord getByTypeAndValue(ShareChannel byOrder, long orderId);
	
	/**
	 * 根据逐渐id获取对象
	 * @param long1
	 * @return
	 */
	RedPacketShareRecord getById(long id);
	
	/**
	 * 获取分享的id
	 */
	String getRedPacketShareId(ShareChannel byOrder, long orderId);

}
