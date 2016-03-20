/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.FreightReverse;

/**
 * @author hzzengchengyuan
 *
 */
public interface FreightReverseDao extends AbstractDao<FreightReverse> {

	/**
	 * 根据快递公司和运单号获取运费
	 * 
	 * @param expressCompany
	 * @param expressNo
	 * @return
	 */
	FreightReverse getByExpressInfo(String expressCompany, String expressNo);
	
	/**
	 * 根据快递公司、仓库站点和发货时间范围查询正向运费
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FreightReverse> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime);
}
