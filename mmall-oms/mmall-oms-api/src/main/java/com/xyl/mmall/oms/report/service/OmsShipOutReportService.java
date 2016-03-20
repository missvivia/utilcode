package com.xyl.mmall.oms.report.service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;

public interface OmsShipOutReportService {
	public OmsShipOutReport getOmsShipOutReport(long dateTime, long supplierId);

	public void addOmsShipOutReport(OmsShipOutReport rp);
	
	public RetArg getOmsShipOutReport(long startDay, long endDay, DDBParam ddbParam);

	public RetArg getOmsShipOutReport(long startDay, long endDay, long warehouseId, DDBParam ddbParam);
}
