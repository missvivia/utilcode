package com.xyl.mmall.task.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.task.dto.DeviceLocationDTO;
import com.xyl.mmall.task.meta.DeviceLocation;

/**
 * 
 * @author jiangww
 *
 */
@Repository
public class DeviceLocationDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<DeviceLocation> implements
		DeviceLocationDao {
	private String sqlAreaCode = "SELECT * FROM " + this.getTableName() + " GROUP BY areaCode";

	private String sqlSelectByAreaCode = "SELECT * FROM " + this.getTableName() + " WHERE id > ? AND areaCode = ?";

	private String sqlSelectByID = "SELECT * FROM " + this.getTableName() + " WHERE deviceId = ?";

	private String updateSql = "UPDATE " + this.getTableName()
			+ " SET areaCode = ?,userId=?,updateTime = ?,PlatformType=? WHERE deviceId = ?";

	private String sqlSelectByAccount = "SELECT * FROM " + this.getTableName() + " WHERE userId = ?";

	@Override
	public List<DeviceLocation> getPushUserList(Map<String, String[]> map, long startID) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND id >");
		sql.append(startID);
		if (map.get("platformType") != null) {
			sql.append(" AND platformType in (");
			String types[] = map.get("platformType");

			for (int i = 0; i < types.length; i++) {
				i++;
				sql.append("?");
				if (i < types.length) {
					sql.append(",");
				}
				param.add(types[i - 1]);
			}
			sql.append(")");
		}
		if (map.get("areaCode") != null) {
			sql.append(" AND areaCode in(");
			String types[] = map.get("areaCode");

			for (int i = 0; i < types.length;) {
				i++;
				sql.append("?");
				if (i < types.length) {
					sql.append(",");
				}
				param.add(types[i - 1]);
			}
			sql.append(")");
		}
		sql.append(" order by id limit 1000");
		return queryObjects(sql.toString(), param);
	}

	@Override
	public List<DeviceLocation> getAllAreaCode() {
		return queryObjects(sqlAreaCode);
	}

	@Override
	public List<DeviceLocation> getPushUserByArea(long areaCode, long startID) {
		return queryObjects(sqlSelectByAreaCode, startID, areaCode);
	}

	@Override
	public DeviceLocationDTO insertOrUpdate(DeviceLocationDTO deviceLocationDTO) {
		if (StringUtils.isBlank(deviceLocationDTO.getDeviceId())) {
			throw new ServiceException("no DeviceId");
		}
		DeviceLocation deviceLocation = queryObject(sqlSelectByID, deviceLocationDTO.getDeviceId());
		if (deviceLocation == null) {
			this.addObject(deviceLocationDTO);
		} else {
			if(deviceLocationDTO.getAreaCode() == 0l){
				deviceLocationDTO.setAreaCode(deviceLocation.getAreaCode());
			}
			this.getSqlSupport().excuteUpdate(updateSql, deviceLocationDTO.getAreaCode(),
					deviceLocationDTO.getUserId(), System.currentTimeMillis(), deviceLocationDTO.getPlatformType(),deviceLocationDTO.getDeviceId());

		}
		return deviceLocationDTO;
	}

	@Override
	public List<DeviceLocation> getPushUserByAccount(long userId) {
		return queryObjects(sqlSelectByAccount, userId);
	}

}
