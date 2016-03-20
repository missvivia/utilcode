/*
 * @(#) 2015-1-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.ActivationRecord;

/**
 * ActivationRecordDao.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-1-23
 * @since      1.0
 */
public interface ActivationRecordDao extends AbstractDao<ActivationRecord> {

	ActivationRecord getActivationRecordByUserIdAndState(long userId, boolean free);

}
