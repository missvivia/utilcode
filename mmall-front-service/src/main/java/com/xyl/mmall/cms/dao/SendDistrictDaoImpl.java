package com.xyl.mmall.cms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.cms.meta.SendDistrict;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;

@Repository
public class SendDistrictDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<SendDistrict> implements SendDistrictDao {
	
	private static Logger logger = Logger.getLogger(SendDistrictDaoImpl.class);
	
	private String tableName = this.getTableName();

	private String sqlDeleteByBusinesssID = "DELETE FROM " + tableName + " WHERE BusinessId=?";

	@Override
	public List<SendDistrict> getDistrictsByBusinessId(long businessId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "BusinessId", businessId);
		return queryObjects(sql);
	}

	@Override
	public List<SendDistrict> getDistrictListByBusinessIds(List<Long> businessIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND BusinessId in (");
		for (Long id : businessIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");
		return queryObjects(sql);
	}

	@Override
	public List<SendDistrict> getDistrictListByDistrictIds(List<Long> districtIds) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("select distinct DistrictId,DistrictName from Mmall_CMS_SendDistrict");
		sql.append(" WHERE DistrictId in (");
		for (Long id : districtIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");
		List<SendDistrict> sendDistricts = new ArrayList<SendDistrict>();
		DBResource resource = getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = resource.getResultSet();
		try {
			while (rs != null && rs.next()) {
				SendDistrict sendDistrict = new SendDistrict();
				sendDistrict.setDistrictId(rs.getLong("DistrictId"));
				sendDistrict.setDistrictName(rs.getString("DistrictName"));
				sendDistricts.add(sendDistrict);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return sendDistricts;
	}

	@Override
	public boolean deleteByBusinessId(long businessId) {
		return this.getSqlSupport().excuteUpdate(sqlDeleteByBusinesssID, businessId) > 0;
	}

	@Override
	public boolean batchDeleteByBusinessId(List<Long> businessIds) {
		if (CollectionUtil.isEmptyOfList(businessIds)) {
			return false;
		}
		StringBuilder sql = new StringBuilder(256);
		sql.append("DELETE FROM Mmall_CMS_SendDistrict WHERE BusinessId in ");
		sql.append("(");
		for (Long id : businessIds) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(") ");

		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public List<Long> getBusinessIdByDistrictId(long areaId, int areaFlag) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT BusinessId FROM ");
		sql.append(this.getTableName()).append(" WHERE ProvinceId = 0 OR ");
		if (areaFlag == 1) {
			sql.append("(ProvinceId = ").append(areaId).append(" AND CityId = 0)");
		} else {
			if (areaFlag == 2) {
				sql.append("(ProvinceId = ").append(areaId / 100l).append(" AND CityId = 0) OR ");
				sql.append("(CityId = ").append(areaId).append(" AND DistrictId = 0)");
			} else {
				if (areaFlag == 3) {
					sql.append("(ProvinceId = ").append(areaId / 10000l).append(" AND CityId = 0) OR ");
					long cityId = areaId / 100l;
					if (cityId == 5001 || cityId == 3101 || cityId == 1201 || cityId == 1101) {
						sql.append("(0 - CityId = ").append(cityId).append(" AND DistrictId = 0) OR ");
					} else {
						sql.append("(CityId = ").append(areaId / 100l).append(" AND DistrictId = 0) OR ");
					}
					sql.append("DistrictId = ").append(areaId);
				} else {
					return null;
				}
			}
		}
		DBResource dbResource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == dbResource) ? null : dbResource.getResultSet();
		if (null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			dbResource.close();
		}
		return null;
	}

}
