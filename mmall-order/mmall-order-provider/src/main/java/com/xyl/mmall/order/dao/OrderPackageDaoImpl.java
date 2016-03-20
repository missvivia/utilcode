package com.xyl.mmall.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.meta.OrderPackage;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:24:50
 * 
 */
@Repository
public class OrderPackageDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderPackage> implements OrderPackageDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getListByOrderIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<OrderPackage> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getListByUserIdWithState(long,
	 *      com.xyl.mmall.order.enums.OrderPackageState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderPackage> getListByUserIdWithState(long userId, OrderPackageState[] states, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamArray(sql, "orderPackageState", states);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getListWithMinPackageId(long,
	 *      long[], com.xyl.mmall.order.enums.OrderPackageState[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderPackage> getListWithMinPackageId(long minPackageId, long[] orderTimeRange,
			OrderPackageState[] opStateArray, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "orderPackageState", opStateArray);
		sql.append(" And packageId>").append(minPackageId);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.ystore.core.dao.order.OrderPackageDao#getListByOrderId(long,
	 *      long)
	 */
	public List<OrderPackage> getListByOrderId(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getListByOrderIdWithState(long,
	 *      long, com.xyl.mmall.order.enums.OrderPackageState[])
	 */
	@Override
	public List<OrderPackage> getListByOrderIdWithState(long userId, long orderId, OrderPackageState[] states) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "orderPackageState", states);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getOrderPackageNumWithState(long,
	 *      long, com.xyl.mmall.order.enums.OrderPackageState[])
	 */
	@Override
	public int getOrderPackageNumWithState(long userId, long orderId, OrderPackageState[] states) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamArray(sql, "orderPackageState", states);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#getListByMailNO(java.lang.String)
	 */
	@Override
	public List<OrderPackage> getListByMailNO(String mailNO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "mailNO", mailNO);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.space.framework.dao.sql.AbstractDaoSqlBase#getObjectFromRs(java.sql.ResultSet)
	 */
	public OrderPackage getObjectFromRs(ResultSet rs) throws SQLException {
		OrderPackage obj = super.getObjectFromRs(rs);
		return obj;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#setMailNO(com.xyl.mmall.order.meta.OrderPackage)
	 */
	@Override
	public boolean setMailNO(OrderPackage obj) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("packageId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("mailNO");
		setOfUpdate.add("expressCompany2");
		setOfUpdate.add("ExpSTime");

		return SqlGenUtil.update(getTableName(), setOfUpdate, setOfWhere, obj, getSqlSupport());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#updateOrderPackageStateAndConfirmTime(com.xyl.mmall.order.meta.OrderPackage,
	 *      com.xyl.mmall.order.enums.OrderPackageState)
	 */
	public boolean updateOrderPackageStateAndConfirmTime(OrderPackage obj, OrderPackageState oldState) {
		OrderPackageState[] validNewStateArray = { OrderPackageState.SIGN_IN };
		if (oldState == null || !CollectionUtil.isInArray(validNewStateArray, obj.getOrderPackageState()))
			return false;
		if (obj.getConfirmTime() <= 0)
			obj.setConfirmTime(System.currentTimeMillis());
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("packageId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderPackageState");
		setOfUpdate.add("confirmTime");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamObject(sql, "orderPackageState", oldState);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#updateOrderPackageState(com.xyl.mmall.order.meta.OrderPackage,
	 *      com.xyl.mmall.order.enums.OrderPackageState)
	 */
	public boolean updateOrderPackageState(OrderPackage obj, OrderPackageState oldState) {
		if (oldState == null)
			return false;
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("packageId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderPackageState");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamObject(sql, "orderPackageState", oldState);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#updateOrderPackageStateAndCancelTime(com.xyl.mmall.order.meta.OrderPackage,
	 *      com.xyl.mmall.order.enums.OrderPackageState)
	 */
	public boolean updateOrderPackageStateAndCancelTime(OrderPackage obj, OrderPackageState oldState) {
		OrderPackageState[] validNewStateArray = OrderPackageState.getCancelArray();
		if (oldState == null || !CollectionUtil.isInArray(validNewStateArray, obj.getOrderPackageState()))
			return false;
		if (obj.getCancelTime() <= 0)
			obj.setCancelTime(System.currentTimeMillis());
		return updateOrderPackageStateAndCancelTimeWithoutCheck(obj, oldState);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageDao#updateOrderPackageStateAndZeroCancelTime(com.xyl.mmall.order.meta.OrderPackage,
	 *      com.xyl.mmall.order.enums.OrderPackageState)
	 */
	public boolean updateOrderPackageStateAndZeroCancelTime(OrderPackage obj, OrderPackageState oldState) {
		obj.setCancelTime(0);
		return updateOrderPackageStateAndCancelTimeWithoutCheck(obj, oldState);
	}

	/**
	 * 更新包裹状态+取消时间
	 * 
	 * @param obj
	 * @param oldState
	 * @return
	 */
	public boolean updateOrderPackageStateAndCancelTimeWithoutCheck(OrderPackage obj, OrderPackageState oldState) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("packageId");
		setOfWhere.add("userId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("orderPackageState");
		setOfUpdate.add("cancelTime");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		SqlGenUtil.appendExtParamObject(sql, "orderPackageState", oldState);
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}