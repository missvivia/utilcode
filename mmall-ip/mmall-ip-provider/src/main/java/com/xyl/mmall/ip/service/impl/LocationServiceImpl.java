package com.xyl.mmall.ip.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.RetState;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.ip.dao.CodLocationDao;
import com.xyl.mmall.ip.dao.LocationCodeDao;
import com.xyl.mmall.ip.dao.RemoteAreaDao;

/**
 * 提供相关的地理位置code的服务
 * 
 * @author chengximing
 *
 */
@Service("locationService")
public class LocationServiceImpl implements LocationService {

	private Map<Long, String> provinceCodeNameMap = new HashMap<>();
	
	private Map<Long, String> cityCodeNameMap = new HashMap<Long, String>();

	@Autowired
	private LocationCodeDao locationCodeDao;
	
	@Autowired
	private CodLocationDao codLocationDao;
	
	@Autowired
	private RemoteAreaDao remoteAreaDao;

	@PostConstruct
	public void init() {
		buildProvinceCodeNameMap();
		buildCityCodeNameMap();
	}

	/**
	 * 设置省份code-name map
	 */
	private void buildProvinceCodeNameMap() {
		List<LocationCode> locationCodeList = getAllProvince();
		Map<Long, String> provinceCodeNameMapOfTmp = new HashMap<>();
		for (LocationCode locationCode : locationCodeList) {
			provinceCodeNameMapOfTmp.put(locationCode.getCode(), locationCode.getLocationName());
		}
		provinceCodeNameMap = provinceCodeNameMapOfTmp;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.ip.service.LocationService#getProvinceCodeNameMap()
	 */
	@Override
	public Map<Long, String> getProvinceCodeNameMap() {
		return ReflectUtil.cloneObj(provinceCodeNameMap);
	}

	@Override
	public List<LocationCode> getAllProvince() {
		return locationCodeDao.getAllProvince();
	}

	@Override
	public JSONObject getAllProvinceInOrder() {
		return locationCodeDao.getAllProvinceInOrder();
	}

	@Override
	public List<LocationCode> getCityListByProvinceCode(long provinceCode) {
		return locationCodeDao.getCityListByProvinceCode(provinceCode);
	}

	@Override
	public List<LocationCode> getDistrictListByCityCode(long cityCode) {
		return locationCodeDao.getDistrictListByCityCode(cityCode);
	}

	@Override
	public List<LocationCode> getStreetListByDistrictCode(long districtCode) {
		return locationCodeDao.getStreetListByDistrictCode(districtCode);
	}

	@Override
	public String getLocationNameByCode(long code, boolean singleName) {
		if (singleName) {
			return locationCodeDao.getLocationNameByCode(code);
		} else {
			return locationCodeDao.getLocationFullNameByCode(code);
		}
	}

	@Override
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp) {
		return locationCodeDao.getLocationCodeListAfterTimeStamp(timeStamp);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.ip.service.LocationService#getLocationCodeListByCodeList(java.util.List)
	 */
	@Override
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList) {
		return locationCodeDao.getLocationCodeListByCodeList(codeList);
	}

	@Override
	public RetState isDistrictCod(long districtCode, ExpressType type) {
		return codLocationDao.isDistrictCod(districtCode, type);
	}

	@Override
	public RetState isStreetCod(long streetCode, ExpressType type) {
		return codLocationDao.isStreetCod(streetCode, type);
	}
	
	@Override
	public boolean isLocationCod(long provinceCode, long cityCode,
			long districtCode, long streetCode, ExpressType type) {
		return codLocationDao.isLocationCod(provinceCode, cityCode, districtCode, streetCode, type);
//		RetState retDistrictState = isDistrictCod(districtCode, type);
//		if (retDistrictState == RetState.STATE_FALSE) {
//			return false;
//		} else if (retDistrictState == RetState.STATE_NOTFULLY_TRUE) {
//			RetState retCityState = isStreetCod(streetCode, type);
//			if (retCityState == RetState.STATE_FALSE) {
//				return false;
//			} else {
//				return true;
//			}
//		} else {
//			return true;
//		}
	}

	@Override
	public RetArg isDistrictRemote(long districtCode, ExpressType type) {
		return remoteAreaDao.isDistrictRemote(districtCode, type);
	}

	@Override
	public RetArg isCityRemote(long cityCode, ExpressType type) {
		return remoteAreaDao.isCityRemote(cityCode, type);
	}

	@Override
	public RetArg isLocationRemote(long provinceCode, long cityCode,
			long districtCode, ExpressType type) {
		RetArg retArg = new RetArg();
		RetArg retCityArg = isCityRemote(cityCode, type);
		RetState retStateCity = RetArgUtil.get(retCityArg, RetState.class);
		if (retStateCity == RetState.STATE_FALSE) {
			RetArgUtil.put(retArg, false);
			RetArgUtil.put(retArg, 0.0);
			return retArg;
		} else if (retStateCity == RetState.STATE_TRUE) {
			BigDecimal price = RetArgUtil.get(retCityArg, BigDecimal.class);
			RetArgUtil.put(retArg, true);
			RetArgUtil.put(retArg, price);
			return retArg;
		} else { // STATE_NOTFULLY_TRUE
			RetArg retDistrictArg = isDistrictRemote(districtCode, type);
			RetState retStateDistrict = RetArgUtil.get(retDistrictArg, RetState.class);
			if (retStateDistrict == RetState.STATE_TRUE) {
				BigDecimal price = RetArgUtil.get(retDistrictArg, BigDecimal.class);
				RetArgUtil.put(retArg, true);
				RetArgUtil.put(retArg, price);
				return retArg;
			} else {
				RetArgUtil.put(retArg, false);
				RetArgUtil.put(retArg, 0.0);
				return retArg;
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.ip.service.LocationService#getLocationCode(long)
	 */
	@Override
	public LocationCode getLocationCode(long code) {
		return locationCodeDao.getLocationCode(code);
	}

	@Override
	public JSONObject getAllCityInOrder() {
		return locationCodeDao.getAllCityInOrder();
	}

	@Override
	public Map<Long, String> getCityCodeNameMap() {
		return ReflectUtil.cloneObj(cityCodeNameMap);
	}

	public List<LocationCode> getAllCities() {
		return locationCodeDao.getAllCities();
	}
	
	/**
	 * 设置省份code-name map
	 */
	private void buildCityCodeNameMap() {
		List<LocationCode> locationCodeList = getAllCities();
		Map<Long, String> cityCodeNameMapOfTmp = new HashMap<>();
		for (LocationCode locationCode : locationCodeList) {
			cityCodeNameMapOfTmp.put(locationCode.getCode(), locationCode.getLocationName());
		}
		cityCodeNameMap = cityCodeNameMapOfTmp;
	}
	
	// @Deprecated
	// @Override
	// public Map<Long, String> getNamesByBusinessIdList(List<Long> ids) {
	// return locationCodeDao.getNamesByBusinessIdList(ids);
	// }

}
