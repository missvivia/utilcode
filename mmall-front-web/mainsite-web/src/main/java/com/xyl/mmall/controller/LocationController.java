package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.facade.AreaOnlineFacade;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {

	@Resource
	LocationFacade locationFacade;
	
	@Autowired
	private AreaOnlineFacade areaOnlineFacade;
	
	@RequestMapping(value = "/province", method = RequestMethod.GET)
	public @ResponseBody JSONObject getProvince() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 200);
		jsonObject.put("result", locationFacade.getAllProvince());
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
	public @ResponseBody JSONObject getCurrentLocation(Long curSupplierAreaId){
		if (curSupplierAreaId == null || curSupplierAreaId < 1){
			curSupplierAreaId = AreaUtils.getAreaCode();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", curSupplierAreaId);
		jsonObject.put("name", locationFacade.getLocationNameByCode(curSupplierAreaId, true));
		return jsonObject;
	}
	
	@RequestMapping(value = "/onlineArea", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getOnlineArea() {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(areaOnlineFacade.getAreaOnlineByStatus(1));
		return ret;
	}
	
	@RequestMapping(value = "/curAreaFullName", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO curAreaFullName() {
		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(locationFacade.getLocationNameByCode(AreaUtils.getAreaCode(), true));
		return ret;
	}
}
