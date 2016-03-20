package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.HBRecycleState;
import com.xyl.mmall.order.meta.HBRecycleLog;

@Repository("hbRecycleLogDao")
public class HBRecycleLogDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<HBRecycleLog> implements HBRecycleLogDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.HBRecycleLogDao#getReturnedButNotRecycledObjects(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<HBRecycleLog> getReturnedButNotRecycledObjects(long minRetPkgId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "hbRecycleState", HBRecycleState.WAITING_RECYCLING);
		sql.append(" And retPkgId > ").append(minRetPkgId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.HBRecycleLogDao#recycleHb(com.xyl.mmall.order.meta.HBRecycleLog)
	 */
	@Override
	public boolean recycleHb(HBRecycleLog hbRecycleLog) {
		if(null == hbRecycleLog) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("retPkgId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("hbRecycleState");
		hbRecycleLog.setHbRecycleState(HBRecycleState.RECYCLED);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, hbRecycleLog));
		SqlGenUtil.appendExtParamObject(sql, "hbRecycleState", HBRecycleState.WAITING_RECYCLING);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
}
