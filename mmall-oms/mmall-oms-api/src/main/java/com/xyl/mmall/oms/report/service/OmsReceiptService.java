package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;

public interface OmsReceiptService {
	/**
	 * 输入数据
	 * @param obj
	 * @return
	 */
	public OmsReceiptReport addObject(OmsReceiptReport obj);

	/**
	 * 根据日期获取的签收情况
	 * @param date
	 * @return
	 */
	public List<OmsReceiptReport> getOmsRecepitReportByDuration(long startTime,long endTime);
	
	/**
	 * 定时任务存储每日数据
	 * @param date
	 * @return
	 */
	public boolean saveDataByDay(long date);

	/**
	 * 存储report数据
	 * @param date
	 * @return
	 */
	public boolean saveReportDataByDay(long date);
	
	/**
	 * 构建OmsReceiptReport表的数据
	 * @param day
	 * @return
	 */
	public List<OmsReceiptReport> getReceiptReportByDay(long day);
	
	/**
	 * 存储数据到Mmall_Oms_Report_ReceiptForm表中
	 * @return
	 */
	public boolean saveReportData(List<OmsReceiptReport> list);
	
	/**
	 * 根据仓库和日期查询签收报表数据
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @param param
	 * @return
	 */
	public List<OmsReceiptReport> queryReceiptReportByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam param);

}
