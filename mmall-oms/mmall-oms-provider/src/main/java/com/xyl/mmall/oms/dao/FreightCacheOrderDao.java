/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.FreightCacheOrder;

/**
 * @author hzzengchengyuan
 *
 */
public interface FreightCacheOrderDao extends AbstractDao<FreightCacheOrder> {
	List<FreightCacheOrder> getByMinCacheId(long startMinCacheId, int limit);
	
	boolean updateReason(FreightCacheOrder cacheOrder);
}
