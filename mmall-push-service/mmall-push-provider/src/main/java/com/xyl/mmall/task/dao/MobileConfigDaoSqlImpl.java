package com.xyl.mmall.task.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.task.meta.MobileConfig;

/**
 * 
 * @author jiangww
 *
 */
@Repository
public class MobileConfigDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<MobileConfig> implements
MobileConfigDao {
	private String sqlAll = "SELECT * FROM " + this.getTableName();
	private String sqlSelect = sqlAll + " WHERE name = ? ";

	@Override
	public MobileConfig getMobileConfig(String ConfigVersion) {
		return  queryObject(sqlSelect, ConfigVersion);
	}

	@Override
	public Map<String, String> getAllMobileConfig() {
		Map<String, String> map = new HashMap<String, String>();
		List<MobileConfig> list = queryObjects(sqlAll);
		for(MobileConfig config:list){
			map.put(config.getName(), config.getConfig());
		}
		return map;
	}

}
