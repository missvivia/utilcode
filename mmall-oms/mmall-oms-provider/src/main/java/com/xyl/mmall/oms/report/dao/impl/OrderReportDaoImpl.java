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
import com.xyl.mmall.oms.report.dao.OrderReportDao;
import com.xyl.mmall.oms.report.meta.OrderReport;
import com.xyl.mmall.oms.util.ReportTimeUtil;

@Repository
public class OrderReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderReport>
	implements OrderReportDao {

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.OrderReportDao#getOrderReportList(long, long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<OrderReport> getOrderReportList(long beginTime, long endTime, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND time >= ").append(beginTime).append(" AND time < ").append(endTime);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.OrderReportDao#syncData(java.util.List)
	 */
	// 最好是半夜零点之后统计 时间轴错位一天 计算昨天的数据       <--- 这个时间定义是2014年12月份定义的计算时间
	// 现在改成了新的计算时间 改成了每天下午四点半计算报表数据    <--- 这个时间定是2015年重新改的 所以时间轴要偏移一天
	@Override
	public boolean syncData(List<String> expressList) {
//		long zero = ReportTimeUtil.getLastDayTimeZeroMillis();				// 昨天早上零点
//		long end = zero + ReportTimeUtil.HOUR16;							// 昨天下午四点
//		long begin = end - ReportTimeUtil.ONEDAY;							// 前天下午四点
		
		long zero = ReportTimeUtil.getDayTimeZeroMillis();					// 今天早上零点
		long end = zero + ReportTimeUtil.HOUR16;							// 今天下午四点
		long begin = end - ReportTimeUtil.ONEDAY;							// 昨天下午四点
		Map<String, Integer> collectiveOrderNumberMap = new HashMap<>();
		Map<String, Integer> cancelOrderNumberMap = new HashMap<>();
		
		// 计算已经汇总的订单数目
		// append(" AND omsOrderFormState <>").append(OmsOrderFormState.CANCEL.getIntValue()).
		StringBuilder sql = new StringBuilder(256);
		sql.append("select B.expressCompany, count(A.omsOrderFormId) as count from Mmall_Oms_OmsOrderForm A, Mmall_Oms_Warehouse B").
		append(" where A.storeAreaId = B.warehouseId AND A.createTime > ").append(begin).append(" AND A.createTime <= ").append(end).
		append(" AND omsOrderFormState <> ").append(OmsOrderFormState.UNPICK_CANCEL.getIntValue()).append(" group by B.expressCompany");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				String expressCompany = rs.getString("expressCompany");
				int count = rs.getInt("count");
				collectiveOrderNumberMap.put(expressCompany, count);
			}
		} catch (SQLException e) {
			return false;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		
		// 计算取消的订单数目
		sql.setLength(0);
		sql.append("select B.expressCompany, count(A.omsOrderFormId) as count from Mmall_Oms_OmsOrderForm A, Mmall_Oms_Warehouse B").
		append(" where A.storeAreaId = B.warehouseId AND A.createTime > ").append(begin).append(" AND A.createTime <= ").append(end).
		append(" AND omsOrderFormState =").append(OmsOrderFormState.UNPICK_CANCEL.getIntValue()).append(" group by B.expressCompany");
		dbr = this.getSqlSupport().excuteQuery(sql.toString());
		rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				String expressCompany = rs.getString("expressCompany");
				int count = rs.getInt("count");
				cancelOrderNumberMap.put(expressCompany, count);
			}
		} catch (SQLException e) {
			return false;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		
		List<OrderReport> addList = new ArrayList<>();
		for (String expressCompany : expressList) {
			int collectiveOrderNumber = 0;
			int cancelOrderNumber = 0;
			Integer number = collectiveOrderNumberMap.get(expressCompany);
			if (number != null) {
				collectiveOrderNumber = number;
			}
			number = cancelOrderNumberMap.get(expressCompany);
			if (number != null) {
				cancelOrderNumber = number;
			}
			int totalOrderNumber = collectiveOrderNumber + cancelOrderNumber;
			if (totalOrderNumber > 0) {
				OrderReport orderReport = new OrderReport();
				orderReport.setId(-1);;
				orderReport.setExpressCompany(expressCompany);
				orderReport.setCollectiveOrderNumber(collectiveOrderNumber);
				orderReport.setCancelOrderNumber(cancelOrderNumber);
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
