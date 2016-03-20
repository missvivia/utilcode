package com.xyl.mmall.ip.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.RetState;
import com.xyl.mmall.ip.meta.CodLocation;

public interface CodLocationDao extends AbstractDao<CodLocation> {

	public RetState isDistrictCod(long districtCode, ExpressType type);
	
	public RetState isStreetCod(long streetCode, ExpressType type);
	
	public boolean isLocationCod(long provinceCode, long cityCode,
			long districtCode, long streetCode, ExpressType type);
	
}
