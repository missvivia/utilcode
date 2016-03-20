package com.xyl.mmall.oms.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.oms.report.dao.OmsShipOutReportDao;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;
import com.xyl.mmall.oms.report.service.OmsShipOutReportService;

/**
 * @author zb
 *
 */
@Service("omsShipOutReportService")
public class OmsShipOutReportServiceImpl implements OmsShipOutReportService {

	@Autowired
	private OmsShipOutReportDao omsShipOutReportDao;

	@Override
	public OmsShipOutReport getOmsShipOutReport(long dateTime, long supplierId) {
		return omsShipOutReportDao.getOmsShipOutReport(dateTime, supplierId);
	}

	@Override
	public void addOmsShipOutReport(OmsShipOutReport rp) {
		omsShipOutReportDao.addObject(rp);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.service.OmsShipOutReportService#getOmsShipOutReport(long, long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public RetArg getOmsShipOutReport(long startDay, long endDay, long warehouseId, DDBParam param) {
		List<OmsShipOutReport> list = omsShipOutReportDao.getOmsShipOutReport(startDay, endDay, warehouseId, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, list);
		return retArg;
	}

	@Override
	public RetArg getOmsShipOutReport(long startDay, long endDay, DDBParam param) {
		List<OmsShipOutReport> list = omsShipOutReportDao.getOmsShipOutReport(startDay, endDay, param);
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, param);
		RetArgUtil.put(retArg, list);
		return retArg;
	}


}
