package com.xyl.mmall.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xyl.mmall.mainsite.facade.CartFacade;

/**
 * Hello world!
 *
 */
@Controller
@RequestMapping("")
public class JobController {
	@Resource
	private CartFacade cartFacade;

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
	
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String getcart(Model model) {
		return "pages/cart";
	}

//	// job调用示例方法
//	@RequestMapping("/job/hello")
//	@ResponseBody
//	public ModelAndView hello(JobParam jobParam) {
//		logger.info("begin execute hello,id:" + jobParam.getCommonParam().getId() + ",signature:" + jobParam.getCommonParam().getSignature());
//
//		// 调用facade服务
//		return JobResultUtil.fillJobResultIntoModle(jobParam.getCommonParam().getId(), jobParam.getCommonParam().getSignature(), true);
//	}

//	@RequestMapping("/callback")
//	@ResponseBody
//	public BaseJsonVO callback(@RequestParam String id, @RequestParam String signature, @RequestParam String code,
//			@RequestParam String result, @RequestParam String timestamp, @RequestParam String nonce) {
//		BaseJsonVO vo = new BaseJsonVO();
//		vo.setResult(code);
//		return vo;
//	}

}
