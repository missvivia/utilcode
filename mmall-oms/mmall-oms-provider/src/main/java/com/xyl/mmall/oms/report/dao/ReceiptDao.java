package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;

public interface ReceiptDao extends AbstractDao<OmsReceiptReport> {
	
	/**
	 * 根据日期获取签收数据 
	 * @param date
	 * @return
	 */
	public List<OmsReceiptReport> getReceiptReportByDate(long date);
	
	
	/**
	 * 根据开始日期和结束日期获取签收数据
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<OmsReceiptReport> getReceiptReportByDate(long warehouseId,long startDate,long endDate,List<Long> warehouseIds,DDBParam ddbParam);


	/**
	 * 根据仓库日期获取报表数据
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	public OmsReceiptReport getOmsReceiptDailyByWarehouseIdAndDay(long warehouseId,long date);
	
	
}
