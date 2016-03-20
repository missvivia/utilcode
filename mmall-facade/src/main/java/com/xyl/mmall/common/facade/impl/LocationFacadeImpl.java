package com.xyl.mmall.common.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.service.AgentService;

/**
 * 地理位置服务的相关接口
 * 
 * @author chengximing
 *
 */
@Facade("locationFacade")
public class LocationFacadeImpl implements LocationFacade {

	@Resource
	private LocationService locationService;
	
	@Resource
	private BusinessService businessService;
	
	@Resource
	private AgentService agentService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.LocationFacade#getAllProvince()
	 */
	@Cacheable(value = "allProvinceCache") 
	@Override
	public List<LocationCode> getAllProvince() {
		return locationService.getAllProvince();
	}

	@Override
	public JSONObject getProvinceListInOrder() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("result", locationService.getAllProvinceInOrder());
		return jsonObject;
	}

	@Override
	public JSONObject getCityList(long provinceCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationService.getCityListByProvinceCode(provinceCode);
		result.put("total", ret.size());
		List<JSONObject> listJsonObjects = new ArrayList<>(ret.size());
		for (LocationCode locationCode : ret) {
			JSONObject obj = new JSONObject();
			obj.put("name", locationCode.getLocationName());
			obj.put("id", locationCode.getCode());
			listJsonObjects.add(obj);
		}
		result.put("list", listJsonObjects);
		jsonObject.put("result", result);
		return jsonObject;
	}

	@Override
	public JSONObject getDistrictList(long cityCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationService.getDistrictListByCityCode(cityCode);
		result.put("total", ret.size());
		List<JSONObject> listJsonObjects = new ArrayList<>(ret.size());
		for (LocationCode locationCode : ret) {
			JSONObject obj = new JSONObject();
			obj.put("name", locationCode.getLocationName());
			obj.put("id", locationCode.getCode());
			listJsonObjects.add(obj);
		}
		result.put("list", listJsonObjects);
		jsonObject.put("result", result);
		return jsonObject;
	}

	@Override
	public JSONObject getStreetList(long districtCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationService.getStreetListByDistrictCode(districtCode);
		result.put("total", ret.size());
		List<JSONObject> listJsonObjects = new ArrayList<>(ret.size());
		for (LocationCode locationCode : ret) {
			JSONObject obj = new JSONObject();
			obj.put("name", locationCode.getLocationName());
			obj.put("id", locationCode.getCode());
			listJsonObjects.add(obj);
		}
		result.put("list", listJsonObjects);
		jsonObject.put("result", result);
		return jsonObject;
	}

	@Override
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp) {
		return locationService.getLocationCodeListAfterTimeStamp(timeStamp);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.LocationFacade#getLocationCodeListByCodeList(java.util.List)
	 */
	@Override
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList) {
		return locationService.getLocationCodeListByCodeList(codeList);
	}

	// @Override
	// public Map<Long, String> getNamesByBusinessIdList(List<Long> ids) {
	// return locationService.getNamesByBusinessIdList(ids);
	// }

	@Override
	public String getLocationNameByCode(long code, boolean singleName) {
		return locationService.getLocationNameByCode(code, singleName);
	}

	@Override
	public JSONObject getCityListInOrder() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("result", locationService.getAllCityInOrder());
		return jsonObject;
	}

	@Override
	public List<AreaDTO> getAreaListByAgentId(long agentId, String permissions) {
		return businessService.getAreadByIdList(agentService.findAgentSiteIdsByPermission(agentId, permissions));
	}

	@Override
	public LocationCode getLocationCodeByCode(long code) {
		return locationService.getLocationCode(code);
	}
}
