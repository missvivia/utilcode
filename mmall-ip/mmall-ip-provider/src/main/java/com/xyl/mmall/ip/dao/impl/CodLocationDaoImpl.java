package com.xyl.mmall.ip.dao.impl;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.ip.dao.CodLocationDao;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.LocationLevel;
import com.xyl.mmall.ip.enums.RetState;
import com.xyl.mmall.ip.meta.CodLocation;

@Repository
public class CodLocationDaoImpl extends 
	PolicyObjectDaoSqlBaseOfAutowired<CodLocation> implements CodLocationDao {

	@Override
	public RetState isDistrictCod(long districtCode, ExpressType type) {
		// TODO Auto-generated method stub
		return RetState.STATE_TRUE;
	}

	@Override
	public RetState isStreetCod(long streetCode, ExpressType type) {
		// TODO Auto-generated method stub
		return RetState.STATE_FALSE;
	}

	@Override
	public boolean isLocationCod(long provinceCode, long cityCode,
			long districtCode, long streetCode, ExpressType type) {
		if (streetCode > 0) {
			StringBuilder sql = new StringBuilder(256);
			sql.append(genSelectSql());
			SqlGenUtil.appendExtParamObject(sql, "code", streetCode);
			SqlGenUtil.appendExtParamObject(sql, "valid", true);
			SqlGenUtil.appendExtParamObject(sql, "level", LocationLevel.LEVEL_STREET.getIntValue());
			SqlGenUtil.appendExtParamObject(sql, "type", type.getIntValue());
			if (queryObjects(sql).size() != 0) {
				return true;
			}
		}
		return false;
	}

}
