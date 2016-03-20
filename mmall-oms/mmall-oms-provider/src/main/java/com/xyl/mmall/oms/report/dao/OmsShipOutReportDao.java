package com.xyl.mmall.oms.report.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;

public interface OmsShipOutReportDao extends AbstractDao<OmsShipOutReport> {
	public OmsShipOutReport getOmsShipOutReport(long dateTime, long supplierId);

	public List<OmsShipOutReport> getOmsShipOutReport(long startDay, long endDay, DDBParam ddbParam) ;
	public List<OmsShipOutReport> getOmsShipOutReport(long startDay, long endDay, long warehouseId, DDBParam ddbParam);
}
