package com.xyl.mmall.oms.report.dao.impl;


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
import com.xyl.mmall.oms.report.dao.OmsNoReceiptDetailDao;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetail;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptDetailReport;
import com.xyl.mmall.oms.report.meta.OmsNoReceiptReport;

@Repository("OmsNoReceiptDetailDao")
public class OmsNoReceiptDetailDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsNoReceiptDetail> implements
		OmsNoReceiptDetailDao {
	
	private String sqlNoReceiptDetail = "select c.warehouseId,a.OmsOrderPackageState,a.MailNO,a.packageId,a.PackageStateUpdateTime,b.ConsigneeTel,b.Address,b.ConsigneeName,a.OmsOrderFormId "
			+ "from Mmall_Oms_OmsOrderPackage as a,Mmall_Oms_OmsOrderForm as  b,Mmall_Oms_Warehouse as c "
			+ "where a.OmsOrderFormId=b.OmsOrderFormId and b.storeareaid=c.warehouseId and (a.OmsOrderPackageState=0 or a.OmsOrderPackageState=20 or a.OmsOrderPackageState=30) and b.ShipTime>=? and b.ShipTime<=? ";

	private String sqlNoReceiptReport = "select WarehouseId,OmsOrderPackageState,count(*) as total from Mmall_Oms_Report_NoReceiptDetail where Day=? group by WarehouseId,OmsOrderPackageState";
	
	private String sqlNoReceiptDetailByDate = "select * from Mmall_Oms_Report_NoReceiptDetail where Day=?";
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Override
	public List<OmsNoReceiptDetail> getNoReceiptDetailBefore(long day){
		long nextDay = day + 24*3600*1000;
		DBResource resource = getSqlSupport().excuteQuery(sqlNoReceiptDetail,day,nextDay);
		List<OmsNoReceiptDetail> result = new ArrayList<OmsNoReceiptDetail>();
		ResultSet rs = resource.getResultSet();
		try {
			while(rs!=null && rs.next()){
				OmsNoReceiptDetail omsNoReceiptDetail = new OmsNoReceiptDetail();
				omsNoReceiptDetail.setWarehouseId(rs.getLong("warehouseId"));
				omsNoReceiptDetail.setMailNo(rs.getString("MailNO"));
				omsNoReceiptDetail.setPackageStateUpdateTime(rs.getLong("PackageStateUpdateTime"));
				omsNoReceiptDetail.setConsigneeTel(rs.getString("ConsigneeTel"));
				omsNoReceiptDetail.setAddress(rs.getString("Address"));
				omsNoReceiptDetail.setConsigneeName(rs.getString("ConsigneeName"));
				omsNoReceiptDetail.setOmsOrderFormId(rs.getLong("OmsOrderFormId"));
				omsNoReceiptDetail.setOmsOrderPackageState(rs.getInt("OmsOrderPackageState"));
				omsNoReceiptDetail.setPackageId(rs.getLong("packageId"));
				omsNoReceiptDetail.setDay(day);
				result.add(omsNoReceiptDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<OmsNoReceiptReport> getNoReceiptReportByDay(long day) {
		long currTime = new Date().getTime();
		DBResource resource = getSqlSupport().excuteQuery(sqlNoReceiptReport,day);
		ResultSet rs = resource.getResultSet();
		List<OmsNoReceiptReport> result = new ArrayList<OmsNoReceiptReport>();
		try {
			Map<Long,ArrayList<NoReturn>> map = new HashMap<Long,ArrayList<NoReturn>>();
			while(rs!=null && rs.next()){
				long warehouseId = rs.getLong("WarehouseId");
				int omsOrderPackageState = rs.getInt("OmsOrderPackageState");
				int total = rs.getInt("total");
				NoReturn noReturn = new NoReturn();
				noReturn.warehouseId = warehouseId;
				noReturn.omsOrderPackageState = omsOrderPackageState;
				noReturn.total = total;
				if(map.get(warehouseId)!=null){
					ArrayList<NoReturn> list = map.get(warehouseId);
					list.add(noReturn	);
					map.put(warehouseId, list);
				}else{
					ArrayList<NoReturn> list = new ArrayList<NoReturn>();
					list.add(noReturn	);
					map.put(warehouseId, list);
				}
			}
			Iterator<Long> it = map.keySet().iterator();
			while(it.hasNext()){
				long warehouseId = it.next();
				OmsNoReceiptReport omsNoReceiptReport = new OmsNoReceiptReport();
				WarehouseForm warehouseForm = warehouseDao.getWarehouseById(warehouseId);
				//设置仓库
				if(warehouseForm!=null){
					omsNoReceiptReport.setWarehouseName(warehouseForm.getWarehouseName());
					omsNoReceiptReport.setExpressCompany(warehouseForm.getExpressCompany());
				}
				ArrayList<NoReturn> list = map.get(warehouseId);
				int total = 0;
				for(NoReturn noReturn :list){
					total += noReturn.total;
					//设置拒收
					if(noReturn.omsOrderPackageState==20){
						omsNoReceiptReport.setReject(noReturn.total);
					}
				}
				//设置未签收
				omsNoReceiptReport.setCount(total);
				//设置其他
				omsNoReceiptReport.setOther(total-omsNoReceiptReport.getReject());
				omsNoReceiptReport.setCreateTime(currTime);
				omsNoReceiptReport.setModifyTime(currTime);
				omsNoReceiptReport.setWarehouseId(warehouseId);
				omsNoReceiptReport.setDate(day);
				result.add(omsNoReceiptReport);
			}

				
		} catch (SQLException e) {
				e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<OmsNoReceiptDetailReport> getNoReceiptDetailReportByDay(long day) {
		long currTime = new Date().getTime();
		List<OmsNoReceiptDetail> list = this.queryObjects(sqlNoReceiptDetailByDate,day);
		List<OmsNoReceiptDetailReport> result = new ArrayList<OmsNoReceiptDetailReport>();
		if(list!=null && list.size()>0){
			for(OmsNoReceiptDetail detail:list){
				OmsNoReceiptDetailReport report = new OmsNoReceiptDetailReport();
				report.setConsigneeAddress(detail.getAddress());
				report.setConsigneeMobile(detail.getConsigneeTel());
				report.setCreateTime(currTime);
				report.setDate(day);
				WarehouseForm warehouseForm = warehouseDao.getWarehouseById(detail.getWarehouseId());
				if(warehouseForm!=null){
					report.setExpressCompany(warehouseForm.getExpressCompany());
					report.setWarehouseId(warehouseForm.getWarehouseId());
					report.setWarehouseName(warehouseForm.getWarehouseName());
				}
				report.setConsigneeName(detail.getConsigneeName());
				report.setMailNO(detail.getMailNo());
				report.setMailNOReturn(detail.getMailNo());
				report.setModifyTime(currTime);
				report.setOmsOrderFormId(detail.getOmsOrderFormId());
				report.setRejectTime(detail.getPackageStateUpdateTime());
				report.setPackageId(detail.getPackageId());
				result.add(report);
			}
		}
		return result;
	}

	@Override
	public OmsNoReceiptDetail getNoReceiptDetail(long day, long packageId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "packageId", packageId);
		SqlGenUtil.appendExtParamObject(sql, "day", day);
		return this.queryObject(sql);
	}


}

class NoReturn{
	long warehouseId;
	int omsOrderPackageState;
	int total;
}
