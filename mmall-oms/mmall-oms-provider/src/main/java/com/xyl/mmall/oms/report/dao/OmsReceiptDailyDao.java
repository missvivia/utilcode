package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.report.meta.OmsReceiptDaily;

public interface OmsReceiptDailyDao extends AbstractDao<OmsReceiptDaily>{

	/**
	 * 保存数据
	 * @param omsReceiptDaily
	 * @return
	 */
	public OmsReceiptDaily saveReceiptReport(OmsReceiptDaily omsReceiptDaily);

	/**
	 * 按日期获取仓库的签收情况
	 * @param date
	 * @param start
	 * @param end
	 * @param range
	 * @return
	 */
	public List<OmsReceiptDaily> getReceiptReportDaily(long date, int start, int end, int range);

	/**
	 * 按发货日期获取Mmall_Oms_Report_ReceiptDaily表的数据
	 * @param date
	 * @return
	 */
	public List<OmsReceiptDaily> getListByDate(long date);
	
	/**
	 * 根据发货日期仓库和时间范围搜索用户
	 * @param warehouseId
	 * @param date
	 * @param range
	 * @return
	 */
	public OmsReceiptDaily getReceiptDailyByWarehouseAnd(long warehouseId,long date,int range);
	
	
	
}
