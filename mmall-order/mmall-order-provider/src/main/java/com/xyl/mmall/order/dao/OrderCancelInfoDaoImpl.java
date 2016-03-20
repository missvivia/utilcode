package com.xyl.mmall.order.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.meta.base.IncrField;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelState;
import com.xyl.mmall.order.meta.OrderCancelInfo;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class OrderCancelInfoDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderCancelInfo> implements
		OrderCancelInfoDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#setCancelRType(com.xyl.mmall.order.meta.OrderCancelInfo, com.xyl.mmall.order.enums.OrderCancelRType[], com.xyl.mmall.order.enums.OrderCancelRType)
	 */
	@Override
	@Transaction
	public boolean setCancelRType(OrderCancelInfo obj, OrderCancelRType[] oldRTypes, OrderCancelRType newRType) {
		if(null == obj || null == oldRTypes || null == newRType) {
			return false;
		}
		obj = getLockByKey(obj);
		if(null == obj) {
			return false;
		}
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("rtype");
		obj.setRtype(newRType);
		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamArray(sql, "rtype", oldRTypes);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#setCancelState(com.xyl.mmall.order.meta.OrderCancelInfo)
	 */
	public boolean setCancelState(OrderCancelInfo obj) {
		return PrintDaoUtil.updateObjectByKey(obj, Arrays.asList(new String[] { "cancelState" }), null, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#setOrderCancelToDone()
	 */
	public boolean setOrderCancelToDone() {
		String sql = "UPDATE " + getTableName() + " SET CancelState = " + OrderCancelState.DONE.getIntValue()
				+ " WHERE RetryFlag=0 AND CancelState=" + OrderCancelState.CREATE.getIntValue();
		return this.getSqlSupport().excuteUpdate(sql)>0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#getListByStateWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.OrderCancelState,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderCancelInfo> getListByStateWithMinOrderId(long minOrderId, OrderCancelState cancelState,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "cancelState", cancelState);
		sql.append(" AND orderId>").append(minOrderId);
		SqlGenUtil.appendDDBParam(sql, param, defaultOrderColumn);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#getListByRTypeWithMinOrderId(long, com.xyl.mmall.order.enums.OrderCancelRType, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderCancelInfo> getListByRTypeWithMinOrderId(long minOrderId, OrderCancelRType cancelRType,
			DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "rtype", cancelRType);
		sql.append(" AND orderId>").append(minOrderId);
		SqlGenUtil.appendDDBParam(sql, param, defaultOrderColumn);
		return queryObjects(sql);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#updateRetryFlag(com.xyl.mmall.order.meta.OrderCancelInfo,
	 *      long)
	 */
	public boolean updateRetryFlag(OrderCancelInfo obj, long retryFlagOfOld) {
		String extWhereSql = " retryFlag=" + retryFlagOfOld;
		IncrField<Integer> ifItem = new IncrField<Integer>("retryCount", 1);
		Collection<IncrField<Integer>> ifColl = CollectionUtil.addOfList(null, ifItem);

		return PrintDaoUtil.updateObjectByKey(obj, Arrays.asList(new String[] { "retryFlag" }), null, ifColl,
				extWhereSql, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCancelInfoDao#incrRetryCount(com.xyl.mmall.order.meta.OrderCancelInfo)
	 */
	public boolean incrRetryCount(OrderCancelInfo obj) {
		IncrField<Integer> ifItem = new IncrField<Integer>("retryCount", 1);
		Collection<IncrField<Integer>> ifColl = CollectionUtil.addOfList(null, ifItem);

		return PrintDaoUtil.updateObjectByKey(obj, null, null, ifColl, null, this);
	}
}
