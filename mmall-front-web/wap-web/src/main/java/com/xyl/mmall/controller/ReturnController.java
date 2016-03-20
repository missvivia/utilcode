package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyl.mmall.mainsite.facade.ReturnPackageFacade;

/**
 * 退货
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午11:05:05
 *
 */
@Controller
@RequestMapping("/returnorder")
public class ReturnController extends BaseController {

	@Autowired
	private ReturnPackageFacade retFacade;

	/**
	 * 退货列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getRetOrdSkuList(Model model, @RequestParam(value = "ordPkgId") long ordPkgId) {
		appendStaticMethod(model);
		model.addAttribute("data", retFacade.getReturnOrderSkuVOList(ordPkgId));
		return "pages/return/return";
	}

}
