package com.xyl.mmall.ip.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.ip.dao.LocationCodeDao;
import com.xyl.mmall.ip.enums.LocationLevel;
import com.xyl.mmall.ip.meta.LocationCode;

@Repository
public class LocationCodeDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<LocationCode> implements LocationCodeDao {
	/**
	 * 直辖市的code数组
	 */
	// private Long[] municipality = {11L, 12L, 31L, 50L};
	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getAllProvince() {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "level", LocationLevel.LEVEL_PROVICE.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		return queryObjects(sql);
	}

	private JSONObject getSelectedProvinces(String begin, String end) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "level", LocationLevel.LEVEL_PROVICE.getIntValue());
		sql.append(" AND valid = 1 AND ProvinceHead >= '").append(begin).append("' AND ProvinceHead <= '").append(end)
				.append("' ORDER BY ProvinceHead ASC");
		List<LocationCode> ret = queryObjects(sql);
		JSONObject object = new JSONObject();
		List<JSONObject> valueList = new ArrayList<>();
		String key = begin + "-" + end;
		object.put("key", key);
		for (LocationCode locationCode : ret) {
			JSONObject value = new JSONObject();
			value.put("name", locationCode.getLocationName());
			value.put("id", locationCode.getCode());
			if (locationCode.getCode() == 11L || locationCode.getCode() == 12L || locationCode.getCode() == 31L
					|| locationCode.getCode() == 50L) {
				value.put("special", true);
			} else {
				value.put("special", false);
			}
			valueList.add(value);
		}
		object.put("value", valueList);
		return object;
	}

	@Cacheable(value = "locationCache")
	@Override
	public JSONObject getAllProvinceInOrder() {
		JSONObject jsonObject = new JSONObject();
		// 不支持港澳台 高大上的地方不支持 34 - 3 = 31
		jsonObject.put("total", 31);
		List<JSONObject> list = new ArrayList<>();
		list.add(getSelectedProvinces("A", "G"));
		list.add(getSelectedProvinces("H", "K"));
		list.add(getSelectedProvinces("L", "S"));
		list.add(getSelectedProvinces("T", "Z"));
		jsonObject.put("list", list);
		return jsonObject;
	}

	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getCityListByProvinceCode(long provinceCode) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "parentCode", provinceCode);
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		return queryObjects(sql);
	}

	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getDistrictListByCityCode(long cityCode) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
