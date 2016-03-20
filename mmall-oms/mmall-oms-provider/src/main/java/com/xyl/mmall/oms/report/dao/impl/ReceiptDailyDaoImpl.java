package com.xyl.mmall.oms.report.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.OmsReceiptDailyDao;
import com.xyl.mmall.oms.report.meta.OmsReceiptDaily;

@Repository("OmsReceiptDailyDao")
public class ReceiptDailyDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsReceiptDaily> implements
		OmsReceiptDailyDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 从Mmall_Oms_OmsOrderPackage,Mmall_Oms_OmsOrderForm,Mmall_Oms_Warehouse获取数据读入到Mmall_Oms_Report_ReceiptDaily
	 */
	private String sql_receipt_by_time = "select c.warehouseId,b.OmsOrderFormId,a.PackageStateUpdateTime,b.shipTime "
			+ " from Mmall_Oms_OmsOrderPackage as a,Mmall_Oms_OmsOrderForm as b,Mmall_Oms_Warehouse as c "
			+ " where a.OmsOrderFormId=b.OmsOrderFormId and b.storeareaid=c.warehouseId and b.shipTime>=? "
			+ " and b.shipTime<=? and a.OmsOrderPackageState=10 ";
	
	
	@Override
	public OmsReceiptDaily saveReceiptReport(OmsReceiptDaily omsReceiptDaily){
		return this.addObject(omsReceiptDaily);
	}
	
	public OmsReceiptDaily getOmsReceiptDailyByWarehouseIdAndDay(long warehouseId,long date){
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" And date=" + date);
		sql.append(" And warehouseId=" + warehouseId);
		return queryObject(sql);
	}
	
	/**
	 * 按发货日期获取Mmall_Oms_Report_ReceiptDaily表的数据
	 * @param date
	 * @return
	 */
	@Override
	public List<OmsReceiptDaily> getListByDate(long date){
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" And date=" + date);
		return queryObjects(sql);
	}
	

	
	@Override
	public List<OmsReceiptDaily> getReceiptReportDaily(long date,int start,int end,int type){
		long nextDay = date + 24*3600*1000;
		long startTime = start*3600;
		long endTime = end*3600;
		DBResource resource = getSqlSupport().excuteQuery(sql_receipt_by_time,date,nextDay);
		logger.info("SQL:"+sql_receipt_by_time + " argv[" + date + "," + nextDay + "]");
		ResultSet rs = resource.getResultSet();
		long currTime = new Date().getTime();
		List<OmsReceiptDaily> result = new ArrayList<OmsReceiptDaily>();
		try {
			Map<Long,List<ReceiptDaily>> map = new HashMap<Long,List<ReceiptDaily>>();
			while(rs!=null && rs.next()){
				ReceiptDaily receiptDaily = new ReceiptDaily();
				receiptDaily.warehouseId= rs.getLong("warehouseId");
				receiptDaily.OmsOrderFormId = rs.getLong("OmsOrderFormId");
				receiptDaily.packageStateUpdateTime = rs.getLong("PackageStateUpdateTime");
				receiptDaily.shipTime = rs.getLong("shipTime");
				//判断时间
				if(receiptDaily.packageStateUpdateTime/1000-receiptDaily.shipTime/1000>=startTime && receiptDaily.packageStateUpdateTime/1000-receiptDaily.shipTime/1000<=endTime){
					if(map.get(receiptDaily.warehouseId)!=null){
						List<ReceiptDaily> list = map.get(receiptDaily.warehouseId);
						list.add(receiptDaily);
						map.put(receiptDaily.warehouseId, list);
					}else{
						List<ReceiptDaily> list = new ArrayList<ReceiptDaily>();
						list.add(receiptDaily);
						map.put(receiptDaily.warehouseId, list);
					}
				}
			}
			Iterator<Long> it = map.keySet().iterator();
			while(it.hasNext()){
				OmsReceiptDaily omsReceiptDaily = new OmsReceiptDaily();
				Long warehouseId = it.next();
				List<ReceiptDaily> list = map.get(warehouseId);
				omsReceiptDaily.setWarehouseId(warehouseId);
				omsReceiptDaily.setDate(date);
				omsReceiptDaily.setType(type);;
				omsReceiptDaily.setCreateTime(currTime);
				omsReceiptDaily.setUpdateTime(currTime);
				omsReceiptDaily.setTotal(list.size());
				result.add(omsReceiptDaily);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public OmsReceiptDaily getReceiptDailyByWarehouseAnd(long warehouseId, long date, int type) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "WarehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "Date", date);
		SqlGenUtil.appendExtParamObject(sql, "Type", type);
		return queryObject(sql);
	}

}
