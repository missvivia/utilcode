package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.ReceiptDao;
import com.xyl.mmall.oms.report.meta.OmsReceiptDaily;
import com.xyl.mmall.oms.report.meta.OmsReceiptReport;

@Repository("ReceiptDao")
public class ReceiptDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsReceiptReport> implements ReceiptDao {

	

	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.ReceiptDao#getReceiptReportByDate(long)
	 */
	@Override
	public List<OmsReceiptReport> getReceiptReportByDate(long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" And date=" + date);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.ReceiptDao#getReceiptReportByDate(long, long)
	 */
	@Override
	public List<OmsReceiptReport> getReceiptReportByDate(long warehouseId,long startDate, long endDate,List<Long> warehouseIds,DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" And date>=" + startDate);
		sql.append(" And date<=" + endDate);
		if(warehouseId>0)
			sql.append(" And warehouseId=" + warehouseId);
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
		SqlGenUtil.appendDDBParam(sql, ddbParam, "");
		return queryObjects(sql);
	}
	/**
	 * 根据仓库日期获取报表数据
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	@Override
	public OmsReceiptReport getOmsReceiptDailyByWarehouseIdAndDay(long warehouseId,long date){
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" And date=" + date);
		sql.append(" And warehouseId=" + warehouseId);
		return queryObject(sql);
	}

}

class ReceiptDaily{
	long warehouseId;
	int OmsOrderPackageState;
	long OmsOrderFormId;
	int total;
	long packageStateUpdateTime;
	long shipTime;
}