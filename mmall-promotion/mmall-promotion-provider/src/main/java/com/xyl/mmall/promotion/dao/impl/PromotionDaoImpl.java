/*
 * 2014-9-9
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
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dao.PromotionDao;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * PromotionDaoImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Repository
public class PromotionDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Promotion> implements PromotionDao {

	@Override
	public List<Promotion> getPromotionList(long userId, boolean isNoExpiredOnly, int state, long fullPermission,
			long areaPermission, PlatformType platformType, int limit, int offset) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());
		buildQuerySql(userId, isNoExpiredOnly, state, fullPermission, areaPermission, platformType, sb);

		sb.append(" ORDER BY CreateTime DESC ");
		if (limit > 0 && limit != Integer.MAX_VALUE) {
			appendLimitSql(sb, limit, offset);
		}

		return this.queryObjects(sb);
	}

	@Override
	public int getPromotionCount(long userId, boolean isNoExpiredOnly, int state, long areaPermission) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genCountSql());
		buildQuerySql(userId, isNoExpiredOnly, state, 0, areaPermission, null, sb);

		return this.getSqlSupport().queryCount(sb.toString());
	}

	private void buildQuerySql(long userId, boolean isNoExpiredOnly, int state, long fullPermission,
			long selectedPermission, PlatformType platformType, StringBuilder sb) {
		if (userId > 0) {
			SqlGenUtil.appendExtParamObject(sb, "ApplyUserId", userId);
		}
		
		if (state >= 0) {
			sb.append(" AND ");
			SqlGenUtil.appendExtParamObject(sb, "AuditState", state);

		}
		
		if (platformType != null) {
			sb.append(" AND (PlatformType = ").append(PlatformType.ALL.getIntValue()).append(" OR PlatformType = ").append(platformType.getIntValue()).append(")");
		}
		
		if (isNoExpiredOnly) {
			long current = System.currentTimeMillis();
			sb.append(" AND StartTime <=").append(current);
			sb.append(" AND EndTime > ").append(current);
		}

		if (fullPermission > 0) {
			sb.append(fullPermission).append("&AreaPermission=AreaPermission");
		}

		if (selectedPermission > 0) {
			sb.append(" AND  AreaPermission&").append(selectedPermission).append("=").append(selectedPermission);
		}
	}

	@Override
	public Promotion addPromotion(Promotion promotion) {
		promotion.setId(this.allocateRecordId());
		return this.addObject(promotion);
	}

	@Override
	public boolean updatePromotion(Promotion promotion) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("Name");
		fieldNameSetOfUpdate.add("Description");
		fieldNameSetOfUpdate.add("SelectedProvince");
		fieldNameSetOfUpdate.add("StartTime");
		fieldNameSetOfUpdate.add("EndTime");
		fieldNameSetOfUpdate.add("UpdateTime");
		fieldNameSetOfUpdate.add("ApplyUserId");
		fieldNameSetOfUpdate.add("Items");
		fieldNameSetOfUpdate.add("AuditState");
		fieldNameSetOfUpdate.add("Labels");
		fieldNameSetOfUpdate.add("AuditUserId");
		fieldNameSetOfUpdate.add("AuditTime");
		fieldNameSetOfUpdate.add("DeclarePO");
		fieldNameSetOfUpdate.add("PlatformType");
		fieldNameSetOfUpdate.add("FavorType");
		fieldNameSetOfUpdate.add("AreaRemainPermission");
		fieldNameSetOfUpdate.add("AreaMulriplePermission");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("id");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, promotion, getSqlSupport());
	}

	@Override
	public Promotion getPromotionById(long id) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());
		SqlGenUtil.appendExtParamObject(sb, "Id", id);

		return this.queryObject(sb);
	}

	@Override
	public Promotion getPromotionByPO(String po, long id, long start, long end, boolean isValid) {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.genSelectSql());

		if (isValid) {
			long current = System.currentTimeMillis();
			SqlGenUtil.appendExtParamObject(sb, "AuditState", StateConstants.PASS);
			sb.append(" AND StartTime <=").append(current);
			sb.append(" AND EndTime > ").append(current);
		}

		if (start > 0 && end > 0) {
			// 获取重叠部分
			sb.append(" AND EndTime >=").append(start).append(" AND StartTime <=").append(end);
		}

		if (id > 0) {
			sb.append(" AND Id != ").append(id);
		}
		sb.append(" AND AuditState != ").append(StateConstants.CANCEL);
		// 指定一个po
		sb.append(" AND (DeclarePO = ").append(po);
		sb.append(" OR ");
		// 指定所有的po，通配符
		sb.append(" DeclarePO = '").append(ActivationConstants.OVERALL).append("'");
		sb.append(" OR ");
		// 开始
		sb.append(" LOCATE('").append(po).append(",',DeclarePO) > 0");
		sb.append(" OR ");
		// 中间
		sb.append(" LOCATE(',").append(po).append(",',DeclarePO) > 0");
		sb.append(" OR ");
		// 结尾
		sb.append(" LOCATE(',").append(po).append("',DeclarePO) > 0)");

		appendLimitSql(sb, 1, 0);
		return this.queryObject(sb);
	}

	@Override
	public List<Promotion> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genSelectSql());
		buildCommitSql(sql, promotionQueryBeanDTO);
		appendOrderSql(sql, "CreateTime", false);
		if (promotionQueryBeanDTO.getLimit() > 0) {
			appendLimitSql(sql, promotionQueryBeanDTO.getLimit(), promotionQueryBeanDTO.getOffset());
		}
		return this.queryObjects(sql);
	}

	@Override
	public int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genCountSql());
		buildCommitSql(sql, promotionQueryBeanDTO);
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private void buildCommitSql(StringBuilder sql, PromotionQueryBeanDTO promotionQueryBeanDTO) {
		int state = promotionQueryBeanDTO.getAuditState();
		if (state >= 0) {
			sql.append(" AND ");
			SqlGenUtil.appendExtParamObject(sql, "AuditState", state);

		}
		long selected = promotionQueryBeanDTO.getAreaPermission();
		long full = promotionQueryBeanDTO.getFullPermission();

		if (selected > 0) {
			sql.append(" AND  AreaPermission & ").append(selected).append("=").append(selected);
		}

		sql.append(" AND ").append(full).append("&AreaPermission=AreaPermission");
	}
}
