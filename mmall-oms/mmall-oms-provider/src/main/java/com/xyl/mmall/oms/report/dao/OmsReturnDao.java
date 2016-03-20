package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;

public interface OmsReturnDao extends AbstractDao<OmsReturnReport> {

	/**
	 * 返货数据
	 * @param date
	 * @return
	 */
	public List<OmsReturnReport> getReturnList(long date);

	/**
	 * 根据仓库和时间查询返货数据
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsReturnReport> getReturnListByWarehouseAndDay(long warehouseId,
			long startDay, long endDay,List<Long> warehouseIds, DDBParam ddbParam);
	
	/**
	 * 根据仓库和日期获取数据
	 * @param warehouseId
	 * @param day
	 * @return
	 */
	public OmsReturnReport getReturnListByWarehouseAndDay(long warehouseId,long date);
	
	

}
