package com.xyl.mmall.oms.report.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.dao.OmsNoReturnDao;
import com.xyl.mmall.oms.report.meta.OmsNoReturnReport;

@Repository("OmsNoReturnDao")
public class OmsNoReturnDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsNoReturnReport> implements OmsNoReturnDao {
	

	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;
	
	private String sqlNoReturn = "select a.warehouseId,c.OmsOrderFormId,c.CartRPrice,c.Address,c.ConsigneeName,c.CashOnDelivery,c.ConsigneeTel,d.skuId,d.Count,b.MailNO,c.Province,c.City "
			+ "from Mmall_Oms_OmsOrderPackage as b,Mmall_Oms_RejectPackage as a,Mmall_Oms_OmsOrderForm as c,Mmall_Oms_OmsOrderPackageSku as d "
			+ "where b.MailNO=a.expressNO and a.OmsOrderFormId=c.OmsOrderFormId and d.OmsOrderFormId=c.OmsOrderFormId and a.state=10 and c.shipTime>=? and c.shipTime<=?";
	
	
	@Override
	public List<OmsNoReturnReport> getNoReturnList(long date){
		long currTime = new Date().getTime();
		long nextDay = date + 24*3600*1000;
		DBResource source = this.getSqlSupport().excuteQuery(sqlNoReturn,date,nextDay);
		List<OmsNoReturnReport> list = new ArrayList<OmsNoReturnReport>();
		ResultSet rs = source.getResultSet();
		try {
			while(rs!=null && rs.next()){
				OmsNoReturnReport omsNoReturnReport = new OmsNoReturnReport();
				omsNoReturnReport.setOmsOrderFormId(rs.getLong("OmsOrderFormId"));
				long warehouseId = rs.getLong("warehouseId");
				omsNoReturnReport.setWarehouseId(warehouseId);
				omsNoReturnReport.setPrice(new BigDecimal(rs.getDouble("CartRPrice")));
				omsNoReturnReport.setConsigneeAddress(rs.getString("Address"));
				omsNoReturnReport.setSkuId(rs.getLong("skuId"));
				//omsNoReturnReport.setCashOnDelivery(rs.getBoolean("CashOnDelivery"));
				omsNoReturnReport.setMailNO(rs.getString("MailNO"));
				omsNoReturnReport.setProvince(rs.getString("Province"));
				omsNoReturnReport.setCity(rs.getString("City"));
				omsNoReturnReport.setProductCount(rs.getInt("Count"));
				omsNoReturnReport.setModifyTime(currTime);
				omsNoReturnReport.setCreateTime(currTime);
				omsNoReturnReport.setDate(date);
				list.add(omsNoReturnReport);
			}
			for(OmsNoReturnReport omsNoReturnReport:list){
				OmsOrderForm omsOrderForm = omsOrderFormDao.getOmsOrderFormByOrderId(omsNoReturnReport.getOmsOrderFormId());
				omsNoReturnReport.setCashOnDelivery(omsOrderForm.isCashOnDelivery());
				omsNoReturnReport.setConsigneeMobile(omsOrderForm.getConsigneeMobile());
				omsNoReturnReport.setConsigneeName(omsOrderForm.getConsigneeName());
				WarehouseForm warehouseForm = warehouseDao.getWarehouseById(omsNoReturnReport.getWarehouseId());
				if(warehouseForm!=null){
					omsNoReturnReport.setExpressCompany(warehouseForm.getExpressCompany());
					omsNoReturnReport.setWarehouseName(warehouseForm.getWarehouseName());
					String warehouseProvince = warehouseForm.getProvince();
					if(warehouseProvince!=null && warehouseProvince.equals(omsNoReturnReport.getProvince()))
						omsNoReturnReport.setMailType("省内配送");
					else
						omsNoReturnReport.setMailType("省外配送");
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 
	 * @param warehouseName
	 * @param startDay
	 * @param endDay
	 * @param ddbParam
	 * @return
	 */
	@Override
	public List<OmsNoReturnReport> getNoReturnListByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam){
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
		return queryObjects(sql);
	}

}
