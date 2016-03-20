/**
 * 
 */
package com.xyl.mmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyl.mmall.mainsite.facade.HelpCenterFacade;

/**
 * @author hzlihui2014
 *
 */
@Controller
public class HelpCenterController {

	@Autowired
	private HelpCenterFacade helpCenterFacade;

	/**
	 * 显示帮助中心页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String getHelpCenter(Model model) {
		model.addAttribute("navigation", helpCenterFacade.getHelpCenterLeftNav());
		return "pages/help/help.center";
	}

	/**
	 * 根据帮助类目查找已发布到APP的文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目ID
	 * @return 文章列表
	 */
	@RequestMapping(value = "/help/article", method = RequestMethod.GET)
	public String findAppPublishedArticleByCategory(@RequestParam("categoryId") long categoryId, Model model) {
		model.addAttribute("detail", helpCenterFacade.findAppPublishedArticleByCategory(categoryId));
		return "pages/help/help.detail";
	}
}
