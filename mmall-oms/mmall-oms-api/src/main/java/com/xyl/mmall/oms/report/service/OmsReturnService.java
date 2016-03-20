package com.xyl.mmall.oms.report.service;

import java.util.List;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;

public interface OmsReturnService {
	/**
	 * 存储返货数据
	 * @param date
	 * @return
	 */
	public boolean saveReturnData(long date);
	
	
	/**
	 * 存储未返货数据
	 * @param date
	 * @return
	 */
	public boolean saveNoReturnData(List<OmsNoReturnReport> list);
	
	
	/**
	 * 根据仓库和时间查询返货数据
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsReturnReport> queryReturnReportByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);
	
	/**
	 * 根据仓库和时间查询未返货数据
	 * @param warehouse
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<OmsNoReturnReport> queryNoReturnReportByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);


	/**
	 * 构建未返回单量明细
	 */
	public List<OmsNoReturnReport> getNoReturnDataByDate(long date);
}
