/**
 * 
 */
package com.xyl.mmall.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AuthorityFacade;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;
import com.xyl.mmall.backend.vo.BackendRoleVO;
import com.xyl.mmall.backend.vo.DealerVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.util.IPUtils;

/**
 * 权限管理相关
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping(value = "/authority")
public class AuthorityController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorityController.class);

	@Autowired
	private AuthorityFacade authorityFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	/**
	 * 权限管理中的用户组管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/authority" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public String authorityManage(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/authority/authority";
	}

	/**
	 * 权限管理中的账号管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public String authorityAccount(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/authority/account";
	}

	/**
	 * 分页获取当前用户可查看的用户组列表。
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/userGroup/getlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO getUserGroup(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
		return authorityFacade.getUserGroup(SecurityContextUtils.getUserId(), limit, offset);
	}

	/**
	 * 获取当前用户的权限列表。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userGroup/getAccessList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO getAccessList() {
		return authorityFacade.getAccessList(SecurityContextUtils.getUserId());
	}

	/**
	 * 新建/更新用户组。
	 * 
	 * @param roleDetial
	 *            用户组信息
	 * @return
	 */
	@RequestMapping(value = "/userGroup/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO saveUserGroup(@RequestBody BackendRoleVO roleDetial) {
		return authorityFacade.saveUserGroup(roleDetial, SecurityContextUtils.getUserId());
	}

	/**
	 * 查找指定ID的用户组详情。
	 * 
	 * @param groupId
	 *            用户组ID
	 * @return
	 */
	@RequestMapping(value = "/userGroup/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO findUserGroup(@RequestParam("groupId") long groupId) {
		return authorityFacade.findUserGroup(SecurityContextUtils.getUserId(), groupId);
	}

	/**
	 * 删除指定ID的用户组。
	 * 
	 * @param groupId
	 *            用户组ID
	 * @return
	 */
	@RequestMapping(value = "/userGroup/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO deleteUserGroup(@RequestParam("groupId") long groupId) {
		return authorityFacade.deleteUserGroup(SecurityContextUtils.getUserId(), groupId);
	}

	/**
	 * 获取当前用户创建的所有用户组.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/getGroupList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:userGroup" })
	public @ResponseBody BaseJsonVO getAllGroupList() {
		return authorityFacade.getUserGroup(SecurityContextUtils.getUserId(), 500, 0);
	}

	/**
	 * 分页获取当前用户创建的账号列表.
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 */
	@RequestMapping(value = "/user/getlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO getUserList(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
		return authorityFacade.getUserList(SecurityContextUtils.getUserId(), limit, offset);
	}

	/**
	 * 创建/保存账号。
	 * 
	 * @param dealer
	 * @return
	 */
	@RequestMapping(value = "/user/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO saveUser(@RequestBody DealerVO dealer,HttpServletRequest request) {
		logger.info("backend User Save by "+ IPUtils.getOriginalClientIp(request)+" "+SecurityContextUtils.getUserName());
		return authorityFacade.saveDealerUser(dealer, SecurityContextUtils.getUserId());
	}

	/**
	 * 查看指定ID的账号详情。
	 * 
	 * @param id
	 *            商家后台账号ID
	 * @return
	 */
	@RequestMapping(value = "/user/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO findUser(@RequestParam("id") long id) {
		return authorityFacade.findDealerUser(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 删除指定ID的账号。
	 * 
	 * @param id
	 *            商家后台账号ID
	 * @return
	 */
	@RequestMapping(value = "/user/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO deleteUser(@RequestParam("id") long id) {
		return authorityFacade.deleteDealerUser(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 锁定指定ID的账号。
	 * 
	 * @param id
	 *            商家后台账号ID
	 * @return
	 */
	@RequestMapping(value = "/user/lock", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO lockUser(@RequestParam("id") long id) {
		return authorityFacade.lockDealerUser(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 解锁指定ID的账号。
	 * 
	 * @param id
	 *            商家后台账号ID
	 * @return
	 */
	@RequestMapping(value = "/user/unlock", method = RequestMethod.GET)
	@RequiresPermissions(value = { "authority:account" })
	public @ResponseBody BaseJsonVO unlockUser(@RequestParam("id") long id) {
		return authorityFacade.unlockDealerUser(SecurityContextUtils.getUserId(), id);
	}
}
