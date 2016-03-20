/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketDao;
import com.xyl.mmall.promotion.meta.RedPacket;

/**
 * RedPacketDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository
public class RedPacketDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacket> implements RedPacketDao {

	@Override
	public boolean updateRedPacket(RedPacket redPacket) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Name");
		fieldNameSetOfUpdate.add("Description");
		fieldNameSetOfUpdate.add("Cash");
		fieldNameSetOfUpdate.add("StartTime");
		fieldNameSetOfUpdate.add("EndTime");
		fieldNameSetOfUpdate.add("UpdateTime");
		fieldNameSetOfUpdate.add("Copies");
		fieldNameSetOfUpdate.add("AuditState");
		fieldNameSetOfUpdate.add("AuditUserId");
		fieldNameSetOfUpdate.add("AuditTime");
		fieldNameSetOfUpdate.add("Count");
		fieldNameSetOfUpdate.add("Reason");
		fieldNameSetOfUpdate.add("Share");
		fieldNameSetOfUpdate.add("Produce");
		fieldNameSetOfUpdate.add("Users");
		fieldNameSetOfUpdate.add("Platform");
		fieldNameSetOfUpdate.add("DistributeRule");
		fieldNameSetOfUpdate.add("BinderType");
		fieldNameSetOfUpdate.add("Used");
		fieldNameSetOfUpdate.add("ValidDay");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, redPacket, getSqlSupport());
	}

	@Override
	public RedPacket addRedPacket(RedPacket redPacket) {
		redPacket.setId(this.allocateRecordId());
		return this.addObject(redPacket);
	}

	@Override
	public List<RedPacket> getRedPacketList(long userId, int state, String qvalue, int limit, int offset) {
		StringBuilder sb = new StringBuilder();
		sb.append(genSelectSql());

		buildQuerySql(userId, state, qvalue, sb);
		sb.append(" ORDER BY CreateTime DESC ");
		
		appendLimitSql(sb, limit, offset);
		return this.queryObjects(sb);
	}

	@Override
	public RedPacket getRedPacketById(long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sb, "Id", id);
		return this.queryObject(sb);
	}

	@Override
	public int getRedPacketCount(long userId, int state, String qvalue) {
		StringBuilder sb = new StringBuilder();
		sb.append(genCountSql());

		buildQuerySql(userId, state, qvalue, sb);
		return this.getSqlSupport().queryCount(sb.toString());
	}

	private void buildQuerySql(long userId, int state, String qvalue, StringBuilder sb) {
		if (userId > 0) {
			SqlGenUtil.appendExtParamObject(sb, "ApplyUserId", userId);
		}
		if (state >= 0) {
			SqlGenUtil.appendExtParamObject(sb, "AuditState", state);
		}
		
		if (StringUtils.isNotBlank(qvalue)) {
			sb.append(" AND Description like '").append(qvalue).append("%'");
		}
	}

	@Override
	public RedPacket getRandomOneToShare(BigDecimal cash) {
		long current = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append(genSelectSql());
		
		sb.append(" AND StartTime <= ").append(current);
		sb.append(" AND EndTime > ").append(current);
		SqlGenUtil.appendExtParamObject(sb, "Share", true);
		SqlGenUtil.appendExtParamObject(sb, "Used", false);
		if (cash != null && cash.compareTo(BigDecimal.ZERO) > 0) {
			SqlGenUtil.appendExtParamObject(sb, "Cash", cash);
		}
		appendLimitSql(sb, 1, 0);
		RedPacket redPacket = this.queryObject(sb);
		return redPacket;
	}

}
