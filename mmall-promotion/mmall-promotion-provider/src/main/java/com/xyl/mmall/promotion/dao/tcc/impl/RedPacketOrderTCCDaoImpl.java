/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.tcc.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.tcc.RedPacketOrderTCCDao;
import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;

/**
 * RedPacketOrderTCCServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-28
 * @since 1.0
 */
@Repository
public class RedPacketOrderTCCDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketOrderTCC> implements
		RedPacketOrderTCCDao {

	@Override
	public RedPacketOrderTCC addRedPacketOrderTCC(RedPacketOrderTCC redPacketOrderTCC) {
		if (redPacketOrderTCC.getId() <= 0) {
			redPacketOrderTCC.setId(this.allocateRecordId());
		}
		return this.addObject(redPacketOrderTCC);
	}

	@Override
	public List<RedPacketOrderTCC> getRedPacketOrderTCCListByTranId(long tranId) {
		StringBuilder sql = new StringBuilder();

		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "TranId", tranId);
		return this.queryObjects(sql);
	}

	@Override
	public boolean deleteRedPacketOrderTCC(long tranId) {
		StringBuilder sql = new StringBuilder();

		sql.append(this.genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "TranId", tranId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
