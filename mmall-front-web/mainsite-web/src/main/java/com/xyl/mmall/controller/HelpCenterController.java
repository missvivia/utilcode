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
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.HelpCenterFacade;

/**
 * @author lihui
 *
 */
@Controller
@RequestMapping("/help")
public class HelpCenterController {

	@Autowired
	private HelpCenterFacade helpCenterFacade;

	@RequestMapping("")
	public String helpcenter(Model model) {
		return "pages/help.center";
	}
	
	@RequestMapping("/newer")
	public String helpnewer(Model model, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
		model.addAttribute("type", type);
		return "pages/help.newer";
	}

	/**
	 * 根据帮助类目查找已发布到web文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目ID
	 * @return 文章列表
	 */
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO findPublishedArticleByCategory(@RequestParam("categoryId") long categoryId) {
		return helpCenterFacade.findWebPublishedArticleByCategory(categoryId);
	}

	/**
	 * 获取帮助中心左边导航条的数据。
	 * 
	 * @return 左导航条数据
	 */
	@RequestMapping(value = "/leftNav", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO getHelpCenterLeftNav() {
		return helpCenterFacade.getHelpCenterLeftNav();
	}

	/**
	 * 根据关键字查询发布到web的帮助文章。
	 * 
	 * @param keywords
	 *            关键字
	 * @return 文章列表
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO searchPublishedArticle(@RequestParam("keywords") String keywords,
			@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
		return helpCenterFacade.searchWebPublishedArticle(keywords, limit, offset);
	}

}
