/*
 * @(#) 2014-12-29
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketRefundDetailDao;
import com.xyl.mmall.promotion.meta.RedPacketRefundDetail;

/**
 * RedPacketRefundDetailDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-12-29
 * @since 1.0
 */
@Repository
public class RedPacketRefundDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketRefundDetail> implements
		RedPacketRefundDetailDao {

	@Override
	public RedPacketRefundDetail addRedPacketRefundDetail(RedPacketRefundDetail detail) {
		return this.addObject(detail);
	}

	@Override
	public boolean updateRedPacketRefundDetail(RedPacketRefundDetail detail) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Refund");
		fieldNameSetOfUpdate.add("Compensate");
		fieldNameSetOfUpdate.add("CompensateCash");
		fieldNameSetOfUpdate.add("Cash");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("OrderId");
		fieldNameSetOfWhere.add("PackageId");
		fieldNameSetOfWhere.add("UserId");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, detail, getSqlSupport());
	}

	@Override
	public RedPacketRefundDetail getRedPacketRefundDetail(long userId, long orderId, long packageId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "OrderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "PackageId", packageId);
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		
		return this.queryObject(sql);
	}

}
