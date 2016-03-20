/*
 * @(#) 2014-11-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketShareRecordDao;
import com.xyl.mmall.promotion.enums.ShareChannel;
import com.xyl.mmall.promotion.meta.RedPacketShareRecord;

/**
 * RedPacketShareRecordDaoImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-11-14
 * @since      1.0
 */
@Repository
public class RedPacketShareRecordDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketShareRecord> implements
		RedPacketShareRecordDao {

	@Override
	public RedPacketShareRecord getByIdAndValue(long redPacketId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketId", redPacketId);
		SqlGenUtil.appendExtParamObject(sql, "ShareChannelValue", String.valueOf(orderId));
		return this.queryObject(sql);
	}

	@Override
	public RedPacketShareRecord getByTypeAndValue(ShareChannel shareChannel, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "ShareChannel", shareChannel);
		SqlGenUtil.appendExtParamObject(sql, "ShareChannelValue", String.valueOf(orderId));
		return this.queryObject(sql);
	}


}
