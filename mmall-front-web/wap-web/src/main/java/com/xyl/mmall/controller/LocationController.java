package com.xyl.mmall.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.ip.enums.LocationLevel;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {

	@Resource
	LocationFacade locationFacade;
	
	@RequestMapping(value = "/page/province", method = RequestMethod.GET)
	public String locationProvince() {
		return "pages/location/province";
	}
	
	@RequestMapping(value = "/page/city", method = RequestMethod.GET)
	public String locationCity() {
		return "pages/location/city";
	}
	
	@RequestMapping(value = "/page/district", method = RequestMethod.GET)
	public String locationDistrict() {
		return "pages/location/district";
	}
	
	@RequestMapping(value = "/page/street", method = RequestMethod.GET)
	public String locationStreet() {
		return "pages/location/street";
	}
	
	@RequestMapping(value = "/province", method = RequestMethod.GET)
	public @ResponseBody JSONObject getProvince() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		 List<LocationCode> lstLocation = locationFacade.getAllProvince();
		 Collections.sort(lstLocation, new Comparator<LocationCode>() {

			@Override
			public int compare(LocationCode o1, LocationCode o2) {
				if (o1 == o2) return 0;
				if (o1 == null || o1.getProvinceHead() == null) {
					return -1;
				}
				if (o2 == null || o2.getProvinceHead() == null) {
					return 1;
				}
				return o1.getProvinceHead().compareToIgnoreCase(o2.getProvinceHead());
			}
			 
		 });
		jsonObject.put("result", lstLocation);
		return jsonObject;
	}
	
	@RequestMapping(value = "/city", method = RequestMethod.GET)
	public @ResponseBody JSONObject getCityList(@RequestParam long code) {
		return locationFacade.getCityList(code);
	}
	
	@RequestMapping(value = "/district", method = RequestMethod.GET)
	public @ResponseBody JSONObject getDistrictList(@RequestParam long code) {
		return locationFacade.getDistrictList(code);
	}
	
	@RequestMapping(value = "/street", method = RequestMethod.GET)
	public @ResponseBody JSONObject getStreetList(@RequestParam long code) {
		return locationFacade.getStreetList(code);
	}
	
	@RequestMapping(value = "/cityInOrder", method = RequestMethod.GET)
	public @ResponseBody JSONObject getCityInOrder() {
		return locationFacade.getCityListInOrder();
	}
	
	@RequestMapping(value = "/currentArea", method = RequestMethod.GET)
	public @ResponseBody JSONObject getCurrentLocation(Long curSupplierAreaId, HttpServletResponse response){
		if (curSupplierAreaId == null || curSupplierAreaId < 1){
			curSupplierAreaId = AreaUtils.getAreaCode();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", curSupplierAreaId);
		jsonObject.put("name", locationFacade.getLocationNameByCode(curSupplierAreaId, true));
		return jsonObject;
	}
	
	/**
	 * 获取当前（指定）区域ID等级及父级对应的所在省、市、区、街道名称。
	 * 
	 * @param curSupplierAreaCode
	 * @return
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public @ResponseBody JSONObject getCurrent(Long curSupplierAreaCode){
		if (curSupplierAreaCode == null || curSupplierAreaCode < 1){
			curSupplierAreaCode = AreaUtils.getAreaCode();
		}
		return getAreaStack(curSupplierAreaCode);
	}
	
	/**
	 * 设置当前选择<code>LocationLevel.LEVEL_DISTRICT</code>区域到cookie。
	 * 
	 * @param curSupplierAreaCode
	 * @return
	 */
	@RequestMapping(value = "/current/set", method = RequestMethod.GET)
	public @ResponseBody JSONObject setCurrent(Long curSupplierAreaCode, HttpServletResponse response){
		if (curSupplierAreaCode == null || curSupplierAreaCode < 1){
			curSupplierAreaCode = AreaUtils.getAreaCode();
		}
		JSONObject jsonObject = getAreaStack(curSupplierAreaCode);
		if (StringUtils.isBlank(jsonObject.getString("districtCode"))) {
			jsonObject.put("code", ResponseCode.RES_ERROR);
			return jsonObject;
		}
		AreaUtils.setAreaCookie((HttpServletResponse) response, jsonObject.getLong("districtCode"));
		return jsonObject;
	}
	
	private JSONObject getAreaStack(long curSupplierAreaCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", ResponseCode.RES_SUCCESS);
		jsonObject.put("currentCode", curSupplierAreaCode);
		LocationCode locationCode = locationFacade.getLocationCodeByCode(curSupplierAreaCode);
		while (locationCode != null) {
			LocationLevel.NULL.genEnumByIntValue(1);
			if (locationCode.getLevel() == LocationLevel.LEVEL_STREET) {
				jsonObject.put("streetCode", locationCode.getCode());
				jsonObject.put("streetName", locationCode.getLocationName());
			} else if (locationCode.getLevel() == LocationLevel.LEVEL_DISTRICT) {
				jsonObject.put("districtCode", locationCode.getCode());
				jsonObject.put("districtName", locationCode.getLocationName());
			} else if (locationCode.getLevel() == LocationLevel.LEVEL_CITY) {
				jsonObject.put("cityCode", locationCode.getCode());
				jsonObject.put("cityName", locationCode.getLocationName());
			} else if (locationCode.getLevel() == LocationLevel.LEVEL_PROVICE) {
				jsonObject.put("provinceCode", locationCode.getCode());
				jsonObject.put("provinceName", locationCode.getLocationName());
			}
			if (locationCode.getLevel().getIntValue() <= LocationLevel.NULL.getIntValue()) {
				break;
			} 
			locationCode = locationFacade.getLocationCodeByCode(locationCode.getParentCode());
		}
		return jsonObject;
	}
	
}
