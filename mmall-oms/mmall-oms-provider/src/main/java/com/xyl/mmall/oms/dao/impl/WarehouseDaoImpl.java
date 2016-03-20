/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.WarehouseDao;
import com.xyl.mmall.oms.meta.WarehouseForm;

/**
 * @author hzzengdan
 *
 */
@Repository
public class WarehouseDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<WarehouseForm> implements WarehouseDao {

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.WarehouseDao#getList()
	 */
	@Override
	public List<WarehouseForm> getList() {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.WarehouseDao#getWarehouseById(long)
	 */
	@Override
	public WarehouseForm getWarehouseById(long id) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "warehouseId", id);
		return queryObject(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.WarehouseDao#getIdList()
	 */
	@Override
	public List<Long> getIdList() {
		List<Long> outList = new ArrayList<>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("select warehouseId from Mmall_Oms_Warehouse group by warehouseId");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				outList.add(rs.getLong("warehouseId"));
			}
		} catch (SQLException e) {
			return outList;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return outList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.WarehouseDao#getExpressCompanyList()
	 */
	@Override
	public List<String> getExpressCompanyList() {
		List<String> outList = new ArrayList<>();
		StringBuilder sql = new StringBuilder(256);
		sql.append("select expressCompany from Mmall_Oms_Warehouse group by expressCompany");
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while (rs.next()) {
				outList.add(rs.getString("expressCompany"));
			}
		} catch (SQLException e) {
			return outList;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return outList;
	}

	@Override
	public List<WarehouseForm> getListByProvinceList(List<Long> areaLists) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if(areaLists!=null && areaLists.size()>0)
		{
			StringBuilder sb = new StringBuilder();
			for(Long id:areaLists){
				sb.append(id).append(",");
			}
			sql.append(" AND provinceId in (" + sb.toString().substring(0, sb.toString().length()-1) + ")");
		}	
		return queryObjects(sql);

	}


}
