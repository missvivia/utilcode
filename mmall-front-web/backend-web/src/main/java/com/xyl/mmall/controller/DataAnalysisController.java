/**
 * 
 */
package com.xyl.mmall.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class DataAnalysisController {

	@Value("${datasite.url:http://sj.baiwandian.cn/}")
	private String dataSiteUrl;

	/**
	 * 数据报表中的数据罗盘页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/data/compass" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "data:compass" })
	public ModelAndView dataCompassPage(Model model) {
		return new ModelAndView(new RedirectView(dataSiteUrl));
	}
}
