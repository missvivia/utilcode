/**
 * 
 */
package com.xyl.mmall.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobileAppInfoFacade;
import com.xyl.mmall.mobile.facade.MobilePoFacade;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;
import com.xyl.mmall.service.MobileHeaderProcess;

/**
 * @author lihui
 *
 */
@RestController
@RequestMapping("/m")
public class MobileMainShowController {
	@Autowired
	private MobilePoFacade MobilePoFacade;
	@Autowired
	private MobileAppInfoFacade mobileAppInfoFacade;
	
	@RequestMapping(value = "/getScheduleCategory", method = RequestMethod.GET)
	public BaseJsonVO getScheduleChannel() {
		return MobilePoFacade.getScheduleChannel();
	}
	
	
	/**
	 * 
	 * 0------------------TODO  ---------
	 */
	
	
	//TODO 后台CMS 未完成
	@RequestMapping(value = "/getHeadInfo", method = RequestMethod.GET)
	public BaseJsonVO getHeadInfo(HttpServletRequest request) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		int os = MobileHeaderProcess.getOS(ao);
		String r = ao.getResolution();
		return mobileAppInfoFacade.getInitImage(areaCode,os,r);
	}
	
	//TODO 后台CMS 未完成
	@RequestMapping(value = "/getBannerInfo", method = RequestMethod.GET)
	public BaseJsonVO getBannerInfo(HttpServletRequest request) {
		MobileHeaderAO ao = MobileHeaderProcess.extraFromHeader(request);
		int areaCode = MobileHeaderProcess.getAreaCode(ao);
		return mobileAppInfoFacade.getBannerImage(areaCode,ao.getOs());
	}


}
