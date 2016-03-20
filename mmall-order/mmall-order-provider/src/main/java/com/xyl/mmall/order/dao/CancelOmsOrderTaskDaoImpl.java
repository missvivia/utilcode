package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.meta.CancelOmsOrderTask;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class CancelOmsOrderTaskDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<CancelOmsOrderTask> implements
		CancelOmsOrderTaskDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.CancelOmsOrderTaskDao#queryCancelOmsOrderTaskListWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.CancelOmsOrderTaskState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<CancelOmsOrderTask> queryCancelOmsOrderTaskListWithMinOrderId(long minOrderId,
			CancelOmsOrderTaskState[] taskStateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "taskState", taskStateArray);
		sql.append(" And OrderId>").append(minOrderId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.CancelOmsOrderTaskDao#queryCancelOmsOrderTask(long,
	 *      long)
	 */
	public CancelOmsOrderTask queryCancelOmsOrderTask(long orderId, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.CancelOmsOrderTaskDao#updateCancelOmsOrderTaskState(com.xyl.mmall.order.meta.CancelOmsOrderTask,
	 *      com.xyl.mmall.order.enums.CancelOmsOrderTaskState)
	 */
	public boolean updateCancelOmsOrderTaskState(CancelOmsOrderTask cancelTask, CancelOmsOrderTaskState oldState) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("taskState");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, cancelTask));
		SqlGenUtil.appendExtParamObject(sql, "taskState", oldState);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
