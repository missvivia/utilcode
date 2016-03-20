/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.config.TestPropertyConfiguration;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobileAddressFacade;
import com.xyl.mmall.mobile.facade.MobilePushBindFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileOS;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.service.MobileHeaderProcess;
import com.xyl.mmall.util.AreaUtils;

/**
 * @author lihui
 *
 */
@RestController
@RequestMapping("/m")
public class MobileDataBaseController {
	
	@Autowired
	private MobilePushBindFacade mobilePushBindFacade;
	@Autowired
	private MobileAddressFacade mobileAddressFacade;
	@Autowired
	private TestPropertyConfiguration propertyConfiguration;
	@RequestMapping(value = "/bindPush", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO bindUserId(HttpServletRequest request,@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
//		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
//		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int areaCode = (int) AreaUtils.getAreaCode();
		mobilePushBindFacade.bindReceiver(id, MobileHeaderProcess.getUserId() , areaCode, latitude, longitude);
		return new BaseJsonVO(ErrorCode.SUCCESS);
	}

	@RequestMapping(value = "/getProvinceCode", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getProvinceCode(HttpServletRequest request,@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		BaseJsonVO b = isAppTest(ao);
		if(b != null)
			return b;
		return mobilePushBindFacade.getProvinceCode(ao.getIp(), latitude, longitude);
	}

	public BaseJsonVO isAppTest(MobileHeaderAO ao){
		if(propertyConfiguration.isLockArea()){
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("areaCode", propertyConfiguration.getAreaCode());
			result.put("type", Converter.getMD5(System.currentTimeMillis()));
			return Converter.converterBaseJsonVO(result);
		}
		if(ao.getOs()!= null && MobileHeaderProcess.getOS(ao) == MobileOS.IOS.getIntValue()){
			if(Converter.protocolVersion(ao.getAppVersion()) == Converter.protocolVersion(propertyConfiguration.getLockVersion())){
				HashMap<String, String> result = new HashMap<String, String>();
				result.put("areaCode", propertyConfiguration.getAreaCode());
				result.put("type", Converter.getMD5(System.currentTimeMillis()));
				return Converter.converterBaseJsonVO(result);
			}
		}
		return null;
	}
	
	
	@RequestMapping(value = "/syncAreaCode", method = RequestMethod.GET)
	@ResponseBody
	public BaseJsonVO getProvinceCode(@RequestParam(value = "timestamp", required = false) Long timestamp) {
		return mobileAddressFacade.getAreaCodeList(timestamp);
	}
}
