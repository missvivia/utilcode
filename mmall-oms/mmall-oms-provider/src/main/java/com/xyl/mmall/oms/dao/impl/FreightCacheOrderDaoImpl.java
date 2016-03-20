/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.FreightCacheOrderDao;
import com.xyl.mmall.oms.meta.FreightCacheOrder;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("freightCacheOrderDao")
public class FreightCacheOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<FreightCacheOrder> implements
		FreightCacheOrderDao {

	private String tableName = super.getTableName();

	private String getByMinCacheId = "select * from " + tableName + " where id > ? order by id";

	private String updateReason = "update ".concat(tableName).concat(" set reason=? where id = ?");

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightCacheOrderDao#getByMinCacheId()
	 */
	@Override
	public List<FreightCacheOrder> getByMinCacheId(long startMinCacheId, int limit) {
		StringBuilder sb = new StringBuilder(getByMinCacheId);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), startMinCacheId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightCacheOrderDao#updateReason(com.xyl.mmall.oms.meta.FreightCacheOrder)
	 */
	@Override
	public boolean updateReason(FreightCacheOrder cacheOrder) {
		return this.getSqlSupport().excuteUpdate(updateReason, cacheOrder.getReason(), cacheOrder.getId()) > 0;
	}

}
