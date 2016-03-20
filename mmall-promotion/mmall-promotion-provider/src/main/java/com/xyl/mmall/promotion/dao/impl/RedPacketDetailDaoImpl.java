/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketDetailDao;
import com.xyl.mmall.promotion.meta.RedPacketDetail;

/**
 * RedPacketDetailDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@Repository
public class RedPacketDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketDetail> implements
		RedPacketDetailDao {

	private static final String COPIES_MINUS_ONE = "UPDATE Mmall_Promotion_RedPacketDetail SET Copies = Copies - 1 WHERE Id = ? AND RedPacketId = ? AND Copies > 0";

	@Override
	public RedPacketDetail addRedPacketDetail(RedPacketDetail detail) {
		detail.setId(this.allocateRecordId());
		return this.addObject(detail);
	}

	@Override
	public RedPacketDetail getRamdomDetailByRedPacketId(long redPacketId, int groupId, Boolean visible) {
		long current = System.currentTimeMillis();
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketId", redPacketId);
		if (groupId > 0) {
			SqlGenUtil.appendExtParamObject(sql, "GroupId", groupId);
		}
		
		if (visible != null) {
			SqlGenUtil.appendExtParamObject(sql, "Visible", visible);	
		}
		
		sql.append(" AND copies > 0");
		sql.append(" AND validStartTime <= ").append(current);
		sql.append(" AND validEndTime > ").append(current);
		appendLimitSql(sql, 1, 0);

		return this.queryObject(sql);
	}

	@Override
	public boolean setCopiesMinusOne(RedPacketDetail detail) {
		return this.getSqlSupport().excuteUpdate(COPIES_MINUS_ONE, detail.getId(), detail.getRedPacketId()) > 0;
	}

	@Override
	public RedPacketDetail getDetailById(long redPacketDetailId) {
		return this.getObjectById(redPacketDetailId);
	}

	@Override
	public List<RedPacketDetail> getDetailListByPacketId(long redPacketId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketId", redPacketId);
		return this.queryObjects(sql);
	}

	@Override
	public boolean deleteDetailById(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "Id", id);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public boolean updateRedPacketDetail(RedPacketDetail detail) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Visible");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, detail, getSqlSupport());
	}
}
