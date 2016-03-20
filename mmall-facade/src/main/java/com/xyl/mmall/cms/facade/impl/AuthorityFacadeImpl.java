/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.AgentPermissionVO;
import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.cms.vo.CmsRoleVO;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.dto.AgentAreaDTO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.param.AgentAccountSearchParam;
import com.xyl.mmall.member.service.AccountService;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.PermissionService;
import com.xyl.mmall.member.service.RoleService;

/**
 * @author lihui
 *
 */
@Facade("cmsAuthorityFacade")
public class AuthorityFacadeImpl implements AuthorityFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityFacadeImpl.class);

	@Resource
	private AgentService agentService;

	@Resource
	private RoleService roleService;

	@Resource
	private PermissionService permissionService;

	@Resource
	private BusinessService businessService;

	@Resource
	private AccountService accountService;
	
	@Resource
	private DealerService dealerService;
	
	@Resource
	private SiteCMSService siteCMSService;
	
	@Resource
	private LocationService locationService;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getRole(long, int,
	 *      int)
	 */
	@Override
	public BaseJsonVO getRole(long userId, int limit, int offset) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		List<RoleDTO> roleList = null;
		AgentDTO currentUser = agentService.findAgentById(userId);
		List<AgentDTO> adminList = new ArrayList<>();
		// 如果是ROOT用户，可查看任意的角色
		if (currentUser.getAgentType() == AgentType.ROOT) {
			roleList = roleService.findAllCmsRole(limit, offset);
			List<AgentDTO> adminAgentList = agentService.findAgentByAgentType(AgentType.ADMIN);
			if (CollectionUtils.isNotEmpty(adminAgentList)) {
				adminList.addAll(adminAgentList);
			}
			adminList.addAll(agentService.findAgentByAgentType(AgentType.ROOT));
		} else {
			adminList.add(currentUser);
			roleList = roleService.findAllCmsRoleByOwnerId(userId, limit, offset);
		}
		if (CollectionUtils.isEmpty(roleList)) {
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			List<CmsRoleVO> roleVOList = new ArrayList<>();
			Set<Long> modifierIds = new HashSet<>();
			for (RoleDTO roleDTO : roleList) {
				modifierIds.add(roleDTO.getLastModifiedBy());
			}
			List<AgentDTO> modifierList = agentService.findAgentByIdList(new ArrayList<>(modifierIds));
			for (RoleDTO roleDTO : roleList) {
				if (CollectionUtils.isNotEmpty(modifierList)) {
					for (AgentDTO modifier : modifierList) {
						if (modifier.getId() == roleDTO.getLastModifiedBy()) {
							roleDTO.setOwnerName(modifier.getName());
							break;
						}
					}
				}
				roleVOList.add(new CmsRoleVO(roleDTO));
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(roleVOList);
			if (currentUser.getAgentType() == AgentType.ROOT) {
				listResult.setTotal(roleService.countAllCmsRole());
			} else {
				listResult.setTotal(roleService.countAllCmsRoleByOwnerId(userId));
			}
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getAccessList(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO getAccessList(long userId, long roleId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		RoleDTO selectedRole = null;
		// 如果是ROOT用户，可获取任意的角色
		if (currentUser.getAgentType() == AgentType.ROOT) {
			selectedRole = roleService.findRoleById(roleId);
			if (null == selectedRole) {
				LOGGER.error("Current user (id = {}) try to search an invalid role {}!", userId, roleId);
				baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
				baseJsonVO.setMessage("无效的角色ID！");
				return baseJsonVO;
			}
		} else {
			// 否则需要检查当前用户是否有权限查看指定的角色
			List<RoleDTO> roleList = roleService.findCmsUserRole(userId);
			if (CollectionUtils.isEmpty(roleList)) {
				LOGGER.error("Current user (id = {}) dosen't have any role!", userId);
				baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
				baseJsonVO.setMessage("获取用户可关联权限时，当前用户无任何角色！");
				return baseJsonVO;
			}
			for (RoleDTO roleDTO : roleList) {
				if (roleDTO.getId() == roleId) {
					selectedRole = roleDTO;
				}
			}
			if (selectedRole == null) {
				LOGGER.error("Current user (id = {}) dosen't have role {}!", userId, roleId);
				baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
				baseJsonVO.setMessage("获取用户可关联权限时，无权查看指定角色权限！");
				return baseJsonVO;
			}
		}
		List<RoleDTO> selectedRoleList = new ArrayList<>();
		selectedRoleList.add(selectedRole);
		// 判断是否需要过滤权限管理的权限。
		// ##如果所选的角色为ROOT权限（parentId为0），则创建的角色需要包含权限管理，否则需过滤权限管理的权限。##
		// 根据最新要求，去除普通管理员不能分配角色管理的限制
		List<PermissionDTO> permissionList = permissionService.findPermissionsByAgentRoles(selectedRoleList, false);
		if (CollectionUtils.isEmpty(permissionList)) {
			return baseJsonVO;
		}
		List<Long> parentIds = new ArrayList<>();
		List<AgentPermissionVO> permissionVOList = new ArrayList<>();
		Map<Long, List<AgentPermissionVO>> childrenPermissionMap = new HashMap<>();
		for (PermissionDTO permissionDTO : permissionList) {
			boolean findParent = false;
			for (Long parentId : parentIds) {
				if (parentId.equals(permissionDTO.getParentId())) {
					findParent = true;
					break;
				}
			}
			if (!findParent) {
				// 未找到parent节点，根据parentId查找父节点权限。
				parentIds.add(permissionDTO.getParentId());
			}
			if (childrenPermissionMap.containsKey(permissionDTO.getParentId())) {
				childrenPermissionMap.get(permissionDTO.getParentId()).add(new AgentPermissionVO(permissionDTO));
			} else {
				List<AgentPermissionVO> childrenList = new ArrayList<>();
				childrenList.add(new AgentPermissionVO(permissionDTO));
				childrenPermissionMap.put(permissionDTO.getParentId(), childrenList);
			}
		}
		List<PermissionDTO> parentDTOList = permissionService.findPermissionsIncludeParentByIdListAndCategory(
				parentIds, AuthzCategory.CMS.getIntValue());
		for (PermissionDTO parentDTO : parentDTOList) {
			AgentPermissionVO parentVO = new AgentPermissionVO(parentDTO);
			parentVO.setChildren(childrenPermissionMap.get(parentDTO.getId()));
			permissionVOList.add(parentVO);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		baseJsonVO.setResult(permissionVOList);
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#saveAgentAccount(com.xyl.mmall.cms.vo.AgentVO,
	 *      long)
	 */
	@Override
	public AgentVO saveAgentAccount(AgentDTO agentDTO, long userId) {
		AgentDTO newDTO = agentService.upsertAgent(agentDTO, userId);
		if (newDTO != null) {
			// 返回更新后的用户信息
			AgentVO agentVO = new AgentVO(newDTO);
			List<CmsRoleVO> roleVOList = new ArrayList<>();
			for (RoleDTO roleDTO : newDTO.getRoleList()) {
				roleVOList.add(new CmsRoleVO(roleDTO));
			}
			agentVO.setRoleList(roleVOList);
			return agentVO;
		}
		return null;
	}


	@Override
	public boolean hasAuthorityPermission(Long id, List<PermissionDTO> authorityList) {
		RoleDTO role = roleService.findRoleById(id);
		if (role != null && !StringUtils.isEmpty(role.getPermissions())) {
			// TODO:decrypt permissions string
			String[] perArr = role.getPermissions().split(",");
			for (String permission : perArr) {
				for (PermissionDTO authority : authorityList) {
					if (permission.equals(String.valueOf(authority.getId()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasAuthorityToAssignRole(AgentDTO currentUser, List<CmsRoleVO> roleList) {
		if (CollectionUtils.isEmpty(roleList)) {
			return false;
		}
		// 如果是ROOT用户，可任意分配任意权限
		if (currentUser.getAgentType() == AgentType.ROOT) {
			return true;
		}
		for (CmsRoleVO role : roleList) {
			RoleDTO roleDTO = roleService.findRoleById(role.getRole().getId());
			if (null == roleDTO || roleDTO.getOwnerId() != currentUser.getId()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#findAgentAccount(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO findAgentAccount(long userId, long id) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO agentDTO = agentService.findAgentWithRoleById(id);
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可任意查看账号。
		if (currentUser.getAgentType() == AgentType.ROOT) {
			baseJsonVO.setResult(buildAgentAccountDetial(agentDTO));
			baseJsonVO.setCode(ErrorCode.SUCCESS);
			return baseJsonVO;
		}
		if (agentDTO != null && !CollectionUtils.isEmpty(agentDTO.getRoleList())) {
			for (RoleDTO role : agentDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					baseJsonVO.setResult(buildAgentAccountDetial(agentDTO));
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to view account {}!", currentUser.getName(),
					agentDTO.getName());
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无查看指定账号的权限！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * 组装运维平台用户详情。
	 * 
	 * @param agentDTO
	 *            运维平台用户数据
	 * @return 返回的用户详情
	 */
	private AgentVO buildAgentAccountDetial(AgentDTO agentDTO) {
		AgentVO agentVO = new AgentVO(agentDTO);
		// 准备角色数据
		if (!CollectionUtils.isEmpty(agentDTO.getRoleList())) {
			List<CmsRoleVO> roleVOList = new ArrayList<>();
			for (RoleDTO roleDTO : agentDTO.getRoleList()) {
				agentVO.setAdminId(roleDTO.getOwnerId());
				CmsRoleVO roleVO = new CmsRoleVO(roleDTO);
				// 准备站点数据
				if (!CollectionUtils.isEmpty(roleDTO.getSiteList())) {
					List<SiteCMSVO> siteList = new ArrayList<>();
					for (Long siteId : roleDTO.getSiteList()) {
						SiteCMSVO site = new SiteCMSVO();
						site.setSiteId(siteId);
						siteList.add(site);
					}
					roleVO.setSiteList(siteList);
				}
				roleVOList.add(roleVO);
			}
			agentVO.setRoleList(roleVOList);
		}
		return agentVO;
	}

	/**
	 * 组装运维平台用户详情。
	 * 
	 * @param agentDTO
	 *            运维平台用户数据
	 * @param siteList
	 *            所有站点列表
	 * @return 返回的用户详情
	 */
	private AgentVO buildAgentAccountDetial(AgentDTO agentDTO, List<SiteCMSDTO> siteList) {
		AgentVO agentVO = new AgentVO(agentDTO);
		// 准备角色数据
		if (!CollectionUtils.isEmpty(agentDTO.getRoleList())) {
			List<CmsRoleVO> roleVOList = new ArrayList<>();
			for (RoleDTO roleDTO : agentDTO.getRoleList()) {
				agentVO.setAdminId(roleDTO.getOwnerId());
				CmsRoleVO roleVO = new CmsRoleVO(roleDTO);
				// 准备站点数据
				if (!CollectionUtils.isEmpty(roleDTO.getSiteList())) {
					List<SiteCMSVO> siteSelectedList = new ArrayList<>();
					for (Long siteId : roleDTO.getSiteList()) {
						for(SiteCMSDTO site : siteList){
							if(siteId.equals(site.getId())){
								siteSelectedList.add(new SiteCMSVO(site));
							}
						}
					}
					roleVO.setSiteList(siteSelectedList);
				}
				roleVOList.add(roleVO);
			}
			agentVO.setRoleList(roleVOList);
		}
		return agentVO;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#deleteAgentAccount(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO deleteAgentAccount(long userId, long id) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可修改删除账号。
		AgentDTO agentDTO = agentService.findAgentWithRoleById(id);
		if (currentUser.getAgentType() == AgentType.ROOT) {
			agentService.deleteAgentById(id, agentDTO.getName());
			baseJsonVO.setCode(ErrorCode.SUCCESS);
			LOGGER.info("Delete Agent Success! UserId : {}, AgentId : {}", id, userId);
			return baseJsonVO;
		}
		if (agentDTO != null && !CollectionUtils.isEmpty(agentDTO.getRoleList())) {
			for (RoleDTO role : agentDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					agentService.deleteAgentById(id, agentDTO.getName());
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					LOGGER.info("Delete Agent Success! UserId : {}, AgentId : {}", id, userId);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to delete account {}!", currentUser.getName(),
					agentDTO.getName());
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无删除指定账号的权限！");
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getAccountList(long,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO getAccountList(long userId, int limit, int offset, AgentAccountSearchParam searchParam) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		List<AgentDTO> agentList = agentService.findAgentByRoleOwner(userId, limit, offset, searchParam);
		if (CollectionUtils.isEmpty(agentList)) {
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			BasePageParamVO<SiteCMSDTO> pageParamVO = new BasePageParamVO<SiteCMSDTO>();
			pageParamVO = siteCMSService.getSiteCMSList(null, pageParamVO);
			List<AgentVO> agentVOList = new ArrayList<>();
			List<SiteCMSDTO> siteList = pageParamVO.getList();
			if (CollectionUtils.isNotEmpty(siteList)) {
				for (AgentDTO agentDTO : agentList) {
					agentVOList.add(buildAgentAccountDetial(agentDTO, siteList));
				}
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(agentVOList);
			listResult.setTotal(agentService.countAgentByRoleOwner(userId, searchParam));
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#deleteRole(long, long)
	 */
	@Override
	@Transaction
	public BaseJsonVO deleteRole(long userId, long roleId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		RoleDTO roleDTO = roleService.findRoleById(roleId);
		if (null != roleDTO && roleDTO.getParentId() == 0L) {
			LOGGER.error("Delete ROOT role is not allowed by user (id={})", userId);
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("ROOT角色无法被删除。如有疑问，请联系系统管理员。");
			return baseJsonVO;
		}
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可删除任意角色。
		if (roleDTO != null && (roleDTO.getOwnerId() == userId || currentUser.getAgentType() == AgentType.ROOT)) {
			agentService.deleteAgentRoleByRoleId(roleId);
			roleService.deleteRoleById(roleId);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
			LOGGER.info("Delete Role Success! RoleId : {}, AgentId : {}", roleId, userId);
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#findRole(long, long)
	 */
	@Override
	public BaseJsonVO findRole(long userId, long roleId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		RoleDTO roleDTO = roleService.findRoleById(roleId);
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可查看任意角色。
		if (roleDTO != null && (roleDTO.getOwnerId() == userId || currentUser.getAgentType() == AgentType.ROOT)) {
			CmsRoleVO cmsRoleVO = new CmsRoleVO(roleDTO);
			if (!CollectionUtils.isEmpty(roleDTO.getPermissionList())) {
				List<AgentPermissionVO> permissionVOList = new ArrayList<>();
				for (PermissionDTO permissionDTO : roleDTO.getPermissionList()) {
					permissionVOList.add(new AgentPermissionVO(permissionDTO));
				}
				cmsRoleVO.setAccessList(permissionVOList);
			}
			baseJsonVO.setResult(cmsRoleVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#saveRole(com.xyl.mmall.cms.vo.CmsRoleVO,
	 *      long)
	 */
	@Override
	public BaseJsonVO saveRole(CmsRoleVO roleDetial, long userId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		String validateResult = validateAgentRole(roleDetial);
		if (StringUtils.isNotBlank(validateResult)) {
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage(validateResult);
			return baseJsonVO;
		}
		if (hasAuthorityToAssignPermission(userId, roleDetial.getParent(), roleDetial.getAccessList())) {
			// 保存初始的角色信息
			RoleDTO orgRole = null;
			if (0 != roleDetial.getId()) {
				orgRole = roleService.findRoleById(roleDetial.getId());
				if (orgRole.getParentId() == 0L) {
					LOGGER.error("Edit ROOT role is not allowed by user (id={})", userId);
					baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
					baseJsonVO.setMessage("ROOT角色无法被编辑。如有疑问，请联系系统管理员。");
					return baseJsonVO;
				}
			}
			RoleDTO roleDTO = new RoleDTO();
			if (roleDetial.getId() != null && roleDetial.getId() != 0) {
				roleDTO.setId(roleDetial.getId());
			} else {
				roleDTO.setCreateTime(System.currentTimeMillis());
			}
			// 设置当前被编辑的角色的拥有者。如果是新的role，拥有者为当前用户
			if (null == orgRole) {
				roleDTO.setOwnerId(userId);
			} else {
				// 如果在编辑角色，检查选择的parent role是否为自身的角色。
				// 最初先将角色的拥有者设置为原有的拥有者
				roleDTO.setOwnerId(orgRole.getOwnerId());
				List<RoleDTO> currentUserRoleList = roleService.findCmsUserRole(userId);
				if (!CollectionUtils.isEmpty(currentUserRoleList)) {
					for (RoleDTO userRole : currentUserRoleList) {
						if (userRole.getId() == roleDetial.getParent()) {
							// 选择的角色parent为用户拥有的一个角色，将角色拥有者改为当前用户
							roleDTO.setOwnerId(userId);
							break;
						}
					}
				}
			}
			roleDTO.setCategory(AuthzCategory.CMS);
			roleDTO.setDisplayName(StringUtils.trim(roleDetial.getName()));
			roleDTO.setLastModifiedBy(userId);
			roleDTO.setLastModifiedTime(System.currentTimeMillis());
			roleDTO.setParentId(roleDetial.getParent());
			List<PermissionDTO> permissions = new ArrayList<>();
			for (AgentPermissionVO permissionVO : roleDetial.getAccessList()) {
				permissions.add(permissionVO.getPermission());
			}
			roleDTO.setPermissionList(permissions);
			RoleDTO newRoleDTO = roleService.upsertRole(roleDTO);
			newRoleDTO.setOwnerName(agentService.findAgentById(userId).getName());
			// 检查是否有需要迭代删除的权限
			if (orgRole != null) {
				// TODO:decrypt permissions string
				String[] orgPermissionsArr = StringUtils.split(orgRole.getPermissions(), ",");
				orgList: for (String orgPermission : orgPermissionsArr) {
					for (PermissionDTO newPermission : permissions) {
						if (orgPermission.equals(String.valueOf(newPermission.getId()))) {
							continue orgList;
						}
					}
					removeChildrenPermission(roleDTO.getId(), orgPermission);
				}
			}
			// 返回更新后的角色组数据
			CmsRoleVO cmsRoleVO = new CmsRoleVO(newRoleDTO);
			if (!CollectionUtils.isEmpty(newRoleDTO.getPermissionList())) {
				List<AgentPermissionVO> permissionVOList = new ArrayList<>();
				for (PermissionDTO permissionDTO : newRoleDTO.getPermissionList()) {
					permissionVOList.add(new AgentPermissionVO(permissionDTO));
				}
				cmsRoleVO.setAccessList(permissionVOList);
			}
			baseJsonVO.setResult(cmsRoleVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		} else {
			LOGGER.error("Agent (id = {}) dosen't have authority to assgin permissions to {}.", userId,
					roleDetial.getName());
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("权限赋予失败，超出该账号权限的范围！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * 校验角色信息
	 * 
	 * @param roleDetial
	 */
	private String validateAgentRole(CmsRoleVO roleDetial) {
		if (StringUtils.isBlank(roleDetial.getName())) {
			return "角色名称不能为空！";
		}
		if (StringUtils.trim(roleDetial.getName()).length() > 64) {
			return "角色名称过长！";
		}
		List<RoleDTO> cmsRoleList = roleService.findAllCmsRole(500, 0);
		if (!CollectionUtils.isEmpty(cmsRoleList)) {
			for (RoleDTO existingRole : cmsRoleList) {
				// 如果新增或者更新的用户组与已存在某的用户组名称相同并且id不同，将不能更新
				if (StringUtils.isNotBlank(existingRole.getDisplayName())
						&& existingRole.getDisplayName().equalsIgnoreCase(StringUtils.trim(roleDetial.getName()))
						&& existingRole.getId() != roleDetial.getId()) {
					return "该角色名称已存在！";
				}
			}
		}
		return null;
	}

	/**
	 * 递归移除指定角色的子角色的指定权限。
	 * 
	 * @param id
	 *            指定角色ID
	 * @param orgPermission
	 *            被移除的权限ID
	 */
	private void removeChildrenPermission(long id, String orgPermission) {
		List<RoleDTO> childrenRoleList = roleService.findCmsRoleChildren(id);
		if (CollectionUtils.isEmpty(childrenRoleList)) {
			return;
		}
		for (RoleDTO roleDTO : childrenRoleList) {
			removeRolePermission(roleDTO, orgPermission);
			removeChildrenPermission(roleDTO.getId(), orgPermission);
		}
	}

	/**
	 * 移除指定角色的某个权限。
	 * 
	 * @param roleDTO
	 *            指定角色ID
	 * @param orgPermission
	 *            被移除的权限ID
	 */
	private void removeRolePermission(RoleDTO roleDTO, String orgPermission) {
		if (roleDTO != null && !StringUtils.isEmpty(roleDTO.getPermissions())) {
			// TODO:decrypt permissions string
			String[] perArr = roleDTO.getPermissions().split(",");
			List<PermissionDTO> newPermissionList = new ArrayList<>();
			boolean hasOrgPermission = false;
			for (String permission : perArr) {
				if (permission.equals(orgPermission)) {
					hasOrgPermission = true;
				} else {
					PermissionDTO dto = new PermissionDTO();
					dto.setId(Long.parseLong(permission));
					newPermissionList.add(dto);
				}
			}
			if (hasOrgPermission) {
				roleDTO.setPermissionList(newPermissionList);
				roleService.upsertRole(roleDTO);
			}
		}
	}

	/**
	 * 判断指定用户在指定的角色下是否有权分配指定的权限列表。
	 * 
	 * @param userId
	 *            用户ID
	 * @param parentId
	 *            指定的角色ID
	 * @param accessList
	 *            将被分配的权限列表数据
	 * @return 如果有权分配指定权限，返回true，否则返回false
	 */
	private boolean hasAuthorityToAssignPermission(long userId, long parentId, List<AgentPermissionVO> accessList) {
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可任意分配任意权限
		if (currentUser.getAgentType() == AgentType.ROOT) {
			return true;
		}
		List<RoleDTO> userRoleList = roleService.findCmsUserRole(userId);
		for (RoleDTO roleDTO : userRoleList) {
			// 检查所选的parent角色是否为该用户的角色
			if (parentId == roleDTO.getId()) {
				List<PermissionDTO> userPermissionList = permissionService.findPermissionsByIdsAndCategory(
						roleDTO.getPermissions(), AuthzCategory.CMS.getIntValue());
				assignList: for (AgentPermissionVO permissionVO : accessList) {
					for (PermissionDTO permissionDTO : userPermissionList) {
						if (permissionDTO.getId() == permissionVO.getId()) {
							continue assignList;
						}
					}
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#lockAgentAccount(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO lockAgentAccount(long userId, long id) {
		return updateAgentAccountStatus(userId, id, AccountStatus.LOCKED);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#unlockAgentAccount(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO unlockAgentAccount(long userId, long id) {
		return updateAgentAccountStatus(userId, id, AccountStatus.NORMAL);
	}

	/**
	 * 由指定用户更新另一用户的指定状态。
	 * 
	 * @param id
	 *            被更新用户ID
	 * @param userId
	 *            更新操作用户ID
	 * @param status
	 *            更新后的账户状态
	 * @return
	 */
	private BaseJsonVO updateAgentAccountStatus(long userId, long id, AccountStatus status) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		// 如果是ROOT用户，可修改任意账号的状态。
		if (currentUser.getAgentType() == AgentType.ROOT) {
			agentService.updateAgentAccountStatus(userId, id, status);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
			return baseJsonVO;
		}
		AgentDTO agentDTO = agentService.findAgentWithRoleById(id);
		if (agentDTO != null && !CollectionUtils.isEmpty(agentDTO.getRoleList())) {
			for (RoleDTO role : agentDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					agentService.updateAgentAccountStatus(userId, id, status);
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to update account {}!", currentUser.getName(),
					agentDTO.getName());
			baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无更新指定账号的权限！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getAdminRole(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO getAdminRole(long userId, long id) {
		// ##如果所选admin是ROOT用户，可以分配任何角色；否则将需要过滤带authority的角色##
		// 如果所选admin是root或者管理员，可以分配自身管理的角色，否则为普通用户，无任何角色可分配
		return getRole(userId, 500, 0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getAdminList(long)
	 */
	@Override
	public BaseJsonVO getAdminList(long userId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		List<AgentDTO> adminList = new ArrayList<>();
		// 如果是ROOT用户，可以获取所有admin和ROOT账号，否则只会返回当前用户
		if (currentUser.getAgentType() == AgentType.ROOT) {
			List<AgentDTO> adminAgentList = agentService.findAgentByAgentType(AgentType.ADMIN);
			if (!CollectionUtils.isEmpty(adminAgentList)) {
				adminList.addAll(adminList);
			}
			adminList.addAll(agentService.findAgentByAgentType(AgentType.ROOT));
		} else {
			adminList.add(currentUser);
		}
		if (CollectionUtils.isEmpty(adminList)) {
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			List<AgentVO> agentVOList = new ArrayList<>();
			for (AgentDTO agentDTO : adminList) {
				agentVOList.add(buildAgentAccountDetial(agentDTO));
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(agentVOList);
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#findUserRole(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO findUserRole(long userId, long parentId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		List<RoleDTO> roleList = roleService.findCmsUserRole(userId);
		if (CollectionUtils.isEmpty(roleList)) {
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			List<CmsRoleVO> roleVOList = new ArrayList<>();
			// 判断父角色是否提供，并且是否为cms的角色
			// 如果用户是root，可以使用自己的的角色或者父角色进行修改
			boolean needCheckRole = false;
			RoleDTO parentRole = null;
			if (currentUser.getAgentType() == AgentType.ROOT && parentId != 0) {
				parentRole = roleService.findRoleById(parentId);
				if (null != parentRole && parentRole.getCategory() == AuthzCategory.CMS) {
					needCheckRole = true;
				}
			}
			boolean hasAddedParent = false;
			for (RoleDTO roleDTO : roleList) {
				if (needCheckRole && !hasAddedParent) {
					hasAddedParent = roleDTO.getId() == parentId;
				}
				roleVOList.add(new CmsRoleVO(roleDTO));
			}
			if (!hasAddedParent && null != parentRole) {
				roleVOList.add(0, new CmsRoleVO(parentRole));
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(roleVOList);
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#getSiteList(long,
	 *      java.lang.String)
	 */
	@Override
	public BaseJsonVO getSiteList(long userId, String permission) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO curAgent = agentService.findAgentById(userId);
		if (curAgent == null || curAgent.getAccountStatus() != AccountStatus.NORMAL) {
			LOGGER.error("Agent is unavailable! UserId {}.", userId);
			return null;
		}
		List<SiteCMSDTO> dtoList = null;
		// 超级管理员可以获取站点下全部区域
		if (curAgent.getAgentType() == AgentType.ROOT) {
			dtoList = siteCMSService.getAllSiteCMSList();
		} else {
			dtoList = siteCMSService.getSiteCMSList(
					agentService.findAgentSiteIdsByPermission(userId, permission));
		}
		baseJsonVO.setResult(new BaseJsonListResultVO(convertTOSiteVO(dtoList)));
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}
	
	private List<SiteCMSVO> convertTOSiteVO(List<SiteCMSDTO> dtoList) {
		List<SiteCMSVO> retList = null;
		if (CollectionUtils.isNotEmpty(dtoList)) {
			retList = new ArrayList<SiteCMSVO>(dtoList.size());
			for (SiteCMSDTO siteCMSDTO : dtoList) {
				retList.add(new SiteCMSVO(siteCMSDTO));
			}
		}
		return retList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.AuthorityFacade#checkUserAuthorityOfSite(java.lang.String,
	 *      java.util.List, java.util.List)
	 */
	@Override
	public boolean checkUserAuthorityOfSite(String userName, List<String> permissionList, List<Long> siteList) {
		// 1. 检查传入的参数是否有效。
		if (StringUtils.isBlank(userName) || CollectionUtils.isEmpty(permissionList)
				|| CollectionUtils.isEmpty(siteList)) {
			// 参数非全部有效，返回false
			return false;
		}
		// 2. 检查用户名是否有效，并获取用户ID
		AgentDTO agentUser = agentService.findAgentByName(userName);
		if (agentUser == null) {
			// 账号不存在，返回false
			return false;
		}
		// 3.依次检查是否有指定权限和是否有指定站点
		for (String permission : permissionList) {
			List<Long> userSiteList = agentService.findAgentSiteIdsByPermission(agentUser.getId(), permission);
			// 检查查找到的站点是否为空，或者是否包含指定的站点
			if (CollectionUtils.isEmpty(userSiteList) || !userSiteList.containsAll(siteList)) {
				// 站点为空或者不全部包含指定站点，继续检查其他权限
				continue;
			}
			// 站点非空，并且包含指定的站点，返回true
			return true;
		}
		// 4. 所有权限检查完毕后没有符合条件的记录，返回false
		return false;
	}

	@Override
	public boolean isContainPermission(long userId, String permission) {
		return agentService.isContainPermission(userId, permission);
	}

	@Override
	public List<SiteAreaVO> getAreaList(long agentId, long siteId, long userId) {
		AgentDTO curAgent = agentService.findAgentById(agentId);
		if (curAgent == null || curAgent.getAccountStatus() != AccountStatus.NORMAL) {
			LOGGER.error("Agent is unavailable! UserId {}.", agentId);
			return null;
		}
		// 用户areaId列表
		List<Long> userAreaIds = null;
		if (userId > 0l) {
			List<AgentAreaDTO> userAreaList = agentService.getAgentAreaList(userId, siteId);
			if (CollectionUtils.isNotEmpty(userAreaList)) {
				userAreaIds = new ArrayList<Long>(userAreaList.size());
				for (AgentAreaDTO agentArea : userAreaList) {
					userAreaIds.add(agentArea.getAreaId());
				}
			}
		}
		// 超级管理员可以获取站点下全部区域
		if (curAgent.getAgentType() == AgentType.ROOT) {
			List<SiteAreaDTO> siteAreaList = null;
			if (siteId > 0l) {
				siteAreaList = siteCMSService.getSiteAreaList(siteId);
			} else {
				siteAreaList = siteCMSService.getSiteAreasList(null);
			}
			if (CollectionUtils.isEmpty(siteAreaList)) {
				return null;
			}
			List<SiteAreaVO> retList = new ArrayList<SiteAreaVO>(siteAreaList.size());
			for (SiteAreaDTO siteArea : siteAreaList) {
				SiteAreaVO area = new SiteAreaVO();
				area.setAreaId(siteArea.getAreaId());
				area.setAreaName(locationService.getLocationNameByCode(siteArea.getAreaId(), false));
				if (userAreaIds != null && userAreaIds.contains(siteArea.getAreaId())) {
					area.setIsChecked(1);
				}
				retList.add(area);
			}
			return retList;
		} else {
			List<AgentAreaDTO> agentAreaList = agentService.getAgentAreaList(agentId, siteId);
			if (CollectionUtils.isEmpty(agentAreaList)) {
				return null;
			}
			List<SiteAreaVO> retList = new ArrayList<SiteAreaVO>(agentAreaList.size());
			for (AgentAreaDTO agentArea : agentAreaList) {
				SiteAreaVO area = new SiteAreaVO();
				area.setAreaId(agentArea.getAreaId());
				area.setAreaName(locationService.getLocationNameByCode(agentArea.getAreaId(), false));
				if (userAreaIds != null && userAreaIds.contains(agentArea.getAreaId())) {
					area.setIsChecked(1);
				}
				retList.add(area);
			}
			return retList;
		}
	}

	@Override
	public AgentDTO getCurrentAgent() {
		return agentService.findAgentById(SecurityContextUtils.getUserId());
	}

	@Override
	public List<Long> getAreaIdList(long userId, long siteId) {
		AgentDTO curAgent = agentService.findAgentById(userId);
		if (curAgent == null || curAgent.getAccountStatus() != AccountStatus.NORMAL) {
			LOGGER.error("Agent is unavailable! UserId {}.", userId);
			return null;
		}
		// 超级管理员可以获取站点下全部区域
		if (curAgent.getAgentType() == AgentType.ROOT) {
			List<SiteAreaDTO> siteAreaList = siteCMSService.getSiteAreaList(siteId);
			if (CollectionUtils.isEmpty(siteAreaList)) {
				return null;
			}
			List<Long> retList = new ArrayList<Long>(siteAreaList.size());
			for (SiteAreaDTO siteArea : siteAreaList) {
				retList.add(siteArea.getAreaId());
			}
			return retList;
		} else {
			List<AgentAreaDTO> agentAreaList = agentService.getAgentAreaList(userId, siteId);
			if (CollectionUtils.isEmpty(agentAreaList)) {
				return null;
			}
			List<Long> retList = new ArrayList<Long>(agentAreaList.size());
			for (AgentAreaDTO agentArea : agentAreaList) {
				retList.add(agentArea.getAreaId());
			}
			return retList;
		}
	}

	@Override
	@Transaction
	public BaseJsonVO deleteAgentAccounts(long userId, List<Long> idList) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		AgentDTO currentUser = agentService.findAgentById(userId);
		if (currentUser == null || currentUser.getAccountStatus() != AccountStatus.NORMAL) {
			LOGGER.error("Agent is unavailable! UserId {}.", userId);
			baseJsonVO.setCodeAndMessage(ResponseCode.RES_ERROR, "fail!");
			return baseJsonVO;
		}
		// 如果是ROOT用户，可修改删除账号。
		for (Long id : idList) {
			AgentDTO agentDTO = agentService.findAgentWithRoleById(id);
			if (currentUser.getAgentType() == AgentType.ROOT) {
				agentService.deleteAgentById(id, agentDTO.getName());
			} else {
				if (agentDTO != null && !CollectionUtils.isEmpty(agentDTO.getRoleList())) {
					for (RoleDTO role : agentDTO.getRoleList()) {
						if (role.getOwnerId() == userId) {
							agentService.deleteAgentById(id, agentDTO.getName());
						}
					}
				} else {
					LOGGER.error("Current user {} dosen't have authority to delete account {}!", currentUser.getName(),
							agentDTO.getName());
					baseJsonVO.setCode(ErrorCode.CMS_AUTHORITY_MANAGEMENT_ERROR);
					baseJsonVO.setMessage("当前用户无删除指定账号的权限！");
					return baseJsonVO;
				}
			}
		}
		LOGGER.info("Delete Agent Success! UserIds : {}, AgentId : {}", idList.toString(), userId);
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}
}
