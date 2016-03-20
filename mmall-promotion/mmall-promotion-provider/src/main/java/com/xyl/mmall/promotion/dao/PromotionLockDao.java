/*
 * @(#) 2014-10-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.PromotionLock;

/**
 * PromotionLockDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-24
 * @since      1.0
 */
public interface PromotionLockDao extends AbstractDao<PromotionLock> {
	
	/**
	 * 获取锁
	 * @param userId
	 * @return
	 */
	PromotionLock getLock(long userId);
	
}
