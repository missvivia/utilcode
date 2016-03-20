package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;

public interface OmsNoReceiptDetailReportDao extends AbstractDao<OmsNoReceiptDetailReport> {
	/**
	 * 根据仓库开始结束时间获取数据
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	public List<OmsNoReceiptDetailReport> getNoReceiptDetailReport(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam);

	/**
	 * 根据包裹id和日查询数据
	 * @param day
	 * @param packageId
	 * @return
	 */
	public OmsNoReceiptDetailReport getNoReceiptDetailReportByPackageIdAndDay(long day, long packageId);

}
