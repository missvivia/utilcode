/**
 * 
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.cms.vo.CmsRoleVO;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.common.facade.AccountFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.PhoneNumberUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.meta.AgentArea;
import com.xyl.mmall.member.param.AgentAccountSearchParam;
import com.xyl.mmall.member.service.PermissionService;

/**
 * 权限管理相关。
 * 
 * @author lihui
 *
 */
@Controller
@RequestMapping(value = "/access")
public class AuthorityController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Autowired
	private AuthorityFacade authorityFacade;

	@Autowired
	private BusinessFacade businessFacase;

	@Autowired
	private LeftNavigationFacade leftNavigationFacade;

	@Resource
	private PermissionService permissionService;
	
	@Autowired
	private AccountFacade accountFacade;
	
	/**
	 * 跳转到角色管理页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/role" }, method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public String authorityManage(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/access/role";
	}

	/**
	 * 跳转到帐号管理页面。
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public String authorityAccount(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/access/account";
	}

	/**
	 * 分页获取当前用户可查看角色的列表。
	 * 
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	@RequestMapping(value = "/role/getlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO getRole(@RequestParam("limit") int limit, @RequestParam("offset") int offset) {
		return authorityFacade.getRole(SecurityContextUtils.getUserId(), limit, offset);
	}

	/**
	 * 获取当前用户指定角色的权限树。
	 * 
	 * @param roleId
	 *            指定角色ID
	 * @return
	 */
	@RequestMapping(value = "/role/getAccessList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO getAccessList(@RequestParam("roleId") long roleId) {
		return authorityFacade.getAccessList(SecurityContextUtils.getUserId(), roleId);
	}

	/**
	 * 创建/保存角色。
	 * 
	 * @param roleDetial
	 *            角色内容
	 * @return
	 */
	@RequestMapping(value = "/role/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO saveRole(@RequestBody CmsRoleVO roleDetial) {
		return authorityFacade.saveRole(roleDetial, SecurityContextUtils.getUserId());
	}

	/**
	 * 查看指定角色ID的详情。
	 * 
	 * @param roleId
	 *            指定角色ID
	 * @return
	 */
	@RequestMapping(value = "/role/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO findRole(@RequestParam("roleId") long roleId) {
		return authorityFacade.findRole(SecurityContextUtils.getUserId(), roleId);
	}

	/**
	 * 获取当前用户所关联的角色列表。
	 * 
	 * @param parentId
	 *            编辑角色时，所选角色的父角色ID。新建角色时不传该数据。
	 * @return
	 */
	@RequestMapping(value = "/role/getUserRole", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO findUserRole(@RequestParam(value = "parentId", required = false) String parentId) {
		return authorityFacade.findUserRole(SecurityContextUtils.getUserId(),
				StringUtils.isNumeric(parentId) ? Long.parseLong(parentId) : 0L);
	}

	/**
	 * 删除指定ID的角色。
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/role/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:role" })
	public @ResponseBody BaseJsonVO deleteRole(@RequestParam("roleId") long roleId) {
		return authorityFacade.deleteRole(SecurityContextUtils.getUserId(), roleId);
	}

	/**
	 * 获取指定管理员的角色列表。
	 * 
	 * @param id
	 *            管理员ID
	 * @return
	 */
	@RequestMapping(value = "/account/getRoleList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO getAdminRoleList(@RequestParam("id") long id) {
		return authorityFacade.getAdminRole(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 分页获取当前用户可查看的帐号列表。
	 * 
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	@RequestMapping(value = "/account/getlist", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO getAccountList(@RequestParam("limit") int limit, @RequestParam("offset") int offset,
			AgentAccountSearchParam searchParam) {
		return authorityFacade.getAccountList(SecurityContextUtils.getUserId(), limit, offset, searchParam);
	}

	/**
	 * 创建/更新运维帐号。
	 * 
	 * @param agent
	 *            运维帐号内容
	 * @return
	 */
	@RequestMapping(value = "/account/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO saveAccount(@RequestBody AgentVO agent) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		// 校验参数
		String validateResult = validateAgentAccount(agent);
		if (StringUtils.isNotBlank(validateResult)) {
			baseJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, validateResult);
			return baseJsonVO;
		}
		long userId = SecurityContextUtils.getUserId();
		// 获取当前登录用户
		AgentDTO currentUser = authorityFacade.getCurrentAgent();
		// 是否有权限
		if (authorityFacade.hasAuthorityToAssignRole(currentUser, agent.getRoleList())) {
			AgentDTO agentDTO = new AgentDTO();
			agentDTO.setId(agent.getId());
			agentDTO.setMobile(StringUtils.trim(agent.getMobile()));
			agentDTO.setName(StringUtils.trim(agent.getDisplayName()));
			agentDTO.setRealName(StringUtils.trim(agent.getName()));
			agentDTO.setDepartment(StringUtils.trim(agent.getDepartment()));
			agentDTO.setEmpNumber(StringUtils.trim(agent.getAccountNum()));
			agentDTO.setIsModifyPassword(agent.getIsModifyPassword());
			agentDTO.setPassword(agent.getPassword());
			// 如果角色列表不为空
			if (CollectionUtils.isNotEmpty(agent.getRoleList())) {
				List<RoleDTO> roleList = new ArrayList<>();
				List<PermissionDTO> authorityList = permissionService.findAgentAuthorityPermissionList();
				AgentType agentType = AgentType.USER;
				// 每个帐号只允许添加一个角色
				CmsRoleVO roleVO = agent.getRoleList().get(0);
				// ##判断是否创建普通用户，从而过滤带authority权限的角色。##
				// 根据最新的要求，普通的管理员也可以继续创建新的管理员，因此判断是否有权限管理的权限，从而决定是否是普通用户
				if (agentType != AgentType.ADMIN) {
					if (authorityFacade.hasAuthorityPermission(roleVO.getId(), authorityList)) {
						agentType = AgentType.ADMIN;
					} else {
						agentType = AgentType.USER;
					}
				}
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(roleVO.getId());
				validateResult = checkSiteAndAreas(roleVO, userId, roleDTO);
				if (StringUtils.isNotBlank(validateResult)) {
					baseJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, validateResult);
					return baseJsonVO;
				}
				for (RoleDTO toAddRole : roleList) {
					if (toAddRole.getId() == roleDTO.getId()) {
						logger.error("Assigning duplicate roles is not allowed!");
						baseJsonVO.setCodeAndMessage(ResponseCode.RES_EPARAM, "不允许添加重复的角色！");
						return baseJsonVO;
					}
				}
				roleList.add(roleDTO);
				agentDTO.setAgentType(agentType);
				agentDTO.setRoleList(roleList);
			}
			// 如果是创建
			if (agentDTO.getId() < 1l) {
				if (accountFacade.findAccountByUserName(agentDTO.getName()) != null) {
					baseJsonVO.setCodeAndMessage(ResponseCode.RES_EEXIST, "帐号已存在！");
					return baseJsonVO;
				}
			}
			AgentVO newVO = authorityFacade.saveAgentAccount(agentDTO, userId);
			if (newVO != null) {
				// 返回更新后的用户信息
				baseJsonVO.setResult(newVO);
			} else {
				logger.error("Failed to upsert agent {}", agentDTO.getName());
				baseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "保存用户帐号失败，该帐号用户已存在！");
				return baseJsonVO;
			}
			baseJsonVO.setCode(ResponseCode.RES_SUCCESS);
		} else {
			logger.error("Can not assign role that current user dosen't has authority to!");
			baseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "当前用户无赋予指定角色的权限！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * 查看指定ID的帐号详情。
	 * 
	 * @param id
	 *            指定帐号的ID
	 * @return
	 */
	@RequestMapping(value = "/account/edit", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO findAccount(@RequestParam("id") long id) {
		return authorityFacade.findAgentAccount(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 删除指定ID的帐号。
	 * 
	 * @param id
	 *            指定帐号的ID
	 * @return
	 */
	@RequestMapping(value = "/account/delete", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO deleteAccount(@RequestParam("id") long id) {
		return authorityFacade.deleteAgentAccount(SecurityContextUtils.getUserId(), id);
	}
	
	@RequestMapping(value = "/account/delBulk", method = RequestMethod.POST)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO deleteAccounts(@RequestBody JSONObject json) {
		BaseJsonVO ret = new BaseJsonVO();
		String ids = json.getString("ids");
		if (StringUtils.isBlank(ids)) {
			ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
			return ret;
		}
		List<Long> idList = new ArrayList<Long>(ids.split(",").length);
		for (String str : ids.split(",")) {
			if (!StringUtils.isNumeric(str)) {
				ret.setCodeAndMessage(ResponseCode.RES_EPARAM, "参数错误！");
				return ret;
			}
			idList.add(Long.parseLong(str));
		}
		ret = authorityFacade.deleteAgentAccounts(SecurityContextUtils.getUserId(), idList);
		return ret;
	}

	/**
	 * 锁定指定ID的帐号。
	 * 
	 * @param id
	 *            指定帐号的ID
	 * @return
	 */
	@RequestMapping(value = "/account/lock", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO lockAccount(@RequestParam("id") long id) {
		return authorityFacade.lockAgentAccount(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 解锁指定ID的帐号。
	 * 
	 * @param id
	 *            指定帐号的ID
	 * @return
	 */
	@RequestMapping(value = "/account/unlock", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO unlockAccount(@RequestParam("id") long id) {
		return authorityFacade.unlockAgentAccount(SecurityContextUtils.getUserId(), id);
	}

	/**
	 * 获取当前用户可查看的管理员列表。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/account/getAdminList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO getAdminList() {
		return authorityFacade.getAdminList(SecurityContextUtils.getUserId());
	}

	/**
	 * 获取当前用户可赋予的站点列表。
	 * 
	 * @return
	 */
	@RequestMapping(value = "/account/getSiteList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "location:site" })
	public @ResponseBody BaseJsonVO getSiteList() {
		return authorityFacade.getSiteList(SecurityContextUtils.getUserId(), "location:site");
	}
	
	@RequestMapping(value = "/account/getAreaList", method = RequestMethod.GET)
	@RequiresPermissions(value = { "access:account" })
	public @ResponseBody BaseJsonVO getAreaList(
			@RequestParam(value = "siteId", required = false, defaultValue = "0") long siteId,
			@RequestParam(value = "userId", required = false, defaultValue = "0") long userId) {
		BaseJsonVO ret = new BaseJsonVO();
		long uid = SecurityContextUtils.getUserId();
		ret.setResult(authorityFacade.getAreaList(uid, siteId, userId));
		ret.setCode(ResponseCode.RES_SUCCESS);
		return ret;
	}
	
	/**
	 * 验证
	 * @param agent
	 * @return
	 */
	private String validateAgentAccount(AgentVO agent) {
		if (StringUtils.isBlank(agent.getDisplayName())) {
			return "员工登录名不能为空！";
		}
		if (StringUtils.trim(agent.getDisplayName()).length() > 128) {
			return "帐号名过长！";
		}
//		if (!Pattern.matches(EMAIL_PATTERN, StringUtils.trim(agent.getDisplayName()))) {
//			return "帐号名必须是有效的电子邮件地址！";
//		}
		// 校验是否以op.xyl结尾
		if (!StringUtils.endsWith(agent.getDisplayName(), MmallConstant.CMS_ACCOUNT_SUFFIX)) {
			agent.setDisplayName(agent.getDisplayName() + MmallConstant.CMS_ACCOUNT_SUFFIX);
		}
		if (!PhoneNumberUtil.isMobilePhone(agent.getMobile())) {
			return "无效的手机号码！";
		}
		if (StringUtils.isNotBlank(agent.getDepartment()) && StringUtils.trim(agent.getDepartment()).length() > 64) {
			return "用户所在部门名称过长！";
		}
		if (StringUtils.isNotBlank(agent.getAccountNum()) && StringUtils.trim(agent.getAccountNum()).length() > 64) {
			return "用户工号过长！";
		}
		if (StringUtils.isNotBlank(agent.getName()) && StringUtils.trim(agent.getName()).length() > 64) {
			return "用户姓名过长！";
		}
		if (CollectionUtils.isEmpty(agent.getRoleList())) {
			return "请至少选择一个角色！";
		}
		if (agent.getIsModifyPassword() == 1) {
			if (!RegexUtils.isValidPassword(agent.getPassword())) {
				return "密码只能为6-20位数字或字母组合！";
			}
		}
		return null;
	}

	/**
	 * 检测站点和区域
	 * @param roleVO 角色
	 * @param userId
	 * @return
	 */
	private String checkSiteAndAreas(CmsRoleVO roleVO, long userId, RoleDTO roleDTO) {
		if (CollectionUtils.isNotEmpty(roleVO.getSiteList())) {
			List<Long> siteList = new ArrayList<>();
			// 用户关联的角色只添加一个站点
			SiteCMSVO site = roleVO.getSiteList().get(0);
			if (CollectionUtils.isEmpty(site.getAreaList())) {
				return "用户关联的角色必须包含至少一个站点下区域！";
			}
			long siteId = site.getSiteId();
			List<Long> curAreaIds = authorityFacade.getAreaIdList(userId, siteId);
			if (CollectionUtils.isEmpty(curAreaIds)) {
				return "当前用户没有该站点或区域的权限！";
			}
			List<AgentArea> areaList = new ArrayList<AgentArea>(site.getAreaList().size());
			for (SiteAreaVO siteArea : site.getAreaList()) {
				long areaId = siteArea.getAreaId();
				if (!curAreaIds.contains(areaId)) {
					return "当前用户没有该站点或区域的权限！";
				}
				AgentArea agentArea = new AgentArea();
				agentArea.setSiteId(siteId);
				agentArea.setAreaId(areaId);
				areaList.add(agentArea);
			}
			siteList.add(siteId);
			roleDTO.setSiteList(siteList);
			roleDTO.setAreaList(areaList);
		} else {
			logger.error("Assigning role must have at least on site!");
			return "用户关联的角色必须添加一个站点！";
		}
		return null;
	}
}
