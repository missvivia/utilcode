/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;

/**
 * 用户权限相关服务。
 * 
 * @author lihui
 *
 */
public interface PermissionService {

	/**
	 * 根据运维用户登录名获取运维用户的权限列表， 默认包含权限管理。
	 * 
	 * @param agentName
	 *            运维用户登录名
	 * @return 运维用户的权限列表
	 */
	List<PermissionDTO> findAgentPermissionsByAgentName(String agentName);

	/**
	 * 根据运维用户ID获取运维用户的权限列表， 默认包含权限管理。
	 * 
	 * @param agentId
	 *            运维用户ID
	 * @return 运维用户的权限列表
	 */
	List<PermissionDTO> findAgentPermissionsByAgentId(long agentId);

	/**
	 * 根据运维用户ID获取运维用户的权限列表，并提供是否过滤权限管理的条件。
	 * 
	 * @param agentId
	 *            运维用户ID
	 * @param filteAuthority
	 *            是否过滤权限管理
	 * @return 运维用户的权限列表
	 */
	List<PermissionDTO> findAgentPermissionsByAgentId(long agentId, boolean filteAuthority);

	/**
	 * 根据指定的运维用户角色列表以及是否需要过滤权限管理权限，查找对应的权限列表
	 * 
	 * @param agentRoleList
	 *            角色列表
	 * @param filteAuthority
	 *            是否过滤权限管理权限
	 * @return 权限列表
	 */
	List<PermissionDTO> findPermissionsByAgentRoles(List<RoleDTO> agentRoleList, boolean filteAuthority);

	/**
	 * 获取所有运维平台的权限MAP。
	 * 
	 * @return 所有运维平台的权限MAP
	 */
	Map<Long, PermissionDTO> findAllCmsPermissions();

	/**
	 * 根据商家后台用户登录名获取用户的权限列表， 默认包含权限管理。
	 * 
	 * @param dealerName
	 *            商家后台用户登录名
	 * @return 用户的权限列表
	 */
	List<PermissionDTO> findDealerPermissionsByDealerName(String dealerName);

	/**
	 * 根据商家后台用户ID获取用户的权限列表， 默认包含权限管理。
	 * 
	 * @param dealerId
	 *            商家后台用户ID
	 * @return 用户的权限列表
	 */
	List<PermissionDTO> findDealerPermissionsByDealerId(long dealerId);

	/**
	 * 根据商家后台用户ID获取用户的权限列表，并提供是否过滤权限管理的条件。
	 * 
	 * @param dealerId
	 *            商家后台用户ID
	 * @param filteAuthority
	 *            是否过滤权限管理
	 * @return 用户的权限列表
	 */
	List<PermissionDTO> findDealerPermissionsByDealerId(long dealerId, boolean filteAuthority);

	/**
	 * 获取所有商家后台平台的权限MAP。
	 * 
	 * @return 所有商家后台平台的权限MAP
	 */
	Map<Long, PermissionDTO> findAllBackendPermissions();

	/**
	 * 根据权限的ID组和类别获取权限的列表。
	 * 
	 * @param ids
	 *            权限的ID组字符串，以逗号“,”分隔。
	 * @param category
	 *            权限的类别
	 * @return
	 */
	List<PermissionDTO> findPermissionsByIdsAndCategory(String ids, int category);

	/**
	 * 根据权限的ID组和类别获取权限的列表,包含父权限。
	 * 
	 * @param idList
	 *            权限的ID列表
	 * @param category
	 *            权限的类别
	 * @return
	 */
	List<PermissionDTO> findPermissionsIncludeParentByIdListAndCategory(List<Long> idList, int category);

	/**
	 * 根据权限列表构建权限ID组的字符串。
	 * 
	 * @param permissionList
	 *            权限列表
	 * @return 权限ID组的字符串，以逗号“,”分隔。
	 */
	String buildPermissionStr(List<PermissionDTO> permissionList);

	/**
	 * 获取运维平台的权限管理的权限。
	 * 
	 * @return 运维平台的权限管理的权限信息。
	 * @deprecated 该方法只返回权限管理权限的父节点，使用findAgentAuthorityPermissionList替换
	 */
	PermissionDTO findAgentAuthorityPermission();

	/**
	 * 获取运维平台的权限管理的权限。
	 * 
	 * @return 运维平台的权限管理的权限信息。
	 */
	List<PermissionDTO> findAgentAuthorityPermissionList();

	/**
	 * 根据权限的ID和类别获取权限。
	 * 
	 * @param id
	 *            权限的ID。
	 * @param category
	 *            权限的类表
	 * @return 权限信息
	 */
	PermissionDTO findPermissionsByIdAndCategory(long id, int category);

}
