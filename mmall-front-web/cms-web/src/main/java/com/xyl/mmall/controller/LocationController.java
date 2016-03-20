package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;

@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {

	@Autowired
	private LocationFacade locationFacade;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private SiteCMSFacade siteCMSFacade;
	
	@RequestMapping(value = "/province", method = RequestMethod.GET)
	@RequiresPermissions({"location:area"})
	public @ResponseBody JSONObject getProvince() {
		return locationFacade.getProvinceListInOrder();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/provinceList", method = RequestMethod.GET)
	@RequiresPermissions({"location:area"})
	public @ResponseBody JSONObject getProvinceList() {
		long userId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationFacade.getAllProvince();
		List<JSONObject> listJsonObjects = new ArrayList<>();
		for (LocationCode locationCode : ret) {
			if(RetArgUtil.get(retArg, Boolean.class)||isContainValue(RetArgUtil.get(retArg, HashSet.class), locationCode.getCode(), 'p')){
				JSONObject obj = new JSONObject();
				obj.put("name", locationCode.getLocationName());
				obj.put("id", locationCode.getCode());
				listJsonObjects.add(obj);
			}
		}
		result.put("total", listJsonObjects.size());
		result.put("isAll", ret.size()==listJsonObjects.size());
		if(ret.size()==listJsonObjects.size()){
			JSONObject obj = new JSONObject();
			obj.put("name", "全国");
			obj.put("id", "00");
			listJsonObjects.add(0, obj);
		}
		result.put("list", listJsonObjects);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/city", method = RequestMethod.GET)
	@RequiresPermissions({"location:area"})
	public @ResponseBody JSONObject getCityList(@RequestParam long code) {
		long userId = SecurityContextUtils.getUserId();
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationService.getCityListByProvinceCode(code);
		//是否直辖市
		boolean isDisCity = false;
		List<JSONObject> listJsonObjects = new ArrayList<>();
		for (LocationCode locationCode : ret) {
			if(RetArgUtil.get(retArg, Boolean.class)||isContainValue(RetArgUtil.get(retArg, HashSet.class), locationCode.getCode(), 'c')){
				JSONObject obj = new JSONObject();
				obj.put("name", locationCode.getLocationName());
				obj.put("id", locationCode.getCode());
				if(locationCode.getCode()<0){
					isDisCity = true;
				}
				listJsonObjects.add(obj);
			}
		}
		result.put("total", listJsonObjects.size());
		if(!isDisCity&&ret.size()==listJsonObjects.size()){
			JSONObject obj = new JSONObject();
			obj.put("name", "全部城市");
			obj.put("id", "00");
			listJsonObjects.add(0, obj);
		}
		result.put("list", listJsonObjects);
		jsonObject.put("result", result);
		return jsonObject;
	}
	
	@RequestMapping(value = "/district", method = RequestMethod.GET)
	@RequiresPermissions({"location:area"})
	public @ResponseBody JSONObject getDistrictList(@RequestParam long code) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		JSONObject result = new JSONObject();
		List<LocationCode> ret = locationService.getDistrictListByCityCode(code);
		result.put("total", ret.size());
		List<JSONObject> listJsonObjects = new ArrayList<>();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", "全部区/县");
		jsonObj.put("id", "00");
		listJsonObjects.add(jsonObj);
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
	
	@RequestMapping(value = "/street", method = RequestMethod.GET)
	@RequiresPermissions({"location:area"})
	public @ResponseBody JSONObject getStreetList(@RequestParam long code) {
		return locationFacade.getStreetList(code);
	}
	
	
	//站点区域是否包含code
	private boolean isContainValue(Set<Long> areaIds, long code, char codeType){
		if(areaIds == null){
			return false;
		}
		if(areaIds.size() == 0){
			return true;//平台级
		}
		long  areaId = 0l;
		switch (codeType) {
			case 'p'://省
				for (long id : areaIds) {
					 areaId = id;
					if(Math.abs(areaId)/100==code){
						return true;
					}
				}
				break;
			case 'c'://市
				return areaIds.contains(code);
			case 'd'://区
		     break;
		default:
			break;
		}
		return false;
	}
	
	@RequestMapping(value = "/areaList")
	@RequiresPermissions({"location:area"})
	public @ResponseBody BaseJsonVO getAreaListByAgentId() {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(locationFacade.getAreaListByAgentId(SecurityContextUtils.getUserId(), "location:area"));
		return ret;
	}
}
