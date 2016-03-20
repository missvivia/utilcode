package com.xyl.mmall.oms.report.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.report.dao.SendOutProvinceFormDao;
import com.xyl.mmall.oms.report.meta.OmsSendOutProvinceForm;

@Repository("SendOutProvinceFormDao")
public class SendOutProvinceFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsSendOutProvinceForm> implements SendOutProvinceFormDao {

	
	@Override
	public List<OmsSendOutProvinceForm> getDataByWarehouseAndDay(long warehouseId, long startDay, long endDay,List<Long> warehouseIds,
			DDBParam ddbParam) {
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

	/**
	 * 根据日期和仓库获取发货数据
	 * @param warehouseId
	 * @param date
	 * @return
	 */
	@Override
	public int getSendOutByWarehouseId(long warehouseId,long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "date", date);
		OmsSendOutProvinceForm form = this.queryObject(sql);
		if(form!=null)
			return form.getNum();
		return 0;
	}

	/**
	 * 根据日期和仓库获取发货数据
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.report.dao.SendOutProvinceFormDao#getSendOutByWarehouseIdAndDay(long, long)
	 */
	@Override
	public OmsSendOutProvinceForm getSendOutByWarehouseIdAndDay(long warehouseId, long date) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", warehouseId);
		SqlGenUtil.appendExtParamObject(sql, "date", date);
		return this.queryObject(sql);
	}
	
}
