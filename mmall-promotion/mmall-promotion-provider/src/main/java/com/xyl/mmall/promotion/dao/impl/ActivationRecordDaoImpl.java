/*
 * @(#) 2015-1-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.ActivationRecordDao;
import com.xyl.mmall.promotion.meta.ActivationRecord;

/**
 * ActivationRecordDaoImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2015-1-23
 * @since      1.0
 */
@Repository
public class ActivationRecordDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ActivationRecord> implements ActivationRecordDao {

	@Override
	public ActivationRecord getActivationRecordByUserIdAndState(long userId, boolean free) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "Free", free);
		return this.queryObject(sql);
	}

}