//		if (cityCode == 11L || cityCode == 12L || cityCode == 31L || cityCode == 50L) {
//			// 直辖市
//			SqlGenUtil.appendExtParamObject(sql, "parentCode", cityCode);
//			SqlGenUtil.appendExtParamObject(sql, "valid", true);
//			List<LocationCode> dataList = queryObjects(sql);
//			List<LocationCode> out = new ArrayList<>();
//			for (LocationCode code : dataList) {
//				StringBuilder sqlInner = new StringBuilder(256);
//				sqlInner.append(genSelectSql());
//				SqlGenUtil.appendExtParamObject(sqlInner, "parentCode", code.getCode());
//				SqlGenUtil.appendExtParamObject(sqlInner, "valid", true);
//				out.addAll(queryObjects(sqlInner));
//			}
//			return out;
//		} else {
		SqlGenUtil.appendExtParamObject(sql, "parentCode", cityCode);
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
//		}
		return queryObjects(sql);
	}

	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getStreetListByDistrictCode(long districtCode) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "parentCode", districtCode);
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		return queryObjects(sql);
	}

	// singleName == false 已经被废弃
	@Cacheable(value = "locationCache")
	@Override
	public String getLocationNameByCode(long code) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (code < 0) {
			SqlGenUtil.appendExtParamObject(sql, "0 - code", 0 - code);
		} else {
			SqlGenUtil.appendExtParamObject(sql, "code", code);
		}
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		LocationCode location = queryObject(sql);
		return location.getLocationName();
	}

	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND updateTime >= " + timeStamp);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.ip.dao.LocationCodeDao#getLocationCodeListByCodeList(java.util.List)
	 */
	@Cacheable(value = "locationCache")
	@Override
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList) {
		if (codeList.size() == 0) {
			return new ArrayList<>();
		}
		StringBuilder codesString = new StringBuilder(100);
		codesString.append("(");
		for (Long code : codeList) {
			codesString.append(code).append(",");
		}
		codesString.deleteCharAt(codesString.lastIndexOf(","));
		codesString.append(")");
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		sql.append(" AND code IN ").append(codesString);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.ip.dao.LocationCodeDao#getLocationCode(long)
	 */
	@Override
	public LocationCode getLocationCode(long code) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		if (code < 0) {
			SqlGenUtil.appendExtParamObject(sql, "0 - code", 0 - code);
		} else {
			SqlGenUtil.appendExtParamObject(sql, "code", code);
		}
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		return queryObject(sql);
	}

	@Cacheable(value = "locationCache")
	@Override
	public JSONObject getAllCityInOrder() {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> list = new ArrayList<>();
		// 从A到Z
		for (int i = 65; i < 91; i++) {
			list.add(getSelectedCities(i));
		}
		jsonObject.put("list", list);
		return jsonObject;
	}

	private JSONObject getSelectedCities(int city) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "level", LocationLevel.LEVEL_CITY.getIntValue());
		sql.append(" AND valid = 1 AND ProvinceHead = '").append((char) city).append("' ORDER BY ProvinceHead ASC");
		List<LocationCode> ret = queryObjects(sql);
		JSONObject object = new JSONObject();
		List<JSONObject> valueList = new ArrayList<>();
		object.put("key", (char) city);
		for (LocationCode locationCode : ret) {
			JSONObject value = new JSONObject();
			value.put("name", locationCode.getLocationName());
			value.put("id", locationCode.getCode());
			if (locationCode.getCode() == 11L || locationCode.getCode() == 12L || locationCode.getCode() == 31L
					|| locationCode.getCode() == 50L) {
				value.put("special", true);
			} else {
				value.put("special", false);
			}
			valueList.add(value);
		}
		object.put("value", valueList);
		return object;
	}

	@Cacheable("locationCache")
	@Override
	public List<LocationCode> getAllCities() {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "level", LocationLevel.LEVEL_CITY.getIntValue());
		SqlGenUtil.appendExtParamObject(sql, "valid", true);
		return queryObjects(sql);
	}

	@Override
	@Cacheable("locationCache")
	public String getLocationFullNameByCode(long code) {
		StringBuilder name = new StringBuilder(64);
		LocationCode location = null;
		do {
			StringBuilder sql = new StringBuilder(128);
			sql.append(genSelectSql());
			if (code < 0) {
				SqlGenUtil.appendExtParamObject(sql, "0 - code", 0 - code);
			} else {
				SqlGenUtil.appendExtParamObject(sql, "code", code);
			}
			SqlGenUtil.appendExtParamObject(sql, "valid", true);
			location = queryObject(sql);
			if (location == null) {
				return null;
			}
			name.insert(0, location.getLocationName() + " ");
			if (code < 0) {
				break;
			}
			code = location.getParentCode();
		} while (location != null && code != 0);
		return name.toString();
	}
	
	// 这个方法废弃使用，直接从area表里面获得数据即可
//	@Deprecated
//	@Override
//	public Map<Long, String> getNamesByBusinessIdList(List<Long> ids) {
//		Map<Long, String> retMap = new HashMap<Long, String>();
//		if (ids.size() == 0) {
//			return retMap;
//		}
//		StringBuilder supplerListString = new StringBuilder(256);
//		supplerListString.append("(");
//		for (Long supplierId : ids) {
//			supplerListString.append(supplierId).append(",");
//		}
//		supplerListString.deleteCharAt(supplerListString.lastIndexOf(","));
//		supplerListString.append(") ");
//		StringBuilder sql = new StringBuilder(512);
//		sql.append("SELECT A.locationName, B.id FROM Mmall_IP_LocationCode A, Mmall_CMS_Business B ")
//			.append(" WHERE A.code = B.areaId AND B.id IN ").append(supplerListString);
//		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
//		ResultSet rs = dbr.getResultSet();
//		try {
//			while (rs.next()) {
//				long supplierId = rs.getLong("B.id");
//				String name = rs.getString("A.locationName");
//				retMap.put(supplierId, name);
//			}
//		} catch (SQLException e) {
//			return retMap;
//		} finally {
//			dbr.close();
//		}
//		return retMap;
//	}

}
