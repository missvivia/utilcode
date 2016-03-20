package com.xyl.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定义不需要登录的提示页
 * @author hzzhaozhenzuo
 *
 */
@Controller
@RequestMapping("/hint")
public class HintPageController {
	
	/**
	 * 区域未开通时，跳转的页面
	 * @return
	 */
	@RequestMapping("/nogoods")
	public String helpcenter() {
		return "pages/nogoods";
	}

}
