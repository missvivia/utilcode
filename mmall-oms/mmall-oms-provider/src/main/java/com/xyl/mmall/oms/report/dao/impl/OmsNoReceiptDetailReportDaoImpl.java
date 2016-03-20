package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.OmsNoReceiptDetailReportDao;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;

@Repository("OmsNoReceiptDetailReportDao")
public class OmsNoReceiptDetailReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsNoReceiptDetailReport> implements OmsNoReceiptDetailReportDao {

	@Override
	public List<OmsNoReceiptDetailReport> getNoReceiptDetailReport(
			long warehouseId, long startDay, long endDay, List<Long> warehouseIds,DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if(warehouseId>0)
			SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		else
		{
			if(warehouseIds!=null && warehouseIds.size()>0)
			{
				StringBuilder sb = new StringBuilder();
				for(Long wid:warehouseIds){
					sb.append(wid).append(",");
				}
				sql.append(" AND warehouseId in (" + sb.toString().substring(0, sb.toString().length()-1) + ")");
			}	
		}
		sql.append(" and date>="+startDay);
		sql.append(" and date<="+endDay);
		SqlGenUtil.appendDDBParam(sql, ddbParam, "");
		return this.queryObjects(sql);
	}

	@Override
	public OmsNoReceiptDetailReport getNoReceiptDetailReportByPackageIdAndDay(long day, long packageId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "packageId", packageId);
		SqlGenUtil.appendExtParamObject(sql, "date", day);
		return this.queryObject(sql);
	}

	
}
