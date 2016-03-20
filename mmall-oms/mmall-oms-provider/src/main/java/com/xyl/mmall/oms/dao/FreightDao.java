/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.meta.Freight;

/**
 * @author hzzengchengyuan
 *
 */
public interface FreightDao extends AbstractDao<Freight> {
	/**
	 * 根据快递公司和运单号获取运费
	 * 
	 * @param expressCompany
	 * @param expressNo
	 * @return
	 */
	Freight getByExpressInfo(String expressCompany, String expressNo);

	/**
	 * @param newState
	 * @param freightId
	 * @param oldState
	 * @return
	 */
	boolean updatePackageStateAndCodCharge(OmsOrderPackageState newState, double codCharge, long freightId,
			OmsOrderPackageState oldState);
	
	/**
	 * 根据快递公司、仓库站点和发货时间范围查询正向运费
	 * @param expressCompany
	 * @param warehouseId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Freight> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime);
}
