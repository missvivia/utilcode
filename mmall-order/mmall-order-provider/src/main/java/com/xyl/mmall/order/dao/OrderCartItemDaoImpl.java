package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderCartItem;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午9:50:29
 * 
 */
@Repository
public class OrderCartItemDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderCartItem> implements OrderCartItemDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#getListByOrderIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<OrderCartItem> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl) {
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
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#getListByOrderId(long,
	 *      long)
	 */
	public List<OrderCartItem> getListByOrderId(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#getListByOrderPackageId(long, long)
	 */
	@Override
	public List<OrderCartItem> getListByOrderPackageId(long userId, long ordPkgId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "packageId", ordPkgId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#updatePackageId(java.util.List,
	 *      long)
	 */
	public boolean updatePackageId(List<OrderCartItem> objList, long packageId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("UPDATE " + getTableName() + " SET PackageId=").append(packageId).append(" WHERE 1=0 ");
		for (OrderCartItem obj : objList) {
			sql.append(" OR ( ");
			SqlGenUtil.appendExtParamObject(sql, "id", obj.getId());
			SqlGenUtil.appendExtParamObject(sql, "userId", obj.getUserId());
			sql.append(" )");
		}
		return getSqlSupport().updateRecord(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#getCountOfZeroPackageId(long,
	 *      long)
	 */
	public int getCountOfZeroPackageId(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "packageId", 0);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderCartItemDao#getCountOfUnZeroPackageId(long,
	 *      long)
	 */
	public int getCountOfUnZeroPackageId(long userId, long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		sql.append(" AND packageId>0");
		return getSqlSupport().queryCount(sql.toString());
	}
}