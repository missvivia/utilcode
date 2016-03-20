package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;

public interface OmsNoReceiptReportDao extends AbstractDao<OmsNoReceiptReport> {
	
	/**
	 * 根据仓库，开始结束时间获取数据
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsNoReceiptReport> getNoReceiptReport(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);
	
	/**
	 * 根据仓库id和日期获取数据
	 * @param warehouseId
	 * @param day
	 * @return
	 */
	public OmsNoReceiptReport getNoReceiptReportByWarehouseIdAndDay(long warehouseId,long day);

	
}
