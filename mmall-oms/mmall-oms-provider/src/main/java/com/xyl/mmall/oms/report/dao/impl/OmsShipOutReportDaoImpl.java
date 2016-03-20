package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.OmsShipOutReportDao;
import com.xyl.mmall.oms.report.meta.OmsShipOutReport;

@Repository("OmsShipOutReportDao")
public class OmsShipOutReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsShipOutReport> implements
		OmsShipOutReportDao {

	private String select = "select * from Mmall_Oms_Report_ShipOutReport where dateTime=? and supplierId=?";

	@Override
	public OmsShipOutReport getOmsShipOutReport(long dateTime, long supplierId) {
		return this.queryObject(select, dateTime, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.report.dao.OmsShipOutReportDao#getOmsShipOutReport(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OmsShipOutReport> getOmsShipOutReport(long startDay, long endDay, long warehouseId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		sql.append(" and dateTime>=" + startDay);
		sql.append(" and dateTime<=" + endDay);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	@Override
	public List<OmsShipOutReport> getOmsShipOutReport(long startDay, long endDay, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" and dateTime>=" + startDay);
		sql.append(" and dateTime<=" + endDay);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

}
