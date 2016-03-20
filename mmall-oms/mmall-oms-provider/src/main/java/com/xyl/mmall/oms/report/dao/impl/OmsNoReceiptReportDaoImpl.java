package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.OmsNoReceiptReportDao;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;

@Repository("OmsNoReceiptReportDao")
public class OmsNoReceiptReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsNoReceiptReport> implements OmsNoReceiptReportDao {

	@Override
	public List<OmsNoReceiptReport> getNoReceiptReport(long warehouseId,
			long startDay, long endDay, List<Long> warehouseIds, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if(warehouseId>0)
			SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		else{
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
	public OmsNoReceiptReport getNoReceiptReportByWarehouseIdAndDay(long warehouseId, long day) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "date", day);
		return this.queryObject(sql);
	}

	
}
