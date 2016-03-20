package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;

public interface OmsNoReceiptService {
	
	/**
	 * 存储7天前发货的未签收数据
	 * @param date
	 * @return
	 */
	public boolean saveData(long date);

	/**
	 * 存储7天前各省未签收订单归类统计数据
	 * @param date
	 * @return
	 */
	public boolean saveNoReceiptReport(long date);

	/**
	 * 存储7天钱未签收明细表
	 * @param date
	 * @return
	 */
	public boolean saveNoReceiptDetailReport(long date);
	
	/**
	 * 查询各省未签收订单明细归类统计表
	 * @return
	 */
	public List<OmsNoReceiptReport> queryNoReceiptReport(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);
	
	/**
	 * 查询未签收明细表
	 * @return
	 */
	public List<OmsNoReceiptDetailReport> queryNoReceiptDetailReport(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);
}
