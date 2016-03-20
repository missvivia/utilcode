/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.tcc;

import java.util.List;

import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;

/**
 * RedPacketOrderTCCService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public interface RedPacketOrderTCCDao {

	RedPacketOrderTCC addRedPacketOrderTCC(RedPacketOrderTCC redPacketOrderTCC);

	List<RedPacketOrderTCC> getRedPacketOrderTCCListByTranId(long tranId);

	boolean deleteRedPacketOrderTCC(long tranId);

}
