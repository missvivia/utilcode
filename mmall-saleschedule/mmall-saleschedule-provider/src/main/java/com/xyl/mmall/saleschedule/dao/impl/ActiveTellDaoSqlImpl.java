package com.xyl.mmall.saleschedule.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.saleschedule.dao.ActiveTellDao;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;

@Repository
public class ActiveTellDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<ActiveTell> implements ActiveTellDao {

	private static final String SELECT_HEADER = "select * from ";

	private static final String WHERE_CLAUSE = " WHERE 1=1 ";

	public List<ActiveTell> getActiveTellByParam(ActiveTellCommonParamDTO paramSo) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SELECT_HEADER + this.getTableName() + WHERE_CLAUSE);

		if (paramSo.getTellActiveType() != null) {
			this.composeSqlByParam(sql, "tellActiveType", paramSo.getTellActiveType());
		}

		if (paramSo.getActiveBeginTime() != null) {
			this.composeSqlByParam(sql, "activeBeginTime", paramSo.getActiveBeginTime());
		}

		if (paramSo.getTellActiveId() != null) {
			this.composeSqlByParam(sql, "tellActiveId", paramSo.getTellActiveId());
		}

		if (paramSo.getAreaId() != null) {
			this.composeSqlByParam(sql, "areaId", paramSo.getAreaId());
		}

		if (paramSo.getTellTargetType() != null) {
			this.composeSqlByParam(sql, "tellTargetType", paramSo.getTellTargetType());
		}

		if (paramSo.getTellTargetValue() != null) {
			this.composeSqlByParam(sql, "tellTargetValue", paramSo.getTellTargetValue());
		}

		return super.queryObjects(sql);
	}

	private void composeSqlByParam(StringBuilder mainSqlBuffer, String fieldName, Object fieldValue) {
		mainSqlBuffer.append(" AND " + fieldName + "=");
		if (fieldValue instanceof String) {
			mainSqlBuffer.append(" '" + fieldValue + "'");
		} else {
			mainSqlBuffer.append(fieldValue);
		}
	}

}
