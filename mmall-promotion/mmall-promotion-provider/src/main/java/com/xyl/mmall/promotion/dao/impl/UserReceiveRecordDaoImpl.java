/*
 * @(#) 2014-11-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.UserReceiveRecordDao;
import com.xyl.mmall.promotion.meta.UserReceiveRecord;

/**
 * UserReceiveRecordDaoImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-19
 * @since      1.0
 */
@Repository
public class UserReceiveRecordDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserReceiveRecord> implements UserReceiveRecordDao {

	@Override
	public UserReceiveRecord getUserReceiveRecord(long userId, long redPacketId, long groupId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "RedPacketId", redPacketId);
		SqlGenUtil.appendExtParamObject(sql, "GroupId", groupId);
		return this.queryObject(sql);
	}

}
