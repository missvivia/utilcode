/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;

import com.xyl.mmall.member.dto.AgentAreaDTO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.AgentRoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.param.AgentAccountSearchParam;

/**
 * 运维平台用户相关服务。
 * 
 * @author lihui
 *
 */
public interface AgentService {

	/**
	 * 根据运维用户Id获取用户信息。
	 * 
	 * @param id
	 *            用户ID
	 * @return 用户信息
	 */
	AgentDTO findAgentById(long id);

	/**
	 * 根据运维用户Id列表获取用户信息。
	 * 
	 * @param idList
	 *            用户ID列表
	 * @return 用户信息
	 */
	List<AgentDTO> findAgentByIdList(List<Long> idList);

	/**
	 * 根据运维用户名获取用户信息。
	 * 
	 * @param name
	 *            用户登录名
	 * @return 用户信息
	 */
	AgentDTO findAgentByName(String name);

	/**
	 * 创建/更新运维用户。
	 * 
	 * @param agentRole
	 *            运维用户信息
	 * @return
	 */
	AgentRoleDTO upsertAgentRole(AgentRoleDTO agentRole);

	/**
	 * 删除运维用户角色。
	 * 
	 * @param agentRole
	 *            运维用户角色
	 */
	void deleteAgentRole(AgentRoleDTO agentRole);

	/**
	 * 删除运维用户。
	 * 
	 * @param agentId
	 *            运维用户ID
	 * @param name 用户名
	 */
	void deleteAgentById(long agentId, String name);

	/**
	 * 创建/更新运维用户。
	 * 
	 * @param agentDTO
	 *            运维用户信息
	 * @param userId
	 *            操作人ID
	 * @return 创建/更新后的运维用户信息
	 */
	AgentDTO upsertAgent(AgentDTO agentDTO, long userId);

	/**
	 * 根据运维用户ID获取运维用户信息和角色信息。
	 * 
	 * @param id
	 *            运维用户ID
	 * @return 包含角色信息的用户信息
	 */
	AgentDTO findAgentWithRoleById(long id);

	/**
	 * 根据角色创建者获取拥有对应角色的运维用户信息的列表。
	 * 
	 * @param userId 角色创建人
	 * @param limit 分页大小
	 * @param offset 分页位置
	 * @param searchParam
	 * @return 运维用户信息的列表
	 */
	List<AgentDTO> findAgentByRoleOwner(long userId, int limit, int offset, AgentAccountSearchParam searchParam);

	/**
	 * 根据角色创建者获取拥有对应角色的运维用户的数量。
	 * 
	 * @param userId 角色创建人
	 * @param searchParam
	 * @return 运维用户的数量
	 */
	int countAgentByRoleOwner(long userId, AgentAccountSearchParam searchParam);

	/**
	 * 根据角色ID删除运维用户角色关系。
	 * 
	 * @param roleId
	 *            角色ID
	 */
	void deleteAgentRoleByRoleId(long roleId);

	/**
	 * 更新指定运维账号的状态。
	 * 
	 * @param userId
	 *            更新人ID
	 * @param id
	 *            账号ID
	 * @param status
	 *            更新后的状态
	 */
	void updateAgentAccountStatus(long userId, long id, AccountStatus status);

	/**
	 * 根据运维用户的类型获取运维用户信息的列表。
	 * 
	 * @param agentType
	 *            运维用户的类型
	 * @return 运维用户信息的列表
	 */
	List<AgentDTO> findAgentByAgentType(AgentType agentType);

	/**
	 * 根据运维用户的ID和对应的权限名查找可操作的站点ID列表。
	 * 
	 * @param permission
	 * @return
	 */
	List<Long> findAgentSiteIdsByPermission(long userId, String permission);

	/**
	 * 根据运维权限的名称列表获取运维用户信息的列表。
	 * 
	 * @param permissionList
	 *            权限的名称列表
	 * @return 运维用户信息的列表
	 */
	List<AgentDTO> findAgentByPermissionList(List<String> permissionList);

	/**
	 * 是否包含权限
	 * @param userId
	 * @param permission
	 * @return
	 */
	public boolean isContainPermission(long userId, String permission);

	/**
	 * 获取管理员可用区域
	 * @param userId
	 * @param siteId 0 取全部
	 * @return
	 */
	public List<AgentAreaDTO> getAgentAreaList(long userId, long siteId);
	
	/**
	 * 按agentid获取角色列表
	 * @param agentId
	 * @return
	 */
	public List<AgentRoleDTO> findByAgentId(long agentId);
	
	/**
	 * 根据站点区域Id获取agentId List
	 * @param siteId 
	 * @return
	 */
	List<Long> getAgentIdListByAreaIds(List<Long>areaIds);
}
