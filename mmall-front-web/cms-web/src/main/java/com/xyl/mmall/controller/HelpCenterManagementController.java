/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.netease.push.util.JSONUtils;
import com.xyl.mmall.cms.facade.HelpCenterManagementFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.vo.HelpArticleVO;
import com.xyl.mmall.cms.vo.HelpContentCategoryVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 帮助中心管理相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping(value = "/content/helpcenter")
public class HelpCenterManagementController extends BaseController {

	@Autowired
	private HelpCenterManagementFacade helpCenterManagementFacade;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	/**
	 * 帮助中心
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public String helpcenter(Model model) {
		appendStaticMethod(model);
		List<HelpContentCategoryVO> categoryList= helpCenterManagementFacade.getHelpArticleCategory();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryList_json", JSONUtils.getJson(categoryList));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/content/helpcenter";
	}

	/**
	 * 帮助中心文章编辑
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/articleEdit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public String articleEdit(Model model,@RequestParam(value="id", defaultValue="0") long id ) {
		appendStaticMethod(model);
		List<HelpContentCategoryVO> categoryList= helpCenterManagementFacade.getHelpArticleCategory();
		HelpArticleVO helpArticle =helpCenterManagementFacade.getHelpArticleDetail(id);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryList_json", JSONUtils.getJson(categoryList));
		model.addAttribute("helpArticle", helpArticle);
		if(helpArticle!=null)
			model.addAttribute("helpArticle_json",JSONUtils.getJson(helpArticle));
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/content/helpcenter.articleEdit";
	}

	/**
	 * 根据条件查询文章的列表。
	 * 
	 * @param categoryId
	 *            帮助类目ID
	 * @return 文章列表
	 */
	@RequestMapping(value = "/article/search", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO searchArticleByPublicTypeAndCategoryAndKeywords(
			@RequestParam("publishType") int publishType, @RequestParam("categoryId") long categoryId,
			@RequestParam(value = "keywords", required = false) String keywords, @RequestParam("limit") int limit,
			@RequestParam("offset") int offset) {
		return helpCenterManagementFacade.searchArticleByPublicTypeAndCategoryAndKeywords(publishType, categoryId,
				keywords, limit, offset);
	}

	/**
	 * 创建/更新文章。
	 * 
	 * @param article
	 *            文章内容
	 * @return
	 */
	@RequestMapping(value = "/article/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO saveHelpArticle(@RequestBody HelpArticleVO article) {
		return helpCenterManagementFacade.saveHelpArticle(article, SecurityContextUtils.getUserId(),
				SecurityContextUtils.getUserName());
	}

	/**
	 * 保存并发布文章。
	 * 
	 * @param article
	 *            文章内容
	 * @return
	 */
	@RequestMapping(value = "/article/savepublish", method = RequestMethod.POST)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO saveAndPublishHelpArticle(@RequestBody HelpArticleVO article) {
		return helpCenterManagementFacade.saveAndPublishHelpArticle(article, SecurityContextUtils.getUserId(),
				SecurityContextUtils.getUserName());
	}

	/**
	 * 发布文章。
	 * 
	 * @param article
	 *            文章内容
	 * @return
	 */
	@RequestMapping(value = "/article/publish", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO publishHelpArticle(@RequestParam("id") long id) {
		return helpCenterManagementFacade.publishHelpArticle(id, SecurityContextUtils.getUserId());
	}

	/**
	 * 撤销发布文章。
	 * 
	 * @param article
	 *            文章内容
	 * @return
	 */
	@RequestMapping(value = "/article/revoke", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO revokeHelpArticle(@RequestParam("id") long id) {
		return helpCenterManagementFacade.revokeHelpArticle(id, SecurityContextUtils.getUserId());
	}

	/**
	 * 删除文章。
	 * 
	 * @param article
	 *            文章内容
	 * @return
	 */
	@RequestMapping(value = "/article/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "content:helpcenter" })
	public @ResponseBody BaseJsonVO deleteHelpArticle(@RequestParam("id") long id) {
		return helpCenterManagementFacade.deleteHelpArticle(id, SecurityContextUtils.getUserId());
	}

//	/**
//	 * 同步已有数据至NCS的Log中
//	 * @return
//	 */
//	@RequestMapping(value = "/article/ncssync", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "content:helpcenter" })
//	public @ResponseBody BaseJsonVO ncssync() {
//		helpCenterManagementFacade.ncssync();
//		return new BaseJsonVO();
//	}
}
