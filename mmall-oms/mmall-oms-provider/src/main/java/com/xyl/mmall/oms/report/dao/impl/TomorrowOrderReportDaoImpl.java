package com.xyl.mmall.oms.report.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.report.dao.TomorrowOrderReportDao;
import com.xyl.mmall.oms.report.meta.TomorrowOrderReport;
import com.xyl.mmall.oms.util.ReportTimeUtil;

@Repository
public class TomorrowOrderReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<TomorrowOrderReport>
	implements TomorrowOrderReportDao {
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.TomorrowOrderReportDao#getOrderReportList(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<TomorrowOrderReport> getOrderReportList(long beginTime, long endTime, DDBParam param,
			List<Long> warehouseList) {
		if (warehouseList == null || (warehouseList != null && warehouseList.size() == 0) ) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND time >= ").append(beginTime).append(" AND time < ").append(endTime);
		StringBuilder dataSet = new StringBuilder(256);
		dataSet.append("(");
		for (Long id : warehouseList) {
			dataSet.append(id).append(",");
		}
		dataSet.deleteCharAt(dataSet.lastIndexOf(","));
		dataSet.append(")");
		sql.append(" AND warehouseId in ").append(dataSet);
//		if (warehouse != null && !StringUtils.isBlank(warehouse)) {
//			warehouse = warehouse.trim();
//			sql.append(" AND warehouse = '").append(warehouse).append("'");
//		}
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.OrderReportDao#syncDataTomorrow(java.util.List)
	 */
	@Override
	public boolean syncDataTomorrow(List<Long> warehouseList) {
//		long zero = ReportTimeUtil.getLastDayTimeZeroMillis();	// 昨天早上零点
//		long end = zero + ReportTimeUtil.HOUR16;				// 昨天下午四点
//		long begin = end - ReportTimeUtil.ONEDAY;				// 前天下午四点
		
		long zero = ReportTimeUtil.getDayTimeZeroMillis();					// 今天早上零点
		long end = zero + ReportTimeUtil.HOUR16;							// 今天下午四点
		long begin = end - ReportTimeUtil.ONEDAY;							// 昨天下午四点
		
		Map<Long, ExpressDataWrapper> collectiveOrderNumberMap = new HashMap<>();
		Map<Long, ExpressDataWrapper> cancelOrderNumberMap = new HashMap<>();
		
		// 计算已经汇总的订单数目
		StringBuilder sql = new StringBuilder(256);
		sql.append("select A.storeAreaId, B.expressCompany, B.warehouseName, count(A.omsOrderFormId) as count ").
		append("from Mmall_Oms_OmsOrderForm A, Mmall_Oms_Warehouse B where A.storeAreaId = B.warehouseId AND A.createTime > ").
		append(begin).append(" AND A.createTime <= ").append(end).append(" AND omsOrderFormState <> ").
		append(OmsOrderFormState.UNPICK_CANCEL.getIntValue()).append(" group by A.storeAreaId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				ExpressDataWrapper wrapper = new ExpressDataWrapper();
				long storeAreaId = rs.getLong("storeAreaId");
				String expressCompany = rs.getString("expressCompany");
				String warehouseName = rs.getString("warehouseName");
				int count = rs.getInt("count");
				wrapper.setCount(count);
				wrapper.setExpressCompany(expressCompany);
				wrapper.setWarehouseName(warehouseName);
				collectiveOrderNumberMap.put(storeAreaId, wrapper);
			}
		} catch (SQLException e) {
			return false;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		
		// 计算取消的订单数目
		sql.setLength(0);
		sql.append("select A.storeAreaId, B.expressCompany, B.warehouseName, count(A.omsOrderFormId) as count ").
		append("from Mmall_Oms_OmsOrderForm A, Mmall_Oms_Warehouse B where A.storeAreaId = B.warehouseId AND A.createTime > ").
		append(begin).append(" AND A.createTime <= ").append(end).append(" AND omsOrderFormState = ").
		append(OmsOrderFormState.UNPICK_CANCEL.getIntValue()).append(" group by B.expressCompany");
		dbr = this.getSqlSupport().excuteQuery(sql.toString());
		rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				ExpressDataWrapper wrapper = new ExpressDataWrapper();
				long storeAreaId = rs.getLong("storeAreaId");
				String expressCompany = rs.getString("expressCompany");
				String warehouseName = rs.getString("warehouseName");
				int count = rs.getInt("count");
				wrapper.setCount(count);
				wrapper.setExpressCompany(expressCompany);
				wrapper.setWarehouseName(warehouseName);
				cancelOrderNumberMap.put(storeAreaId, wrapper);
			}
		} catch (SQLException e) {
			return false;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		
		List<TomorrowOrderReport> addList = new ArrayList<>();
		for (Long warehouseId : warehouseList) {
			int collectiveOrderNumber = 0;
			int cancelOrderNumber = 0;
			ExpressDataWrapper wrapper = collectiveOrderNumberMap.get(warehouseId);
			String expressCompany = "";
			String warehouseName = "";
			if (wrapper != null) {
				collectiveOrderNumber = wrapper.getCount();
				expressCompany = wrapper.getExpressCompany();
				warehouseName = wrapper.getWarehouseName();
			}
			wrapper = cancelOrderNumberMap.get(warehouseId);
			if (wrapper != null) {
				cancelOrderNumber = wrapper.getCount();
				expressCompany = wrapper.getExpressCompany();
				warehouseName = wrapper.getWarehouseName();
			}
			int totalOrderNumber = collectiveOrderNumber + cancelOrderNumber;
			if (totalOrderNumber > 0) {
				TomorrowOrderReport orderReport = new TomorrowOrderReport();
				orderReport.setId(-1);
				orderReport.setWarehouseId(warehouseId);
				orderReport.setExpressCompany(expressCompany);
				orderReport.setWarehouse(warehouseName);
				orderReport.setCancelOrderNumber(cancelOrderNumber);
				orderReport.setCollectiveOrderNumber(collectiveOrderNumber);
				orderReport.setTotalOrderNumber(totalOrderNumber);
				orderReport.setTime(end);
				addList.add(orderReport);
			}
		}
		if (addList.size() > 0) {
			return addObjects(addList);
		}
		return true;
	}


}

class ExpressDataWrapper {
	
	private String expressCompany;
	
	private String warehouseName;
	
	private int count;

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
