package com.xyl.mmall.order.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.IncrField;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.SkuOrderStock;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class SkuOrderStockDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SkuOrderStock> implements SkuOrderStockDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.SkuOrderStockDao#updateSaleCount(com.xyl.mmall.order.meta.SkuOrderStock,
	 *      java.util.List)
	 */
	public boolean updateSaleCount(SkuOrderStock obj, Collection<IncrField<Integer>> ifColl) {
		if (obj != null)
			obj.setUpTime(System.currentTimeMillis());
		return PrintDaoUtil.updateObjectByKey(obj, Arrays.asList(new String[] { "upTime" }), null, ifColl, null, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.SkuOrderStockDao#getListBySkuIds(java.util.Collection)
	 */
	public List<SkuOrderStock> getListBySkuIds(Collection<Long> skuIdColl) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "skuId", skuIdColl);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.SkuOrderStockDao#deleteObjectByKeys(java.util.Collection)
	 */
	public boolean deleteObjectByKeys(Collection<? extends SkuOrderStock> objColl) {
		if (CollectionUtil.isEmptyOfCollection(objColl))
			return false;
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql()).append(" ( 1=0 ");
		for (SkuOrderStock obj : objColl) {
			sql.append(" OR skuId=").append(obj.getSkuId());
		}
		sql.append(" ) ");
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
}
