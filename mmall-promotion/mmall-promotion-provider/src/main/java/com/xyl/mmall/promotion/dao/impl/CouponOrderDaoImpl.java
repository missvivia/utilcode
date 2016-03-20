/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.CouponOrderDao;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CouponOrderDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository
public class CouponOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CouponOrder> implements CouponOrderDao {

	@Override
	public CouponOrder addCouponOrder(CouponOrder couponOrder) {
		couponOrder.setId(this.allocateRecordId());
		return this.addObject(couponOrder);
	}

	@Override
	public List<CouponOrder> getCouponOrderByCodeOfUseType(long userId, String couponCode,
			CouponOrderType couponOrderType) {
		if (StringUtils.isBlank(couponCode))
			return null;
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "CouponCode", couponCode);
		SqlGenUtil.appendExtParamObject(sql, "CouponOrderType", couponOrderType);
		return this.queryObjects(sql);
	}

	@Override
	public boolean updateCouponOrder(CouponOrder couponOrder) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("CouponOrderType");
		fieldNameSetOfUpdate.add("CouponHandlerType");
		fieldNameSetOfUpdate.add("UserCouponId");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("Id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, couponOrder,
				getSqlSupport());
	}

	@Override
	public Map<CouponOrderType, List<CouponOrder>> getMapByOrderId(long userId, long orderId) {
		List<CouponOrder> list = getObjectByOrderAndUserId(orderId, userId);
		Map<CouponOrderType, List<CouponOrder>> map = new HashMap<CouponOrderType, List<CouponOrder>>();
		if (CollectionUtil.isNotEmptyOfCollection(list)) {
			for (CouponOrder co : list) {
				CollectionUtil.putValueOfListMap(map, co.getCouponOrderType(), co, false);
			}
		}
		return map;
	}

	@Override
	public List<CouponOrder> getObjectByOrderAndUserId(long orderId, long userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "OrderId", orderId);
		return this.queryObjects(sql);
	}

	@Override
	public List<CouponOrder> getCouponOrderByUserId(long userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		return this.queryObjects(sql);
	}

	@Override
	public List<CouponOrder> getCouponOrderByUserAndCouponId(long userId, long usercouponId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "UserCouponId", usercouponId);
		return this.queryObjects(sql);
	}

	@Override
	public List<CouponOrder> getCouponOrdersByUserIdAndOrderIds(long userId,
			List<Long> orderIds) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamColl(sql, "OrderId", orderIds);
		return this.queryObjects(sql);
	}

}
