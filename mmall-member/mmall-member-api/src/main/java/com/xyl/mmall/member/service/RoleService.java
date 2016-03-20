/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;

import com.xyl.mmall.member.dto.RoleDTO;

/**
 * 用户角色/用户组相关服务。
 * 
 * @author lihui
 *
 */
public interface RoleService {

	/**
	 * 根据角色创建人分页获取运维平台角色列表。
	 * 
	 * @param ownerId
	 *            角色创建人
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 运维平台角色列表
	 */
	List<RoleDTO> findAllCmsRoleByOwnerId(long ownerId, int limit, int offset);

	/**
	 * 根据用户组创建人分页获取商家后台用户组列表。
	 * 
	 * @param ownerId
	 *            用户组创建人
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 家后台用户组列表
	 */
	List<RoleDTO> findAllBackendRoleOwnerId(long ownerId, int limit, int offset);

	/**
	 * 根据角色创建人获取运维平台角色数量。
	 * 
	 * @param ownerId
	 *            角色创建人
	 * @return 运维平台角色数量
	 */
	int countAllCmsRoleByOwnerId(long ownerId);

	/**
	 * 根据用户组创建人获取商家后台用户组数量。
	 * 
	 * @param ownerId
	 *            角色创建人
	 * @return 商家后台用户组数量
	 */
	int countAllBackendRoleByOwnerId(long ownerId);

	/**
	 * 分页获取运维平台所有角色。
	 * 
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	List<RoleDTO> findAllCmsRole(int limit, int offset);

	/**
	 * 分页获取商家后台所有客户组。
	 * 
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return
	 */
	List<RoleDTO> findAllBackend(int limit, int offset);

	/**
	 * 获取运维平台所有角色数量.
	 * 
	 * @return 运维平台所有角色数量
	 */
	int countAllCmsRole();

	/**
	 * 获取商家后台所有客户组数量
	 * 
	 * @return 商家后台所有客户组数量
	 */
	int countAllBackendRole();

	/**
	 * 根据角色ID获取角色信息。
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 角色信息
	 */
	RoleDTO findRoleById(long roleId);

	/**
	 * 创建/更新角色信息。
	 * 
	 * @param roleDTO
	 *            角色信息
	 * @return 创建/更新后的角色信息
	 */
	RoleDTO upsertRole(RoleDTO roleDTO);

	/**
	 * 删除指定ID的角色。
	 * 
	 * @param roleId
	 *            被删除的角色
	 */
	void deleteRoleById(long roleId);

	/**
	 * 获取商家后台的管理员角色。
	 * 
	 * @return 商家后台的管理员角色信息
	 */
	RoleDTO findBackendAdminRole();

	/**
	 * 获取运维平台的管理员角色。
	 * 
	 * @return 运维平台的管理员角色
	 */
	RoleDTO findCmsAdminRole();

	/**
	 * 获取运维平台用户所拥有的角色
	 * 
	 * @param userId
	 * @return
	 */
	List<RoleDTO> findCmsUserRole(long userId);

	/**
	 * 获取指定角色的子角色。
	 * 
	 * @param id
	 *            父角色ID
	 * @return 所有子角色
	 */
	List<RoleDTO> findCmsRoleChildren(long id);

}
