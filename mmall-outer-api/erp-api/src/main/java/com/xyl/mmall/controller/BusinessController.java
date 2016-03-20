/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.controller;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * BusinessController.java created by yydx811 at 2015年8月10日 下午12:15:26
 * 供应商，店铺相关
 *
 * @author yydx811
 */
@RestController
public class BusinessController {

	private static Logger logger = Logger.getLogger(BusinessController.class);
	
	@Autowired
	private BusinessFacade businessFacade;
	
	/**
	 * 按id获取店铺信息
	 * @param businessId
	 * @param appid
	 * @return
	 */
	@RequestMapping(value = "/busi/getBusinessInfo", method = RequestMethod.GET)
	public BaseJsonVO getBusinessInfo(@RequestParam(value = "businessId", required = true) long businessId,
			@RequestParam(value = "appid") String appid) {
		BaseJsonVO ret = new BaseJsonVO();
		// get binded relationship
		String businessIds = ERPAccountUtils.getERPAccountBusinessIdsByAppId(appid);
		if (StringUtils.isBlank(businessIds)) {
			logger.info("No businessId bind to appid! AppId : " + appid + ".");
			ret.setCodeAndMessage(ResponseCode.RES_ERROR, "No businessId bind to appid!");
			return ret;
		}
		if (!Arrays.asList(businessIds.split(",")).contains(String.valueOf(businessId))) {
			logger.info("BusinessId bind to appid! AppId : " + appid + ", BusinessId : " + businessId + ".");
			ret.setCodeAndMessage(ResponseCode.RES_FORBIDDEN, "BusinessId doesn't bind to appid!");
			return ret;
		}
		ret.setResult(businessFacade.getBusinessById(businessId));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}
}
