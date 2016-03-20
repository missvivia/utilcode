package com.xyl.mmall.controller;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.util.AreaUtils;

@Controller
@RequestMapping("/m/location")
public class MobileLocationController extends BaseController {

	@Resource
	LocationFacade locationFacade;

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
	public @ResponseBody JSONObject getCurrentLocation(Long curSupplierAreaId) {
		if (curSupplierAreaId == null || curSupplierAreaId < 1) {
			curSupplierAreaId = AreaUtils.getAreaCode();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", curSupplierAreaId);
		jsonObject.put("name", locationFacade.getLocationNameByCode(curSupplierAreaId, true));
		return jsonObject;
	}

	@RequestMapping(value = "/setArea", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO setCurrentArea(ServletRequest request, ServletResponse response, String location) {
		String[] strs = location.split("\\|");
		BaseJsonVO baseJsonVO = new BaseJsonVO(ErrorCode.SUCCESS);
		if (strs.length <= 3) {
			baseJsonVO.setCode(ErrorCode.INCOMING_PARAMETER_IS_ILLEGAL);
			baseJsonVO.setMessage("THE INCOMING PARAMETER IS ILLEGAL");
			return baseJsonVO;
		}
		
		String cityCode = strs[strs.length-3];
		String sectionCode = strs[strs.length-1];
		String cityName = strs[strs.length-4];
		String sectionName = strs[strs.length-2];
		StringBuilder sb = new StringBuilder();
		sb.append(cityName).append(AreaUtils.AREA_VALUE_SPLIT).append(cityCode).append(AreaUtils.AREA_VALUE_SPLIT)
				.append(sectionName).append(AreaUtils.AREA_VALUE_SPLIT).append(sectionCode);
		AreaUtils.setAreaCookie((HttpServletResponse) response, sb.toString());
		return baseJsonVO;
	}

}
