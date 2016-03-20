/*
 * @(#) 2014-10-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.PromotionLockDao;
import com.xyl.mmall.promotion.meta.PromotionLock;

/**
 * PromotionLockDaoImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-24
 * @since      1.0
 */
@Repository
public class PromotionLockDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PromotionLock> implements PromotionLockDao {
	
	private static final String GET_PROMOTION_LOCK = "SELECT * FROM Mmall_Promotion_Lock WHERE UserId = ? FOR UPDATE";
	
	@Override
	public PromotionLock getLock(long userId) {
		return this.queryObject(GET_PROMOTION_LOCK, userId);
	}

}
