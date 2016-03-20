/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.dao.PermissionDao;
import com.xyl.mmall.member.dao.RoleDao;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.meta.Permission;
import com.xyl.mmall.member.meta.Role;
import com.xyl.mmall.member.service.PermissionService;

/**
 * @author lihui
 *
 */
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private RoleDao roleDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAgentPermissionsByAgentName(java.lang.String)
	 */
	@Override
	public List<PermissionDTO> findAgentPermissionsByAgentName(String agentName) {
		return findPermissionsByAgentRoles(buildRoleDTOList(roleDao.findByAgentName(agentName)), false);
	}

	/**
	 * @param permissionList
	 * @return
	 */
	private Map<Long, PermissionDTO> buildPermissionDTOMap(List<Permission> permissionList) {
		Map<Long, PermissionDTO> permissionDTOMap = new HashMap<>();
		for (Permission permission : permissionList) {
			permissionDTOMap.put(permission.getId(), new PermissionDTO(permission));
		}
		return permissionDTOMap;
	}

	@Override
	public List<PermissionDTO> findPermissionsByAgentRoles(List<RoleDTO> agentRoleList, boolean filteAuthority) {
		if (CollectionUtils.isEmpty(agentRoleList)) {
			return null;
		}
		StringBuilder psb = new StringBuilder(512);
		for (Role role : agentRoleList) {
			if (StringUtils.isNotBlank(role.getPermissions())) {
				psb.append(role.getPermissions());
				psb.append(",");
			}
		}
		List<PermissionDTO> permissionList = findPermissionsByIdsAndCategory(psb.toString(),
				AuthzCategory.CMS.getIntValue());
		if (filteAuthority) {
			Permission authority = permissionDao.findByCategoryAndPermission(AuthzCategory.CMS.getIntValue(), "access");
			List<PermissionDTO> toBeRemoved = new ArrayList<>();
			for (PermissionDTO permission : permissionList) {
				if (isAuthorityPermission(authority, permission)) {
					toBeRemoved.add(permission);
				}
			}
			if (!CollectionUtils.isEmpty(toBeRemoved)) {
				permissionList.removeAll(toBeRemoved);
			}
		}
		return permissionList;
	}

	/**
	 * 检查permission是否为权限管理或其子权限。
	 * 
	 * @param authority
	 * @param permission
	 * @return
	 */
	private boolean isAuthorityPermission(Permission authority, PermissionDTO permission) {
		if (authority.getId() == permission.getId() || authority.getId() == permission.getParentId()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findPermissionsByIdsAndCategory(java.lang.String,
	 *      int)
	 */
	@Override
	public List<PermissionDTO> findPermissionsByIdsAndCategory(String ids, int category) {
		if (StringUtils.isEmpty(ids)) {
			return null;
		}
		// TODO:decrypt permissions string
		String[] idsArr = ids.split(",");
		List<String> idList = Arrays.asList(idsArr);
		Set<Long> idSet = new HashSet<>();
		for (String idStr : idList) {
			idSet.add(Long.parseLong(idStr));
		}
		return findPermissionsByIdSetAndCategory(idSet, category, false);
	}

	/**
	 * @param category
	 * @param idSet
	 * @param needParent
	 * @return
	 */
	private List<PermissionDTO> findPermissionsByIdSetAndCategory(Set<Long> idSet, int category, boolean needParent) {
		Map<Long, PermissionDTO> allPermissions = category == AuthzCategory.CMS.getIntValue() ? findAllCmsPermissions()
				: findAllBackendPermissions();
		List<PermissionDTO> permissions = new ArrayList<>();
		for (Long permId : idSet) {
			if (allPermissions.containsKey(permId) && (needParent || allPermissions.get(permId).getParentId() != 0L)) {
				permissions.add(ReflectUtil.cloneObj(allPermissions.get(permId)));
			}
		}
		Collections.sort(permissions, new Comparator<PermissionDTO>() {

			@Override
			public int compare(PermissionDTO o1, PermissionDTO o2) {
				return o1.getId() - o2.getId() > 0 ? 1 : -1;
			}

		});
		return permissions;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAllCmsPermissions()
	 */
	@Override
	public Map<Long, PermissionDTO> findAllCmsPermissions() {
		List<Permission> permissionList = permissionDao.findByCategory(AuthzCategory.CMS.getIntValue());
		if (CollectionUtils.isEmpty(permissionList)) {
			return null;
		}
		return buildPermissionDTOMap(permissionList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findDealerPermissionsByDealerName(java.lang.String)
	 */
	@Override
	public List<PermissionDTO> findDealerPermissionsByDealerName(String dealerName) {
		return findPermissionsByDealerRoles(roleDao.findByDealerName(dealerName), false);
	}

	/**
	 * 根据指定的后台用户角色列表以及是否需要过滤权限管理权限，查找对应的权限列表
	 * 
	 * @param delaerRoleList
	 *            角色列表
	 * @param includAuthority
	 *            是否需要过滤权限管理权限
	 * @return 权限列表
	 */
	private List<PermissionDTO> findPermissionsByDealerRoles(List<Role> delaerRoleList, boolean filteAuthority) {
		if (CollectionUtils.isEmpty(delaerRoleList)) {
			return null;
		}
		StringBuilder psb = new StringBuilder(512);
		for (Role role : delaerRoleList) {
			if (StringUtils.isNotBlank(role.getPermissions())) {
				psb.append(role.getPermissions());
				psb.append(",");
			}
		}
		List<PermissionDTO> permissionList = findPermissionsByIdsAndCategory(psb.toString(),
				AuthzCategory.VIS.getIntValue());
		// 如果需要，过滤权限管理的权限。
		if (filteAuthority) {
			Permission authority = permissionDao.findByCategoryAndPermission(AuthzCategory.VIS.getIntValue(),
					"authority");
			List<PermissionDTO> toBeRemoved = new ArrayList<>();
			for (PermissionDTO permission : permissionList) {
				if (isAuthorityPermission(authority, permission)) {
					toBeRemoved.add(permission);
				}
			}
			if (!CollectionUtils.isEmpty(toBeRemoved)) {
				permissionList.removeAll(toBeRemoved);
			}
		}
		return permissionList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAllBackendPermissions()
	 */
	@Override
	public Map<Long, PermissionDTO> findAllBackendPermissions() {
		List<Permission> permissionList = permissionDao.findByCategory(AuthzCategory.VIS.getIntValue());
		if (CollectionUtils.isEmpty(permissionList)) {
			return null;
		}
		return buildPermissionDTOMap(permissionList);
	}

	/**
	 * @param permissionList
	 * @return
	 */
	public String buildPermissionStr(List<PermissionDTO> permissionList) {
		StringBuilder sb = new StringBuilder(512);
		if (CollectionUtils.isEmpty(permissionList)) {
			return sb.toString();
		}
		for (PermissionDTO permissionDTO : permissionList) {
			sb.append(permissionDTO.getId());
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAgentPermissionsByAgentId(long)
	 */
	@Override
	public List<PermissionDTO> findAgentPermissionsByAgentId(long agentId) {
		return findAgentPermissionsByAgentId(agentId, false);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findDealerPermissionsByDealerId(long)
	 */
	@Override
	public List<PermissionDTO> findDealerPermissionsByDealerId(long dealerId) {
		return findDealerPermissionsByDealerId(dealerId, false);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAgentAuthorityPermission()
	 */
	@Override
	@Deprecated
	public PermissionDTO findAgentAuthorityPermission() {
		Permission authority = permissionDao.findByCategoryAndPermission(AuthzCategory.CMS.getIntValue(), "access");
		return new PermissionDTO(authority);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findDealerPermissionsByDealerId(long,
	 *      boolean)
	 */
	@Override
	public List<PermissionDTO> findDealerPermissionsByDealerId(long dealerId, boolean filteAuthority) {
		return findPermissionsByDealerRoles(roleDao.findByDealerId(dealerId), filteAuthority);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAgentPermissionsByAgentId(long,
	 *      boolean)
	 */
	@Override
	public List<PermissionDTO> findAgentPermissionsByAgentId(long agentId, boolean filteAuthority) {
		return findPermissionsByAgentRoles(buildRoleDTOList(roleDao.findByAgentId(agentId)), filteAuthority);
	}

	/**
	 * @param roleList
	 * @return
	 */
	private List<RoleDTO> buildRoleDTOList(List<Role> roleList) {
		if (CollectionUtils.isEmpty(roleList)) {
			return null;
		}
		List<RoleDTO> roleDTOList = new ArrayList<>();
		for (Role role : roleList) {
			roleDTOList.add(new RoleDTO(role));
		}
		return roleDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findPermissionsByIdAndCategory(long,
	 *      int)
	 */
	@Override
	public PermissionDTO findPermissionsByIdAndCategory(long id, int category) {
		Permission permission = permissionDao.getObjectById(id);
		if (permission == null || permission.getCategory().getIntValue() != category) {
			return null;
		}
		return new PermissionDTO(permission);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findPermissionsIncludeParentByIdListAndCategory(java.util.List,
	 *      int)
	 */
	@Override
	public List<PermissionDTO> findPermissionsIncludeParentByIdListAndCategory(List<Long> idList, int category) {
		return findPermissionsByIdSetAndCategory(new HashSet<>(idList), category, true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.PermissionService#findAgentAuthorityPermissionList()
	 */
	@Override
	public List<PermissionDTO> findAgentAuthorityPermissionList() {
		List<PermissionDTO> authorityPermissionList = new ArrayList<>();
		Permission authority = permissionDao.findByCategoryAndPermission(AuthzCategory.CMS.getIntValue(), "access");
		authorityPermissionList.add(new PermissionDTO(authority));
		List<Permission> permissionList = permissionDao.findByCategory(AuthzCategory.CMS.getIntValue());
		if (CollectionUtils.isEmpty(permissionList)) {
			return authorityPermissionList;
		}
		for (Permission permission : permissionList) {
			if (permission.getParentId() == authority.getId()) {
				authorityPermissionList.add(new PermissionDTO(permission));
			}
		}
		return authorityPermissionList;
	}

}
