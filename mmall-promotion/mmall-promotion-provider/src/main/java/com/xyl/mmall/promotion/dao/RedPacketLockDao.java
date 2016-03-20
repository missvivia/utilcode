/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.RedPacketLock;

/**
 * RedPacketLockDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
public interface RedPacketLockDao extends AbstractDao<RedPacketLock> {

	void deleteByRedPacketId(long id);
}
