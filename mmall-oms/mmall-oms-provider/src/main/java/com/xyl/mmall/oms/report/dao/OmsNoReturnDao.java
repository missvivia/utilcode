package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;

public interface OmsNoReturnDao extends AbstractDao<OmsNoReturnReport> {

	/**
	 * 未返货数据
	 * @param date
	 * @return
	 */
	public List<OmsNoReturnReport> getNoReturnList(long date);

	/**
	 * 根据仓库和时间查询未返货数据
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsNoReturnReport> getNoReturnListByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam);

}
