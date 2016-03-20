/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.UserRedPacketDao;
import com.xyl.mmall.promotion.meta.UserRedPacket;

/**
 * UserRedPacketDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */

@Repository
public class UserRedPacketDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserRedPacket> implements UserRedPacketDao {
	
	private static final String GET_TOTAL_CASH = "SELECT SUM(RemainCash) AS TotalCash FROM Mmall_Promotion_UserRedPacket WHERE UserId = ? AND validStartTime < ? AND validEndTime > ? AND State = 0 AND Visible = 1";
	
	@Override
	public UserRedPacket addUserRedPacket(UserRedPacket userRedPacket) {
		userRedPacket.setId(this.allocateRecordId());
		return this.addObject(userRedPacket);
	}

	@Override
	public List<UserRedPacket> getUserRedPacketList(long userId, int state, int limit, int offset) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());

		buildQuerySql(userId, state, sb);
		
		if (state >= 0) {
			//获取有效的按照失效日期排列，保证优先使用快过期的红包
			sb.append(" ORDER BY ValidStartTime");
		} else {
			sb.append(" ORDER BY ValidStartTime desc, State, ValidEndTime");
		}
		
		if (limit != Integer.MAX_VALUE) {
			appendLimitSql(sb, limit, offset);
		}

		return this.queryObjects(sb);
	}
	
	@Override
	public int getUserRedPacketCount(long userId, int state) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genCountSql());

		buildQuerySql(userId, state, sb);
		return this.getSqlSupport().queryCount(sb.toString());
	}
	
	private void buildQuerySql(long userId, int state, StringBuilder sql) {
		if (userId > 0) {
			SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		}
		
		long currentTime = System.currentTimeMillis();
		long interval = 1000 * 60 * 60 * 24 * 7L;
		
		if (state == ActivationConstants.STATE_CAN_USE) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidStartTime <=").append(currentTime);
			sql.append(" AND ValidEndTime > ").append(currentTime);
			sql.append(" AND RemainCash > 0");
		} else if (state == ActivationConstants.STATE_EXPIRED) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidEndTime < ").append(currentTime);
		} else if (state == ActivationConstants.STATE_HAS_BEAN_USED) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_HAS_BEAN_USED);
		} else if (state == ActivationConstants.STATE_NOT_TAKE_EFFECT) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidStartTime >").append(currentTime);
			sql.append(" AND RemainCash > 0");
		} else if (state == ActivationConstants.STATE_INACTIVE) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_INACTIVE);
		}
		SqlGenUtil.appendExtParamObject(sql, "Visible", Boolean.TRUE);
		//失效7天前的不显示
		sql.append(" AND ValidEndTime > ").append(currentTime - interval);
	}

	@Override
	public boolean updateUserRedPacket(UserRedPacket userRedPacket) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("UsedCash");
		fieldNameSetOfUpdate.add("RemainCash");
		fieldNameSetOfUpdate.add("State");
		fieldNameSetOfUpdate.add("ValidStartTime");
		fieldNameSetOfUpdate.add("ValidEndTime");
		fieldNameSetOfUpdate.add("Visible");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("Id");
		fieldNameSetOfWhere.add("UserId");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, userRedPacket,
				getSqlSupport());
	}

	@Override
	public UserRedPacket getUserRedPacketById(long id, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "Id", id);
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "Visible", Boolean.TRUE);
		return this.queryObject(sql);
	}

	@Override
	public List<UserRedPacket> getUserRedPacketList(long userId, long timestamp, int count) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "Visible", Boolean.TRUE);
		sql.append(" AND ValidStartTime > ").append(timestamp);
		sql.append(" ORDER BY ValidStartTime, ValidEndTime DESC");
		appendLimitSql(sql, count, 0);
		return this.queryObjects(sql);
	}

	@Override
	public BigDecimal getTotalCash(long userId) {
		long current = System.currentTimeMillis();
		DBResource resource = this.getSqlSupport().excuteQuery(GET_TOTAL_CASH, userId, current, current);
		ResultSet rs = null;
		try {
			rs = resource.getResultSet();
			if (rs.next()) {
				return rs.getBigDecimal("TotalCash");
			}
		} catch (SQLException e) {
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (resource != null) {
					resource.close();
				}
			} catch (SQLException e) {
			}
		}
		return BigDecimal.ZERO;
	}

	@Override
	public boolean deleteByDetailId(long detailId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketDetailId", detailId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public List<UserRedPacket> getUserRedPacketListByDetailId(long detailId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketDetailId", detailId);
		return this.queryObjects(sql);
	}

}
