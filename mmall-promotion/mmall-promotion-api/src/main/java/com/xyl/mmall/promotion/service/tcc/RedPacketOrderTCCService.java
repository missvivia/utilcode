/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc;

import java.util.List;

import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;

/**
 * RedPacketOrderTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public interface RedPacketOrderTCCService {
	
	/**
	 * 添加
	 * @param tranId
	 * @param redPacketOrderTCC
	 * @return
	 */
	RedPacketOrderTCC tryAddRedPacketOrderTCC(long tranId, RedPacketOrderTCC redPacketOrderTCC);

	List<RedPacketOrderTCC> getRedPacketOrderTCCListByTranId(long tranId);

	/**
	 * 确认添加 RedPacketOrderTCC --> RedPacketOrder
	 * @param tranId
	 * @return
	 */
	boolean confirmAddRedPacketOrderTCC(long tranId);
	
	/**
	 * 取消tcc插入
	 * @param tranId
	 * @return
	 */
	boolean cancelAddRedPacketOrderTCC(long tranId);
	
	/**
	 * 删除指定事务id记录
	 * @param tranId
	 * @return
	 */
	boolean deleteRedPacketOrderTCC(long tranId);

}
