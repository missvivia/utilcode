/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dao.CouponDao;
import com.xyl.mmall.promotion.meta.Coupon;

/**
 * CouponDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository
public class CouponDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Coupon> implements CouponDao {
	
	private static Logger logger = Logger.getLogger(CouponDaoImpl.class);
	
	private static final String GET_OBJECT_LOCK = "SELECT 1 FROM Mmall_Promotion_Coupon WHERE CouponCode = ? FOR UPDATE";
	
	@Override
	public List<Coupon> getCouponList(List<Long> ids, int state, String qvalue, int limit, int offset) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());

		buildQuerySql(ids, state, qvalue, sb);
		
		sb.append(" ORDER BY CreateTime DESC ");
		if (limit != Integer.MAX_VALUE) {
			appendLimitSql(sb, limit, offset);
		}

		return this.queryObjects(sb);
	}

	@Override
	public int getCouponCount(List<Long> ids, int state, String qvalue) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genCountSql());

		buildQuerySql(ids, state, qvalue, sb);

		return this.getSqlSupport().queryCount(sb.toString());
	}

	private void buildQuerySql(List<Long> ids, int state, String qvalue, StringBuilder sb) {
		if (CollectionUtils.isNotEmpty(ids)) {
			sb.append(" AND ApplyUserId IN (");
			for (Long id : ids) {
				sb.append(id).append(",");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
		}
		sb.append(" AND (RootCode = '' OR RootCode IS NULL)");
		if (state >= 0) {
			SqlGenUtil.appendExtParamObject(sb, "AuditState", state);
		}

		if (StringUtils.isNotBlank(qvalue)) {
			qvalue = qvalue.replaceAll("'", "\\\\'");
			sb.append(" AND ( Name like '%").append(qvalue).append("%'");
			sb.append(" OR CouponCode like '%").append(qvalue).append("%' )");
		}
	}

	@Override
	public Coupon addCoupon(Coupon coupon) {
		coupon.setId(this.allocateRecordId());
		return this.addObject(coupon);
	}

	@Override
	public boolean updateCoupon(Coupon coupon) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("CouponCode");
		fieldNameSetOfUpdate.add("Name");
		fieldNameSetOfUpdate.add("Description");
		fieldNameSetOfUpdate.add("SelectedProvince");
		fieldNameSetOfUpdate.add("StartTime");
		fieldNameSetOfUpdate.add("EndTime");
		fieldNameSetOfUpdate.add("UpdateTime");
		fieldNameSetOfUpdate.add("ApplyUserId");
		fieldNameSetOfUpdate.add("CodeType");
		fieldNameSetOfUpdate.add("AuditState");
		fieldNameSetOfUpdate.add("Items");
		fieldNameSetOfUpdate.add("AuditUserId");
		fieldNameSetOfUpdate.add("AuditTime");
		fieldNameSetOfUpdate.add("RandomCount");
		fieldNameSetOfUpdate.add("Reason");
		fieldNameSetOfUpdate.add("Times");
		fieldNameSetOfUpdate.add("TimesType");
		fieldNameSetOfUpdate.add("Users");
		fieldNameSetOfUpdate.add("BinderType");
		fieldNameSetOfUpdate.add("RootCode");
		fieldNameSetOfUpdate.add("FavorType");
		fieldNameSetOfUpdate.add("AreaIds");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, coupon, getSqlSupport());
	}

	@Override
	public Coupon getCouponByCode(String couponCode, boolean isValidOnly) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sb, "CouponCode", couponCode);

		buildWhereCondition(isValidOnly, sb);

		return this.queryObject(sb);
	}

	private void buildWhereCondition(boolean isValidOnly, StringBuilder sb) {
		if (isValidOnly) {
			long current = System.currentTimeMillis();
			sb.append(" AND StartTime <= ").append(current);
			sb.append(" AND EndTime > ").append(current);
			sb.append(" AND AuditState = ").append(StateConstants.PASS);
		}
	}

	@Override
	public Coupon getCouponById(long id, boolean isValidOnly) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sb, "Id", id);

		buildWhereCondition(isValidOnly, sb);

		return this.queryObject(sb);
	}

	@Override
	public List<Coupon> getRandomCoupons(String rootCode, int count) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "RootCode", rootCode);
		appendLimitSql(sql, count, 0);
		return this.queryObjects(sql);
	}

	@Override
	public int getObjectLockByCode(String couponCode) {
		DBResource resource = this.getSqlSupport().excuteQuery(GET_OBJECT_LOCK, couponCode);
		ResultSet rs = null;
		try {
			rs = resource.getResultSet();
			if (rs.next()) {
				return rs.getInt(1);
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
		return 0;
	}

	@Override
	public boolean deleteRamdomCoupon(String rootCode) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "RootCode", rootCode);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
}
