/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.member.dao.RoleDao;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.meta.Role;
import com.xyl.mmall.member.service.PermissionService;
import com.xyl.mmall.member.service.RoleService;

/**
 * @author lihui
 *
 */
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionService permissionService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findAllCmsRoleByOwnerId(long,
	 *      int, int)
	 */
	@Override
	public List<RoleDTO> findAllCmsRoleByOwnerId(long ownerId, int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		param.setOrderColumn("lastModifiedTime");
		List<Role> roleList = roleDao.findByCategoryAndOwnerId(AuthzCategory.CMS.getIntValue(), ownerId, param);
		return buildRoleDTOList(roleList);
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
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findAllBackendRoleOwnerId(long,
	 *      int, int)
	 */
	@Override
	public List<RoleDTO> findAllBackendRoleOwnerId(long ownerId, int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		param.setOrderColumn("lastModifiedTime");
		List<Role> roleList = roleDao.findByCategoryAndOwnerId(AuthzCategory.VIS.getIntValue(), ownerId, param);
		return buildRoleDTOList(roleList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findRoleById(long)
	 */
	@Override
	@Cacheable(value = "roleOfId", key = "#roleId")
	public RoleDTO findRoleById(long roleId) {
		Role role = roleDao.getObjectById(roleId);
		if (role == null) {
			return null;
		}
		List<PermissionDTO> permissionList = permissionService.findPermissionsByIdsAndCategory(role.getPermissions(),
				role.getCategory().getIntValue());
		RoleDTO roleDTO = new RoleDTO(role);
		roleDTO.setPermissionList(permissionList);
		return roleDTO;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#upsertRole(com.xyl.mmall.member.dto.RoleDTO)
	 */
	@Override
	@CacheEvict(value = "roleOfId", key = "#roleDTO.id")
	public RoleDTO upsertRole(RoleDTO roleDTO) {
		roleDTO.setPermissions(permissionService.buildPermissionStr(roleDTO.getPermissionList()));
		if (roleDTO.getId() == 0) {
			roleDTO = addNewRole(roleDTO);
		} else {
			roleDTO = updateExistingRole(roleDTO);
		}
		return roleDTO;
	}

	/**
	 * @param role
	 * @return
	 */
	private RoleDTO updateExistingRole(RoleDTO roleDTO) {
		Role orgRole = roleDao.getObjectById(roleDTO.getId());
		if (null != orgRole) {
			roleDTO.setCreateTime(orgRole.getCreateTime());
			roleDao.updateObjectByKey(roleDTO);
		}
		return roleDTO;
	}

	/**
	 * @param role
	 * @return
	 */
	private RoleDTO addNewRole(RoleDTO roleDTO) {
		Role newRole = roleDao.addObject(roleDTO);
		return new RoleDTO(newRole);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#deleteRoleById(long)
	 */
	@Override
	@CacheEvict(value = "roleOfId", key = "#roleId")
	public void deleteRoleById(long roleId) {
		roleDao.deleteById(roleId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#countAllCmsRoleByOwnerId(long)
	 */
	@Override
	public int countAllCmsRoleByOwnerId(long ownerId) {
		return roleDao.countByCategoryAndOwnerId(AuthzCategory.CMS.getIntValue(), ownerId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#countAllBackendRoleByOwnerId(long)
	 */
	@Override
	public int countAllBackendRoleByOwnerId(long ownerId) {
		return roleDao.countByCategoryAndOwnerId(AuthzCategory.VIS.getIntValue(), ownerId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findAllCmsRole(int,
	 *      int)
	 */
	@Override
	public List<RoleDTO> findAllCmsRole(int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		param.setOrderColumn("lastModifiedTime");
		List<Role> roleList = roleDao.findByCategory(AuthzCategory.CMS.getIntValue(), param);
		return buildRoleDTOList(roleList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findAllBackend(int,
	 *      int)
	 */
	@Override
	public List<RoleDTO> findAllBackend(int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		param.setOrderColumn("lastModifiedTime");
		List<Role> roleList = roleDao.findByCategory(AuthzCategory.VIS.getIntValue(), param);
		return buildRoleDTOList(roleList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#countAllCmsRole()
	 */
	@Override
	public int countAllCmsRole() {
		return roleDao.countByCategory(AuthzCategory.CMS.getIntValue());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#countAllBackendRole()
	 */
	@Override
	public int countAllBackendRole() {
		return roleDao.countByCategory(AuthzCategory.VIS.getIntValue());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findBackendAdminRole()
	 */
	@Override
	public RoleDTO findBackendAdminRole() {
		Role adminRole = roleDao.getBackendAdmin();
		return new RoleDTO(adminRole);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findCmsAdminRole()
	 */
	@Override
	public RoleDTO findCmsAdminRole() {
		Role adminRole = roleDao.findByCategoryAndParentIdIsNull(AuthzCategory.CMS.getIntValue());
		return new RoleDTO(adminRole);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findCmsUserRole(long)
	 */
	@Override
	public List<RoleDTO> findCmsUserRole(long userId) {
		List<Role> roleList = roleDao.findByAgentId(userId);
		return buildRoleDTOList(roleList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.RoleService#findCmsRoleChildren(long)
	 */
	@Override
	public List<RoleDTO> findCmsRoleChildren(long id) {
		List<Role> roleList = roleDao.findByCategoryAndParentId(AuthzCategory.CMS.getIntValue(), id);
		return buildRoleDTOList(roleList);
	}

}
