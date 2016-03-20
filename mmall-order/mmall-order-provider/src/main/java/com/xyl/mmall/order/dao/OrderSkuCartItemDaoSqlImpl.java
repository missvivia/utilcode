package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderSkuCartItem;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class OrderSkuCartItemDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderSkuCartItem> implements
		OrderSkuCartItemDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuCartItemDao#getListByCartId(long,
	 *      long)
	 */
	public OrderSkuCartItem getObjectByCartId(long userId, long cartId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderCartItemId", cartId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuCartItemDao#getListByOrderCartItemIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<OrderSkuCartItem> getListByOrderCartItemIdsAndUserId(long userId, Collection<Long> orderCartItemIdColl) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderCartItemId", orderCartItemIdColl);
		return queryObjects(sql);
	}
}
