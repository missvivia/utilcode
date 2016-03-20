/**
 * ==================================================================
 * Copyright (c) XINYUNLIAN Co.ltd Hangzhou, 2015-2016
 * 
 * 杭州新云联技术有限公司拥有该文件的使用、复制、修改和分发的许可权
 * 如果你想得到更多信息，请访问 <http://www.xinyunlian.com>
 *
 * XINYUNLIAN Co.ltd Hangzhou owns permission to use, copy, modify and
 * distribute this documentation.
 * For more information, please see <http://www.xinyunlian.com>
 * ==================================================================
 */

package com.xyl.mmall.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.meta.ERPAccount;
import com.xyl.mmall.member.service.ERPAccountService;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * GreetingController.java created by skh at 2015年6月4日 上午11:28:03
 * 
 *
 * @author skh
 * @version 1.0
 */
@RestController
public class GreetingController {

	@Resource
	private ERPAccountService erpAccountService;
	
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam(value = "username") String username,
            HttpServletRequest request, HttpServletResponse response) {
        return "Hello " + username + ", Welcome to Mmall!";
    }
    
    @RequestMapping(value = "/addErpAccount", method = RequestMethod.GET)
    public BaseJsonVO addErpAccount(@RequestParam(value = "businessIds") String businessIds,
    		String appId) {
    	BaseJsonVO ret = new BaseJsonVO();
    	if (StringUtils.equals(appId, "yyerptest0001")) {
    		ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Not Found!");
    		return ret;
    	}
    	ERPAccount erpAccount = erpAccountService.addERPAccount(businessIds);
    	if (erpAccount == null) {
    		ret.setCodeAndMessage(ResponseCode.RES_ERROR, "fail!");
    	} else {
    		ERPAccountUtils.getInstance().addOne(erpAccount);
    		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "ok!");
    		ret.setResult(erpAccount);
    	}
    	return ret;
    }
    
    @RequestMapping(value = "/reloadErpAccountMap", method = RequestMethod.GET)
    public BaseJsonVO reloadErpAccountMap(String appId) {
    	BaseJsonVO ret = new BaseJsonVO();
    	if (StringUtils.equals(appId, "yyerptest0001")) {
    		ret.setCodeAndMessage(ResponseCode.RES_ENOTEXIST, "Not Found!");
    		return ret;
    	}
		// 重载erp account
		List<ERPAccount> erpAccountList = erpAccountService.getAll();
		if (CollectionUtils.isNotEmpty(erpAccountList)) {
			ERPAccountUtils.getInstance().initMap(erpAccountList);
		}
		ret.setCodeAndMessage(ResponseCode.RES_SUCCESS, "ok!");
    	return ret;
    }
}
