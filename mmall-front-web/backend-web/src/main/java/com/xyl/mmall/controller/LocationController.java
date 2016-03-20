package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.common.facade.LocationFacade;

@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {

	@Resource
	LocationFacade locationFacade;
	
	@RequestMapping(value = "/province", method = RequestMethod.GET)
	public @ResponseBody JSONObject getProvince() {
		return locationFacade.getProvinceListInOrder();
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
}
