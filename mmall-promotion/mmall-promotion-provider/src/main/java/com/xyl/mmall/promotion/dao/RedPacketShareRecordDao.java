/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;

/**
 * RedPacketShareRecordDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
public interface RedPacketShareRecordDao extends AbstractDao<RedPacketShareRecord> {

	RedPacketShareRecord getByIdAndValue(long redPacketId, long orderId);

	RedPacketShareRecord getByTypeAndValue(ShareChannel shareChannel, long orderId);

}
