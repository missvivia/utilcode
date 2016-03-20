package com.xyl.mmall.order.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.meta.base.IncrField;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.OrderPackageRefundTaskState;
import com.xyl.mmall.order.meta.OrderPackageRefundTask;

/**
 * 
 * @author dingmingliang
 * 
 */
@Repository
public class OrderPackageRefundTaskDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderPackageRefundTask> implements
		OrderPackageRefundTaskDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageRefundTaskDao#updateRetryFlag(com.xyl.mmall.order.meta.OrderPackageRefundTask,
	 *      long)
	 */
	public boolean updateRetryFlag(OrderPackageRefundTask obj, long retryFlagOfOld) {
		String extWhereSql = " retryFlag=" + retryFlagOfOld;
		IncrField<Integer> ifItem = new IncrField<Integer>("retryCount", 1);
		Collection<IncrField<Integer>> ifColl = CollectionUtil.addOfList(null, ifItem);

		return PrintDaoUtil.updateObjectByKey(obj, Arrays.asList(new String[] { "retryFlag", "state" }), null, ifColl,
				extWhereSql, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageRefundTaskDao#getListByStateWithMinId(long,
	 *      com.xyl.mmall.order.enums.OrderPackageRefundTaskState,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderPackageRefundTask> getListByStateWithMinId(long minId, OrderPackageRefundTaskState state,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		sql.append(" AND id>").append(minId);
		return getListByDDBParam(sql.toString(), param);
	}

}