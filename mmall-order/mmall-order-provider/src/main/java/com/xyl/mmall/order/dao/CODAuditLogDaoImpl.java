package com.xyl.mmall.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.CODAuditState;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.CODAuditLog;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午11:03:48
 *
 */
@Repository(value = "CODAuditLogDao")
public class CODAuditLogDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CODAuditLog> 
	implements CODAuditLogDao {
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogByState(com.xyl.mmall.order.enums.CODAuditState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogByState(CODAuditState[] stateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogByStateWithUserIdAndOrderId(com.xyl.mmall.order.enums.CODAuditState[], long, long)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogByStateWithUserIdAndOrderId(CODAuditState[] stateArray, long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return getListByDDBParam(sql.toString(), null);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogByStateWithOrderIdList(com.xyl.mmall.order.enums.CODAuditState[], java.util.List, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogByStateWithOrderIdList(CODAuditState[] stateArray, List<Long> orderIdList,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		SqlGenUtil.appendExtParamArray(sql, "orderId", orderIdList.toArray());
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogByStateWithTimeRange(com.xyl.mmall.order.enums.CODAuditState[], long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogByStateWithTimeRange(CODAuditState[] stateArray, long start, long end, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		String sqlStr = sql.toString().trim();
		if (!(sqlStr.endsWith(" WHERE") || sqlStr.endsWith(" AND") || sqlStr.endsWith(" OR") || sqlStr.endsWith("("))) {
			sql.append(" AND ").append("ctime >= ").append(start)
			   .append(" AND ").append("ctime <= ").append(end);
		}
		return getListByDDBParam(sql.toString(), param);
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogByStateBeforeSomeTime(long, com.xyl.mmall.order.enums.CODAuditState[], com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogByStateBeforeSomeTime(long someTime, CODAuditState[] states,
			DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "auditState", states);
		sql.append(" AND ctime < ").append(someTime);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryCODAuditLogOfTimeout(long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryCODAuditLogOfTimeout(long someTime, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT A.* FROM Mmall_Order_CODAuditLog A, Mmall_Order_OrderForm B ")
		   .append("WHERE (")
		   .append("A.userId = B.userId").append(" AND ")
		   .append("A.orderId = B.orderId").append(" AND ")
		   .append("A.auditState = ").append(CODAuditState.REFUSED.getIntValue()).append(" AND ")
		   .append("A.updateTime < ").append(someTime).append(" AND ")
		   .append("B.orderFormState = ").append(OrderFormState.COD_AUDIT_REFUSE.getIntValue())
		   .append(")");
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#queryIllegalCODAuditLog(com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<CODAuditLog> queryIllegalCODAuditLog(DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT A.* FROM Mmall_Order_CODAuditLog A, Mmall_Order_OrderForm B ")
		   .append("WHERE (")
		   .append("A.userId = B.userId").append(" AND ")
		   .append("A.orderId = B.orderId").append(" AND ")
		   .append("A.auditState = ").append(CODAuditState.WAITING.getIntValue()).append(" AND ")
		   .append("B.orderFormState != ").append(OrderFormState.WAITING_COD_AUDIT.getIntValue())
		   .append(")");
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#updateCODAuditState(long, long, long, java.lang.String, boolean, com.xyl.mmall.order.enums.CODAuditState, com.xyl.mmall.order.enums.CODAuditState[])
	 */
	@Override
	public boolean updateCODAuditState(long userId, long auditLogId, long auditUserId, String extInfo, boolean byRobot, 
			CODAuditState newState, CODAuditState[] stateArray) {
		CODAuditLog log = getObjectByIdAndUserId(auditLogId, userId);
		if(null == log) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("auditUserId");
		log.setAuditUserId(auditUserId);
		setOfUpdate.add("auditState");
		log.setAuditState(newState);
		setOfUpdate.add("passedByRobot");
		log.setPassedByRobot(byRobot);
		if(null != extInfo) {
			setOfUpdate.add("extInfo");
			log.setExtInfo(extInfo);
		}
		setOfUpdate.add("updateTime");
		log.setUpdateTime(System.currentTimeMillis());
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, log));
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#updateCODAuditState(com.xyl.mmall.order.meta.CODAuditLog, java.lang.String, boolean, com.xyl.mmall.order.enums.CODAuditState, com.xyl.mmall.order.enums.CODAuditState[])
	 */
	@Override
	public boolean updateCODAuditState(CODAuditLog auditLog, String extInfo, CODAuditState newState, CODAuditState[] stateArray) {
		if(null == auditLog) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("id");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("auditState");
		auditLog.setAuditState(newState);
		if(null != extInfo) {
			setOfUpdate.add("extInfo");
			auditLog.setExtInfo(extInfo);
		}
		setOfUpdate.add("updateTime");
		auditLog.setUpdateTime(System.currentTimeMillis());
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, auditLog));
		SqlGenUtil.appendExtParamArray(sql, "auditState", stateArray);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.CODAuditLogDao#getWaitingAuditCount()
	 */
	@Override
	public Map<Integer, Long> getWaitingAuditCount() {
		Map<Integer, Long> retMap = new HashMap<Integer, Long>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT COUNT(id), provinceId FROM Mmall_Order_CODAuditLog WHERE auditState = ")
		   .append(CODAuditState.WAITING.getIntValue())
		   .append(" GROUP BY provinceId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				long count = rs.getLong("COUNT(id)");
				int provinceId = rs.getInt("provinceId");
				retMap.put(provinceId, count);
			}
		} catch (SQLException e) {
			return retMap;
		} finally {
			dbr.close();
		}
		return retMap;
	}
	
}
