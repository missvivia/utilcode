package com.xyl.mmall.content.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.meta.NCSContentDispatchLog;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
@Repository
public class NCSContentDispatchLogDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<NCSContentDispatchLog> implements NCSContentDispatchLogDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.NCSContentDispatchLogDao#getDispatchLogByTypeAndStateWithMinId(long, com.xyl.mmall.content.enums.ContentType[], com.xyl.mmall.content.enums.NCSIndexDispatchState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<NCSContentDispatchLog> getDispatchLogByTypeAndStateWithMinId(long minId, 
			ContentType[] contentTypes, NCSIndexDispatchState[] states, DDBParam param) {
		if(null == param) {
			param = new DDBParam("id", true, 10, 0);
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "contentType", contentTypes);
		SqlGenUtil.appendExtParamArray(sql, "dispatchState", states);
		sql.append(" AND id > ").append(minId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.content.dao.NCSContentDispatchLogDao#updateDispatchLog(com.xyl.mmall.content.meta.NCSContentDispatchLog, com.xyl.mmall.content.enums.NCSIndexDispatchState, com.xyl.mmall.content.enums.NCSIndexDispatchState[])
	 */
	@Override
	@Transaction
	public boolean updateDispatchLog(NCSContentDispatchLog log, NCSIndexDispatchState newState, NCSIndexDispatchState[] oldStates) {
		if(null == log) {
			return false;
		}
		log = getLockByKey(log);
		if(null == log) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("dispatchState");
		log.setDispatchState(newState);
		setOfUpdate.add("updateTime");
		log.setUpdateTime(System.currentTimeMillis());
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, log));
		SqlGenUtil.appendExtParamArray(sql, "dispatchState", oldStates);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
}
