package com.xyl.mmall.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.ItemCenterFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年7月30日下午4:01:25
 */
@Controller
@RequestMapping("/store")
public class StoreController {
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@Autowired
	private ItemCenterFacade itemCenterFacade;
	
	@RequestMapping(value = "/index")
	//@RequiresPermissions(value = { "store:index" })
	//TODO
	public String index(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/store/index";
	}
	
	
	

	/**
	 * 设置起批金额
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/updateBatchCash", method = RequestMethod.GET)
	//@RequiresPermissions(value = { "store:index" })
	public @ResponseBody  BaseJsonVO setBatchCash(@RequestParam("cash") BigDecimal cash,@RequestParam("isOpen") Boolean isOpen) {
		BaseJsonVO ret = new BaseJsonVO();
		long loginId = SecurityContextUtils.getUserId();
		long businessId = itemCenterFacade.getSupplierId(loginId);
		boolean result = businessFacade.updateBatchCash(businessId, loginId, cash, isOpen);
		if(result){
			ret.setCode(ResponseCode.RES_SUCCESS);
			ret.setMessage("新增或者编辑备注成功！");
		}else{
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("新增或者编辑备注失败！");
		}
		return ret;
	}
}
