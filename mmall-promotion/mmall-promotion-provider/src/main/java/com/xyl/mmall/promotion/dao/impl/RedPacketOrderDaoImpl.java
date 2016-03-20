/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.promotion.dao.RedPacketOrderDao;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.RedPacketOrder;

/**
 * RedPacketOrderDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-28
 * @since 1.0
 */
@Repository
public class RedPacketOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RedPacketOrder> implements
		RedPacketOrderDao {

	@Override
	public boolean addRedPacketOrder(RedPacketOrder redPacketOrder) {
		redPacketOrder.setId(this.allocateRecordId());
		return this.addObject(redPacketOrder) != null;
	}

	@Override
	public RedPacketOrder getRedPacketOrderByIds(long userId, long orderId, long userRedPacketId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "OrderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "UserRedPacketId", userRedPacketId);
		return this.queryObject(sql);
	}

	@Override
	public boolean updateRedPacketOrder(RedPacketOrder redPacketOrder) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Cash");
		fieldNameSetOfUpdate.add("redPacketOrderType");
		fieldNameSetOfUpdate.add("redPacketHandlerType");
		fieldNameSetOfUpdate.add("UserRedPacketId");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("Id");
		fieldNameSetOfWhere.add("UserId");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, redPacketOrder,
				getSqlSupport());
	}

	@Override
	public List<RedPacketOrder> getUserRedPacketOrderList(long userId, long userRedPacketId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "UserRedPacketId", userRedPacketId);
		sql.append(" ORDER BY UsedTime DESC");
		return this.queryObjects(sql);
	}

	@Override
	public List<RedPacketOrder> getRedPacketOrderList(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "OrderId", orderId);
		return this.queryObjects(sql);
	}

	@Override
	public List<RedPacketOrder> getRedPacketOrderListByDate(long minId, String time, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND Id > ").append(minId);
		sql.append(" AND RedPacketOrderType = ").append(RedPacketOrderType.RETURN_RED_PACKET.getIntValue());
		sql.append(" AND RedPacketHandlerType = ").append(ActivationHandlerType.GRANT.getIntValue());
		sql.append(" AND CreateTime >= date_sub('").append(time).append("',interval 1 day)");
		sql.append(" AND CreateTime < '").append(time).append("'");
		SqlGenUtil.appendDDBParam(sql, param, defaultOrderColumn);
		return this.queryObjects(sql);
	}

	@Override
	public boolean deleteByRedPacketId(long redPacketId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "RedPacketId", redPacketId);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > -1;
	}

}
