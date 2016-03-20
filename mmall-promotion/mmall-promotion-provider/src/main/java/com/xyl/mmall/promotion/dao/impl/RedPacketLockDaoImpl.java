/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketLockDao;
import com.xyl.mmall.promotion.meta.RedPacketLock;

/**
 * RedPacketLockDaoImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
@Repository
public class RedPacketLockDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketLock> implements RedPacketLockDao {

	@Override
	public void deleteByRedPacketId(long id) {
		StringBuilder sql = new StringBuilder();
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "redPacketId", id);
		
		this.getSqlSupport().excuteUpdate(sql.toString());
	}
}
