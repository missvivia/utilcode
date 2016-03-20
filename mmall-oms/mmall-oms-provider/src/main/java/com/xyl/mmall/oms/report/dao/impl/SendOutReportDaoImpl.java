package com.xyl.mmall.oms.report.dao.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.dao.SendOutReportDao;
import com.xyl.mmall.oms.report.meta.OmsSendOutCountryForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;
import com.xyl.mmall.oms.report.meta.OmsSendOutReport;

@Repository("SendOutReportDao")
public class SendOutReportDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsSendOutReport> implements
		SendOutReportDao {

	@Autowired
	private WarehouseDao warehouseDao;

	private String sql_send_out_by_express = "select b.warehouseId,a.CashOnDelivery,count(*) as total "
			+ "from Mmall_Oms_OmsOrderForm as a,Mmall_Oms_Warehouse as b "
			+ "where a.storeareaid=b.warehouseId and a.ShipTime >= ? and a.ShipTime <= ? "
			+ "group by b.warehouseId,a.CashOnDelivery";

	private String sql_send_out_by_warehouse = "select a.date,b.expressCompany,b.warehouseId,b.warehouseName,a.Cod,a.NonCode "
			+ "from Mmall_Oms_Report_SendOutReport as a,Mmall_Oms_Warehouse as b "
			+ "where a.WarehouseId=b.warehouseId and a.date=? order by warehouseName";

	private String sql_send_out_by_express_account = "select b.expressCompany,sum(a.Cod) as Cod,sum(a.NonCode) as NonCod "
			+ "from Mmall_Oms_Report_SendOutReport as a,Mmall_Oms_Warehouse as b "
			+ "where a.WarehouseId=b.warehouseId and a.date=? group by b.expressCompany";

	@Override
	public List<OmsSendOutProvinceForm> getSendOutProvinceForm(long day) {
		long currTime = new Date().getTime();
		int total = this.getSendOutCountByDay(day);
		DBResource resource = this.getSqlSupport().excuteQuery(sql_send_out_by_warehouse, day);
		List<OmsSendOutProvinceForm> list = new ArrayList<OmsSendOutProvinceForm>();
		ResultSet rs = resource.getResultSet();
		try {
			while (rs != null && rs.next()) {
				OmsSendOutProvinceForm omsSendOutProvinceForm = new OmsSendOutProvinceForm();
				long date = rs.getLong("date");
				String expressCompany = rs.getString("expressCompany");
				String warehouseName = rs.getString("warehouseName");
				long warehouseId = rs.getLong("warehouseId");
				int cod = rs.getInt("Cod");
				int nonCod = rs.getInt("NonCode");
				omsSendOutProvinceForm.setCodRate(new BigDecimal(1.0 * cod / (cod + nonCod)));
				omsSendOutProvinceForm.setWarehouseId(warehouseId);
				if (total == 0)
					omsSendOutProvinceForm.setWarehouseRate(BigDecimal.ZERO);
				else
					omsSendOutProvinceForm.setWarehouseRate(new BigDecimal(1.0 * (cod + nonCod) / total));
				omsSendOutProvinceForm.setCreateTime(currTime);
				omsSendOutProvinceForm.setUpdateTime(currTime);
				omsSendOutProvinceForm.setExpressName(expressCompany);
				omsSendOutProvinceForm.setNum(cod + nonCod);
				omsSendOutProvinceForm.setDate(date);
				omsSendOutProvinceForm.setWarehouse(warehouseName);
				list.add(omsSendOutProvinceForm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return list;
	}

	/**
	 * 组装全国发货报表数据
	 * 
	 * @param day
	 * @return
	 */
	@Override
	public List<OmsSendOutCountryForm> getSendOutCountryForm(long day) {
		long currTime = new Date().getTime();
		int total = this.getSendOutCountByDay(day);
		DBResource resource = this.getSqlSupport().excuteQuery(sql_send_out_by_express_account, day);
		List<OmsSendOutCountryForm> list = new ArrayList<OmsSendOutCountryForm>();
		ResultSet rs = resource.getResultSet();
		try {
			while (rs != null && rs.next()) {
				OmsSendOutCountryForm omsSendOutCountryForm = new OmsSendOutCountryForm();
				String expressCompany = rs.getString("expressCompany");
				int cod = rs.getInt("Cod");
				int nonCod = rs.getInt("NonCod");
				omsSendOutCountryForm.setCod(cod);
				omsSendOutCountryForm.setCreateTime(currTime);
				omsSendOutCountryForm.setUpdateTime(currTime);
				omsSendOutCountryForm.setDate(day);
				omsSendOutCountryForm.setExpressName(expressCompany);
				omsSendOutCountryForm.setNoncode(nonCod);
				omsSendOutCountryForm.setNum(cod+nonCod);
				// 发货量统计
				omsSendOutCountryForm.setTotal(getSendOutCountByDay(day));

				MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
				// 仓库占比
				if (total == 0)
					omsSendOutCountryForm.setRate(BigDecimal.ZERO);
				else
					omsSendOutCountryForm.setRate(new BigDecimal(1.00 * (cod + nonCod) / total, mc));
				if (cod + nonCod != 0) {
					// COD占比
					omsSendOutCountryForm.setCodRate(new BigDecimal(1.00 * cod / total, mc));
				}
				list.add(omsSendOutCountryForm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return list;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.report.dao.SendOutReportDao#getSentOutListByDay(long)
	 */
	@Override
	public List<OmsSendOutReport> getSentOutListByDay(long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "date", date);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.report.dao.SendOutReportDao#getSentOutListByDuration(long,
	 *      long)
	 */
	@Override
	public List<OmsSendOutReport> getSentOutListByDuration(long startTime, long endTime) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND date>=" + startTime).append(" AND date<=" + endTime);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.report.dao.SendOutReportDao#getSendOutCountByDay(long)
	 */
	@Override
	public int getSendOutCountByDay(long date) {
		List<OmsSendOutReport> list = getSentOutListByDay(date);
		int sum = 0;
		if (list != null && list.size() > 0) {
			for (OmsSendOutReport report : list) {
				sum += (report.getCod() + report.getNoncode());
			}
		}
		return sum;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.report.dao.SendOutReportDao#getSendOutByWarehouseId(long)
	 */
	@Override
	public int getSendOutByWarehouseId(long warehouseId) {

		return 0;
	}

	@Override
	public OmsSendOutReport save(OmsSendOutReport report) {
		return this.addObject(report);
	}

	@Override
	public List<OmsSendOutReport> getSendOutReport(long day) {
		long nextDay = day + 24 * 3600 * 1000;
		DBResource resource = getSqlSupport().excuteQuery(sql_send_out_by_express, day, nextDay);
		ResultSet rs = resource.getResultSet();
		List<OmsSendOutReport> result = new ArrayList<OmsSendOutReport>();
		long currTime = new Date().getTime();
		try {
			Map<Long, List<CODData>> map = new HashMap<Long, List<CODData>>();
			while (rs != null && rs.next()) {
				CODData codData = new CODData();
				codData.CashOnDelivery = rs.getInt("CashOnDelivery");
				codData.warehouseId = rs.getLong("warehouseId");
				codData.total = rs.getInt("total");
				if (map.get(codData.warehouseId) != null) {
					List<CODData> list = map.get(codData.warehouseId);
					list.add(codData);
					map.put(codData.warehouseId, list);
				} else {
					List<CODData> list = new ArrayList<CODData>();
					list.add(codData);
					map.put(codData.warehouseId, list);
				}
			}

			Iterator<Long> it = map.keySet().iterator();
			while (it.hasNext()) {
				Long warehouseId = it.next();
				List<CODData> codList = map.get(warehouseId);
				OmsSendOutReport omsSendOutReport = new OmsSendOutReport();
				omsSendOutReport.setDate(day);
				omsSendOutReport.setCreateTime(currTime);
				omsSendOutReport.setUpdateTime(currTime);
				omsSendOutReport.setWarehouseId(codList.get(0).warehouseId);
				WarehouseForm warehouseForm = warehouseDao.getWarehouseById(warehouseId);
				omsSendOutReport.setExpressName(warehouseForm.getExpressCompany());
				for (CODData data : codList) {
					if (data.CashOnDelivery == 0)
						omsSendOutReport.setNoncode(data.total);
					else
						omsSendOutReport.setCod(data.total);
				}
				result.add(omsSendOutReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return result;
	}

	@Override
	public OmsSendOutReport getSendOutReportByWarehouseIdAndDay(long warehouseId, long day) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "date", day);
		return queryObject(sql);
	}

}

class CODData {
	int CashOnDelivery;

	int total;

	long warehouseId;
}
