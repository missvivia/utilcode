/**
 * 
 */
package com.xyl.mmall.backend.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.AuthorityFacade;
import com.xyl.mmall.backend.vo.BackendRoleVO;
import com.xyl.mmall.backend.vo.DealerPermissionVO;
import com.xyl.mmall.backend.vo.DealerVO;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.vo.BaseJsonListResultVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.enums.DealerType;
import com.xyl.mmall.member.service.AccountService;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.PermissionService;
import com.xyl.mmall.member.service.RoleService;

/**
 * @author lihui
 *
 */
@Facade("backendAuthorityFacade")
public class AuthorityFacadeImpl implements AuthorityFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityFacadeImpl.class);

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Resource
	private DealerService dealerService;

	@Resource
	private RoleService roleService;

	@Resource
	private PermissionService permissionService;
	
	@Resource
	private AccountService accountService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#getUserGroup(long,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO getUserGroup(long userId, int limit, int offset) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		DealerDTO currentUser = dealerService.findDealerById(userId);
		List<RoleDTO> roleList = roleService.findAllBackendRoleOwnerId(userId, limit, offset);
		if (CollectionUtils.isEmpty(roleList)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dealer (id = {}) has no user group yet.", userId);
			}
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			List<BackendRoleVO> roleVOList = new ArrayList<>();
			for (RoleDTO roleDTO : roleList) {
				roleDTO.setOwnerName(currentUser.getName());
				roleVOList.add(new BackendRoleVO(roleDTO));
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(roleVOList);
			listResult.setTotal(roleService.countAllBackendRoleByOwnerId(userId));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dealer (id = {}) has {} user group.", userId, listResult.getTotal());
			}
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#getAccessList(long)
	 */
	@Override
	public BaseJsonVO getAccessList(long userId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		List<PermissionDTO> permissionList = permissionService.findDealerPermissionsByDealerId(userId, true);
		if (CollectionUtils.isEmpty(permissionList)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Dealer (id = {}) has no permission.", userId);
			}
			return baseJsonVO;
		}
		List<Long> parentIds = new ArrayList<>();
		List<DealerPermissionVO> permissionVOList = new ArrayList<>();
		Map<Long, List<DealerPermissionVO>> childrenPermissionMap = new HashMap<>();
		for (PermissionDTO permissionDTO : permissionList) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dealer (id = {}) has permission:{}.", userId, permissionDTO.getName());
			}
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
				childrenPermissionMap.get(permissionDTO.getParentId()).add(new DealerPermissionVO(permissionDTO));
			} else {
				List<DealerPermissionVO> childrenList = new ArrayList<>();
				childrenList.add(new DealerPermissionVO(permissionDTO));
				childrenPermissionMap.put(permissionDTO.getParentId(), childrenList);
			}
		}
		List<PermissionDTO> parentDTOList = permissionService.findPermissionsIncludeParentByIdListAndCategory(
				parentIds, AuthzCategory.VIS.getIntValue());
		for (PermissionDTO parentDTO : parentDTOList) {
			DealerPermissionVO parentVO = new DealerPermissionVO(parentDTO);
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
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#saveUserGroup(com.xyl.mmall.backend.vo.BackendRoleVO,
	 *      long)
	 */
	@Override
	public BaseJsonVO saveUserGroup(BackendRoleVO roleDetial, long userId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		String validateResult = validateBackendRole(roleDetial, userId);
		if (StringUtils.isNotBlank(validateResult)) {
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage(validateResult);
			return baseJsonVO;
		}
		if (hasAuthorityToAssignPermission(userId, roleDetial.getAccessList())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dealer (id = {}) has authority to assgin permissions to {}.", userId,
						roleDetial.getName());
			}
			RoleDTO roleDTO = new RoleDTO();
			if (roleDetial.getId() != null && roleDetial.getId() != 0) {
				roleDTO.setId(roleDetial.getId());
			} else {
				roleDTO.setCreateTime(System.currentTimeMillis());
			}
			roleDTO.setOwnerId(userId);
			roleDTO.setCategory(AuthzCategory.VIS);
			roleDTO.setDisplayName(StringUtils.trim(roleDetial.getName()));
			roleDTO.setLastModifiedBy(userId);
			roleDTO.setLastModifiedTime(System.currentTimeMillis());
			RoleDTO adminRole = roleService.findBackendAdminRole();
			if (adminRole == null) {
				// Should never happen because dealer admin role is always
				// required.
				LOGGER.error("Dealer admin role is not exsting!");
				baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
				baseJsonVO.setMessage("商家管理员用户组不存在!");
				return baseJsonVO;
			}
			roleDTO.setParentId(adminRole.getId());
			List<PermissionDTO> permissions = new ArrayList<>();
			for (DealerPermissionVO permissionVO : roleDetial.getAccessList()) {
				permissions.add(permissionVO.getPermission());
			}
			roleDTO.setPermissionList(permissions);
			RoleDTO newRoleDTO = roleService.upsertRole(roleDTO);
			// 返回更新后的角色组数据
			BackendRoleVO dealerRoleVO = new BackendRoleVO(newRoleDTO);
			if (!CollectionUtils.isEmpty(newRoleDTO.getPermissionList())) {
				List<DealerPermissionVO> permissionVOList = new ArrayList<>();
				for (PermissionDTO permissionDTO : newRoleDTO.getPermissionList()) {
					permissionVOList.add(new DealerPermissionVO(permissionDTO));
				}
				dealerRoleVO.setAccessList(permissionVOList);
			}
			baseJsonVO.setResult(dealerRoleVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		} else {
			LOGGER.error("Dealer (id = {}) dosen't have authority to assgin permissions to {}.", userId,
					roleDetial.getName());
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("权限赋予失败，超出该账号权限的范围！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * 校验用户组信息
	 * 
	 * @param roleDetial
	 * @param userId
	 */
	private String validateBackendRole(BackendRoleVO roleDetial, long userId) {
		if (StringUtils.isBlank(roleDetial.getName())) {
			return "用户组名称不能为空！";
		}
		if (StringUtils.trim(roleDetial.getName()).length() > 64) {
			return "用户组名称过长！";
		}
		List<RoleDTO> cmsRoleList = roleService.findAllBackendRoleOwnerId(userId, 500, 0);
		if (!CollectionUtils.isEmpty(cmsRoleList)) {
			for (RoleDTO existingRole : cmsRoleList) {
				// 如果新增或者更新的用户组与已存在某的用户组名称相同并且id不同，将不能更新
				if (StringUtils.isNotBlank(existingRole.getDisplayName())
						&& existingRole.getDisplayName().equalsIgnoreCase(StringUtils.trim(roleDetial.getName()))
						&& existingRole.getId() != roleDetial.getId()) {
					return "该用户组名称已存在！";
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定用户是否有权限分配指定的后台权限。
	 * 
	 * @param userId
	 *            用户ID
	 * @param list
	 *            将被分配的权限列表
	 * @return 如果有分配的权限，返回true，否则返回false。
	 */
	private boolean hasAuthorityToAssignPermission(long userId, List<DealerPermissionVO> list) {
		List<PermissionDTO> userPermissionList = permissionService.findDealerPermissionsByDealerId(userId, true);
		assignList: for (DealerPermissionVO permissionVO : list) {
			for (PermissionDTO permissionDTO : userPermissionList) {
				if (permissionDTO.getId() == permissionVO.getId()) {
					continue assignList;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#findUserGroup(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO findUserGroup(long userId, long groupId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		RoleDTO roleDTO = roleService.findRoleById(groupId);
		if (roleDTO != null && roleDTO.getOwnerId() == userId) {
			BackendRoleVO dealerRoleVO = new BackendRoleVO(roleDTO);
			if (!CollectionUtils.isEmpty(roleDTO.getPermissionList())) {
				List<DealerPermissionVO> permissionVOList = new ArrayList<>();
				for (PermissionDTO permissionDTO : roleDTO.getPermissionList()) {
					permissionVOList.add(new DealerPermissionVO(permissionDTO));
				}
				dealerRoleVO.setAccessList(permissionVOList);
			}
			baseJsonVO.setResult(dealerRoleVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#deleteUserGroup(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO deleteUserGroup(long userId, long groupId) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		RoleDTO roleDTO = roleService.findRoleById(groupId);
		if (roleDTO != null && roleDTO.getOwnerId() == userId) {
			roleService.deleteRoleById(groupId);
			dealerService.deleteDealerRoleByRoleId(groupId);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#getUserList(long,
	 *      int, int)
	 */
	@Override
	public BaseJsonVO getUserList(long userId, int limit, int offset) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		List<DealerDTO> dealerList = dealerService.findDealerByRoleOwner(userId, limit, offset);
		if (CollectionUtils.isEmpty(dealerList)) {
			baseJsonVO.setResult(new BaseJsonListResultVO());
		} else {
			List<DealerVO> dealerVOList = new ArrayList<>();
			for (DealerDTO dealerDTO : dealerList) {
				DealerVO dealerVO = new DealerVO(dealerDTO);
				if (!CollectionUtils.isEmpty(dealerDTO.getRoleList())) {
					List<BackendRoleVO> groupList = new ArrayList<>();
					for (RoleDTO roleDTO : dealerDTO.getRoleList()) {
						groupList.add(new BackendRoleVO(roleDTO));
					}
					dealerVO.setGroupList(groupList);
				}
				dealerVOList.add(dealerVO);
			}
			BaseJsonListResultVO listResult = new BaseJsonListResultVO(dealerVOList);
			listResult.setTotal(dealerService.countDealerByRoleOwner(userId));
			baseJsonVO.setResult(listResult);
		}
		baseJsonVO.setCode(ErrorCode.SUCCESS);
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#saveDealerUser(com.xyl.mmall.backend.vo.DealerVO,
	 *      long)
	 */
	@Override
	public BaseJsonVO saveDealerUser(DealerVO dealer, long userId) {
		
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		String validateResult = validateDealer(dealer);
		if (StringUtils.isNotBlank(validateResult)) {
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage(validateResult);
			return baseJsonVO;
		}
		if (hasAuthorityToAssignGroup(userId, dealer.getGroupList())) {
			DealerDTO dealerDTO = new DealerDTO();
			dealerDTO.setId(dealer.getId());
			dealerDTO.setDepartment(StringUtils.trim(dealer.getDepartment()));
			dealerDTO.setEmpNumber(StringUtils.trim(dealer.getAccountNum()));
			dealerDTO.setMobile(StringUtils.trim(dealer.getMobile()));
			dealerDTO.setName(StringUtils.trim(dealer.getDisplayName()));
			dealerDTO.setRealName(StringUtils.trim(dealer.getName()));
			if (StringUtils.equalsIgnoreCase(dealer.getPasswordIsChange(), "Y")) {
				dealerDTO.setPasswordIsChange("Y");
				dealerDTO.setPassword(dealer.getPassword());
			}
			List<RoleDTO> roleList = new ArrayList<>();
			for (BackendRoleVO roleVO : dealer.getGroupList()) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(roleVO.getId());
				for (RoleDTO toAddRole : roleList) {
					if (toAddRole.getId() == roleDTO.getId()) {
						LOGGER.error("Assigning duplicate roles is not allowed!");
						baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
						baseJsonVO.setMessage("不允许添加重复的用户组！");
						return baseJsonVO;
					}
				}
				roleList.add(roleDTO);
			}
			dealerDTO.setRoleList(roleList);
			
			DealerDTO newDTO = dealerService.upsertDealerEmployee(dealerDTO, userId);
			if (newDTO == null) {
				LOGGER.error("Failed to upsert dealer {}", dealerDTO.getName());
				accountService.deleteAccountByUserName(dealer.getDisplayName());
				baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
				baseJsonVO.setMessage("保存用户账号失败，该账号用户已存在！");
				return baseJsonVO;
			}
			// 返回更新后的用户信息
			DealerVO dealerVO = new DealerVO(newDTO);
			List<BackendRoleVO> roleVOList = new ArrayList<>();
			for (RoleDTO roleDTO : newDTO.getRoleList()) {
				roleVOList.add(new BackendRoleVO(roleDTO));
			}
			dealerVO.setGroupList(roleVOList);
			baseJsonVO.setResult(dealerVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
		} else {
			LOGGER.error("Unable to assgin usergroup of dealer {}", dealer.getDisplayName());
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("用户组分配失败！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * 校验传入的参数
	 * 
	 * @param dealer
	 */
	private String validateDealer(DealerVO dealer) {
		if (StringUtils.isBlank(dealer.getDisplayName())) {
			return "员工登录名不能为空！";
		}
		if (StringUtils.trim(dealer.getDisplayName()).length() > 128) {
			return "员工登录名过长！";
		}
//		if (!Pattern.matches(EMAIL_PATTERN, StringUtils.trim(dealer.getDisplayName()))) {
//			return "员工登录名必须是有效的电子邮件地址！";
//		}
		// 获取主账号前缀
		StringBuilder creator = new StringBuilder(SecurityContextUtils.getUserName());
		String main = creator.substring(0, creator.indexOf(MmallConstant.BUSINESS_ACCOUNT_SUFFIX));
		if (main.indexOf(".") > 0) {
			main = main.substring(0, main.indexOf("."));
		}
		if (StringUtils.indexOf(dealer.getDisplayName(), main) != 0) {
			return "请以主账号“" + main + "”开头！";
		}
		String dealerName = dealer.getDisplayName();
		// 是否以@st.xyl结尾
		if (StringUtils.endsWith(dealerName.toString(), MmallConstant.BUSINESS_ACCOUNT_SUFFIX)) {
			dealerName = dealerName.substring(0, dealerName.indexOf(MmallConstant.BUSINESS_ACCOUNT_SUFFIX));
		}
		// 检测子账号
		if (dealerName.indexOf(".") < 0 || dealerName.indexOf(".") == dealerName.length() - 1) {
			return "请添加子账号！";
		}
		if (!RegexUtils.isLetterOrNumber(dealerName.substring(dealerName.indexOf(".") + 1).toString())) {
			return "子账号只能是数字或者字母组合！";
		}
		// 重新拼接
		dealer.setDisplayName(dealerName + MmallConstant.BUSINESS_ACCOUNT_SUFFIX);
		if (StringUtils.isNotBlank(dealer.getMobile())
				&& !(StringUtils.trim(dealer.getMobile()).startsWith("1") && StringUtils.trim(dealer.getMobile())
						.length() == 11)) {
			return "无效的手机号码！";
		}
		if (StringUtils.isNotBlank(dealer.getDepartment()) && StringUtils.trim(dealer.getDepartment()).length() > 64) {
			return "用户所在部门名称过长！";
		}
		if (StringUtils.isNotBlank(dealer.getAccountNum()) && StringUtils.trim(dealer.getAccountNum()).length() > 64) {
			return "员工工号过长！";
		}
		if (StringUtils.isNotBlank(dealer.getName()) && StringUtils.trim(dealer.getName()).length() > 64) {
			return "员工姓名过长！";
		}
		if (CollectionUtils.isEmpty(dealer.getGroupList())) {
			return "请至少选择一个用户组！";
		}
		if (StringUtils.equalsIgnoreCase(dealer.getPasswordIsChange(), "Y")) {
			if (!RegexUtils.isValidPassword(dealer.getPassword())) {
				return "密码只能是6-20位字母或数字组合！";
			}
		}
		return null;
	}

	/**
	 * 判断指定用户是否有权限分配指定的用户组。
	 * 
	 * @param userId
	 *            用户ID
	 * @param groupList
	 *            将被分配的用户组列表
	 * @return 如果有权分配，返回true，否则返回false。
	 */
	private boolean hasAuthorityToAssignGroup(long userId, List<BackendRoleVO> groupList) {
		if (CollectionUtils.isEmpty(groupList)) {
			return false;
		}
		for (BackendRoleVO role : groupList) {
			RoleDTO roleDTO = roleService.findRoleById(role.getRole().getId());
			if (null == roleDTO || roleDTO.getOwnerId() != userId) {
				return false;
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#findDealerUser(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO findDealerUser(long userId, long id) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		DealerDTO currentDealer = dealerService.findDealerById(userId);
		DealerDTO dealerDTO = dealerService.findDealerWithRoleById(id);
		if (dealerDTO != null && !CollectionUtils.isEmpty(dealerDTO.getRoleList())) {
			for (RoleDTO role : dealerDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					DealerVO dealerVO = new DealerVO(dealerDTO);
					List<BackendRoleVO> roleVOList = new ArrayList<>();
					for (RoleDTO roleDTO : dealerDTO.getRoleList()) {
						roleVOList.add(new BackendRoleVO(roleDTO));
					}
					dealerVO.setGroupList(roleVOList);
					baseJsonVO.setResult(dealerVO);
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to view account {}!", userId, dealerDTO.getName());
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无查看指定账号的权限！");
			return baseJsonVO;
		} else if (DealerType.OWNER == currentDealer.getDealerType()
				&& currentDealer.getSupplierId() == dealerDTO.getSupplierId()) {
			DealerVO dealerVO = new DealerVO(dealerDTO);
			dealerVO.setGroupList(new ArrayList<BackendRoleVO>());
			baseJsonVO.setResult(dealerVO);
			baseJsonVO.setCode(ErrorCode.SUCCESS);
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#deleteDealerUser(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO deleteDealerUser(long userId, long id) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		DealerDTO dealerDTO = dealerService.findDealerWithRoleById(id);
		if (dealerDTO != null && !CollectionUtils.isEmpty(dealerDTO.getRoleList())) {
			for (RoleDTO role : dealerDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					dealerService.deleteDealerById(id);
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to delete account {}!", userId, dealerDTO.getName());
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无删除指定账号的权限！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#lockDealerUser(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO lockDealerUser(long userId, long id) {
		return updateDealerAccountStatus(userId, id, AccountStatus.LOCKED);
	}

	/**
	 * 由指定用户更新另一用户的指定状态。
	 * 
	 * @param id
	 *            被更新用户ID
	 * @param userId
	 *            更新操作用户ID
	 * @param status
	 *            更新后的状态
	 * @return
	 */
	private BaseJsonVO updateDealerAccountStatus(long userId, long id, AccountStatus status) {
		BaseJsonVO baseJsonVO = new BaseJsonVO();
		DealerDTO dealerDTO = dealerService.findDealerWithRoleById(id);
		if (dealerDTO != null && !CollectionUtils.isEmpty(dealerDTO.getRoleList())) {
			for (RoleDTO role : dealerDTO.getRoleList()) {
				if (role.getOwnerId() == userId) {
					dealerService.updateDealerAccountStatus(userId, id, status);
					baseJsonVO.setCode(ErrorCode.SUCCESS);
					return baseJsonVO;
				}
			}
			LOGGER.error("Current user {} dosen't have authority to update account {}!", userId, dealerDTO.getName());
			baseJsonVO.setCode(ErrorCode.BACKEND_AUTHORITY_MANAGEMENT_ERROR);
			baseJsonVO.setMessage("当前用户无删除更新账号的权限！");
			return baseJsonVO;
		}
		return baseJsonVO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.AuthorityFacade#unlockDealerUser(long,
	 *      long)
	 */
	@Override
	public BaseJsonVO unlockDealerUser(long userId, long id) {
		return updateDealerAccountStatus(userId, id, AccountStatus.NORMAL);
	}

}
