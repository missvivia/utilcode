/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dao.UserCouponDao;
import com.xyl.mmall.promotion.meta.UserCoupon;

/**
 * UserCouponService.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository("userCouponDao")
public class UserCouponDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserCoupon> implements UserCouponDao {

	private static Logger logger = Logger.getLogger(UserCouponDaoImpl.class);
	
	@Override
	public List<UserCoupon> getUserCouponListByState(long userId, int state, int limit, int offset) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		genUserCouponSql(userId, state, sql);
		if (state > -1) {
			sql.append(" ORDER BY ValidStartTime, Id");
		} else {
			sql.append(" ORDER BY State, ValidEndTime, ValidStartTime DESC, Id DESC");
		}
		
		if (limit != Integer.MAX_VALUE) {
			sql.append(" Limit ").append(limit);
			sql.append(" OFFSET ").append(offset);
		}

		return queryObjects(sql);
	}

	@Override
	public int getUserCouponCount(long userId, int state) {
		StringBuilder sql = new StringBuilder();
		sql.append(genCountSql());
		genUserCouponSql(userId, state, sql);

		return this.getSqlSupport().queryCount(sql.toString());
	}

	private void genUserCouponSql(long userId, int state, StringBuilder sql) {
		long currentTime = System.currentTimeMillis();
//		long interval = 1000 * 60 * 60 * 24 * 7L;
		if (userId > 0) {
			SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		}
		
		if (state == ActivationConstants.STATE_CAN_USE) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidStartTime <=").append(currentTime);
			sql.append(" AND ValidEndTime > ").append(currentTime);
		} else if (state == ActivationConstants.STATE_EXPIRED) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidEndTime < ").append(currentTime);
		} else if (state == ActivationConstants.STATE_HAS_BEAN_USED) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_HAS_BEAN_USED);
		} else if (state == ActivationConstants.STATE_NOT_TAKE_EFFECT) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_CAN_USE);
			sql.append(" AND ValidStartTime >").append(currentTime);
		} else if (state == ActivationConstants.STATE_INACTIVE) {
			SqlGenUtil.appendExtParamObject(sql, "State", ActivationConstants.STATE_INACTIVE);
		}
		
		//失效7天前的不显示
//		sql.append(" AND ValidEndTime > ").append(currentTime - interval);
	}

	@Override
	public UserCoupon addUserCoupon(UserCoupon userCoupon) {
		if (userCoupon == null) {
			return null;
		}
		
		userCoupon.setId(this.allocateRecordId());
		return this.addObject(userCoupon);
	}

	@Override
	public List<UserCoupon> getUserCouponList(long userId, long timestamp, int count) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		sql.append(" AND ValidStartTime > ").append(timestamp);
		sql.append(" ORDER BY ValidStartTime, ValidEndTime DESC ");
		sql.append(" Limit ").append(count);

		return queryObjects(sql);
	}

	@Override
	public boolean updateUserCoupon(UserCoupon userCoupon) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("State");
		fieldNameSetOfUpdate.add("ValidStartTime");
		fieldNameSetOfUpdate.add("ValidEndTime");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("Id");

		return SqlGenUtil
				.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, userCoupon, getSqlSupport());
	}

	@Override
	public int getUserCouponCountByCode(String couponCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public UserCoupon getUserCouponsByState(long userId, long userCouponId, int state, Boolean isValid) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "Id", userCouponId);
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		

		if (state > -1) {
			SqlGenUtil.appendExtParamObject(sql, "State", state);
		}
		
		if (isValid != null) {
			long current = System.currentTimeMillis();
			if (isValid) {
				sql.append(" AND validStartTime <= ").append(current);
				sql.append(" AND validEndTime > ").append(current);
			} else {
				sql.append(" AND (validStartTime > ").append(current);
				sql.append(" OR validEndTime < ").append(current).append(")");
			}
		}
		
		//按有效期结束时间排列，保证最先失效在前
		appendOrderSql(sql, "validEndTime", true);
		
		return this.queryObject(sql);
	}

	@Override
	public int getUserCouponCountByCode(long userId, String couponCode, Date startDate, Date endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		if (startDate != null && endDate != null) {
			sql.append(" AND CreateTime >= ").append(startDate);
			sql.append(" AND CreateTime < ").append(endDate);
		}
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public boolean deleteUserCoupon(long userId, String couponCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public int getUserCouponCountBycreateTime(long userId, long time) {
		if(time == 0)
			return 0;
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		StringBuilder sql = new StringBuilder();
		sql.append(genCountSql());
		sql.append(" AND UserId = ").append(userId);
		sql.append(" AND CreateTime > '").append(dateString).append("'");
		sql.append(" AND ValidEndTime > NOW()");
		
		return this.getSqlSupport().queryCount(sql.toString());
	}

	@Override
	public boolean batchUpdateBindUserCouponByCode(String couponCode) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		SqlGenUtil.appendExtParamObject(sql, "State", 0);
		if (this.getSqlSupport().queryCount(sql.toString()) > 0) {
			sql = new StringBuilder("UPDATE ");
			sql.append(this.getTableName()).append(" SET State = ").append(ActivationConstants.STATE_INACTIVE);
			sql.append(" WHERE CouponCode = '").append(couponCode).append("' AND State = 0");
			return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
		} else {
			return true;
		}
	}

	@Override
	public boolean batchDeleteBindUserCouponByCode(String couponCode) {
		StringBuilder sql = new StringBuilder(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		if (this.getSqlSupport().queryCount(sql.toString()) > 0) {
			sql = new StringBuilder(genDeleteSql());
			sql.append("CouponCode = '").append(couponCode).append("'");
			return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
		} else {
			return true;
		}
	}
}
