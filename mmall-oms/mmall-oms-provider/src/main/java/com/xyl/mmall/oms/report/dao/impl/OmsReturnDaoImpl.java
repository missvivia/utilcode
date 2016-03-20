package com.xyl.mmall.oms.report.dao.impl;

import java.math.BigDecimal;
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
import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.report.dao.OmsReturnDao;
import com.xyl.mmall.oms.report.meta.OmsReturnReport;

@Repository("OmsReturnDao")
public class OmsReturnDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsReturnReport> implements OmsReturnDao {
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	private String sqlReturn = "select b.warehouseId,b.state,count(*) as returnProduct "
			+ "from Mmall_Oms_OmsOrderForm as a,Mmall_Oms_RejectPackage as b,Mmall_Oms_Warehouse as c "
			+ "where a.OmsOrderFormId=b.omsOrderFormId and b.warehouseId=c.warehouseId and (b.state=20 or b.state=10) and a.shipTime>=? and a.shipTime<=? "
			+ "group by b.warehouseId,b.state";
	
	@Override
	public List<OmsReturnReport> getReturnList(long date){
		long currTime = new Date().getTime();
		long nextday = date + 24*3600*1000;
		DBResource source = this.getSqlSupport().excuteQuery(sqlReturn,date,nextday);
		List<OmsReturnReport> list = new ArrayList<OmsReturnReport>();
		ResultSet rs = source.getResultSet();
		try {
			Map<Long,List<Return>> map = new HashMap<Long,List<Return>>();
			while(rs!=null && rs.next()){
				Return obj = new Return();
				long warehouseId = rs.getLong("warehouseId");
				obj.warehouseId = warehouseId;
				obj.state = rs.getInt("state");
				obj.returnProduct = rs.getInt("returnProduct");
				if(map.get(warehouseId)!=null){
					List<Return> temp = map.get(warehouseId);
					temp.add(obj);
					map.put(warehouseId, temp);
				}else{
					List<Return> temp = new ArrayList<Return>();
					temp.add(obj);
					map.put(warehouseId, temp);
				}
			}
			Iterator<Long> it = map.keySet().iterator();
			while(it.hasNext()){
				long warehouseId = it.next();
				List<Return> temp = map.get(warehouseId);
				OmsReturnReport report = new OmsReturnReport();
				WarehouseForm warehouseForm = warehouseDao.getWarehouseById(warehouseId);
				report.setWarehouseId(warehouseId);
				if(warehouseForm!=null){
					report.setExpressCompany(warehouseForm.getExpressCompany());
					report.setWarehouseName(warehouseForm.getWarehouseName());
				}
				report.setCreateTime(currTime);
				report.setModifyTime(currTime);
				for(Return obj:temp){
					if(obj.state==10)
						report.setNoreturnCount(obj.returnProduct);
					else if(obj.state==20)
						report.setReturnCount(obj.returnProduct);
				}
				report.setDate(date);
				if(report.getReturnCount()+report.getNoreturnCount()==0)
					report.setReturnRate(BigDecimal.ZERO);
				else
					report.setReturnRate(new BigDecimal(1.0*report.getReturnCount()/(report.getReturnCount()+report.getNoreturnCount())));
				report.setReturnProduct(report.getReturnCount()+report.getNoreturnCount());
				list.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<OmsReturnReport> getReturnListByWarehouseAndDay(long warehouseId,long startDay,long endDay,List<Long> warehouseIds,DDBParam ddbParam){
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

	@Override
	public OmsReturnReport getReturnListByWarehouseAndDay(long warehouseId, long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "date", date);
		return queryObject(sql);
	}
	

}

class Return{
	long warehouseId;
	int state;
	int returnProduct;
}
