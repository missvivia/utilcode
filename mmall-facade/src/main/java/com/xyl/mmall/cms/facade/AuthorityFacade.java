/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.cms.vo.CmsRoleVO;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.param.AgentAccountSearchParam;

/**
 * 权限管理相关facade。
 * 
 * @author lihui
 *
 */
public interface AuthorityFacade {

	/**
	 * 根据指定用户ID分页获取可查看的角色列表。
	 * 
	 * @param userId
	 *            用户ID
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 角色列表
	 */
	BaseJsonVO getRole(long userId, int limit, int offset);

	/**
	 * 根据指定的用户ID获取指定角色下可以分配的权限数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @param roleId
	 *            指定角色
	 * @return 权限树数据
	 */
	BaseJsonVO getAccessList(long userId, long roleId);

	/**
	 * 新建/更新运维平台用户。
	 * 
	 * @param agent 运维平台用户信息
	 * @param userId 操作人
	 * @return 更新后的用户信息
	 */
	public AgentVO saveAgentAccount(AgentDTO agentDTO, long userId);

	/**
	 * 根据指定的用户ID查找运维平台用户详情。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定运维平台用户ID
	 * @return 用户详情
	 */
	BaseJsonVO findAgentAccount(long userId, long id);

	/**
	 * 根据指定的用户ID删除运维平台用户
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            运维平台用户ID
	 * @return
	 */
	BaseJsonVO deleteAgentAccount(long userId, long id);

	/**
	 * 根据用户ID分页获取可查看的运维平台用户列表
	 * 
	 * @param userId 用户ID
	 * @param limit 分页大小
	 * @param offset 分页位置
	 * @param searchParam
	 * 
	 * @return 运维平台用户分页列表
	 */
	BaseJsonVO getAccountList(long userId, int limit, int offset, AgentAccountSearchParam searchParam);

	/**
	 * 根据用户ID删除指定角色。
	 * 
	 * @param userId
	 *            用户ID
	 * @param roleId
	 *            被删除的指定角色ID
	 * @return
	 */
	BaseJsonVO deleteRole(long userId, long roleId);

	/**
	 * 根据用户ID查找指定角色的详情。
	 * 
	 * @param userId
	 *            用户ID
	 * @param roleId
	 *            指定角色ID
	 * @return 角色详情
	 */
	BaseJsonVO findRole(long userId, long roleId);

	/**
	 * 创建/更新角色。
	 * 
	 * @param roleDetial
	 *            角色信息
	 * @param userId
	 *            操作人
	 * @return 更新后的角色详情
	 */
	BaseJsonVO saveRole(CmsRoleVO roleDetial, long userId);

	/**
	 * 根据用户ID锁定运维平台用户
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            运维平台用户ID
	 * @return
	 */
	BaseJsonVO lockAgentAccount(long userId, long id);

	/**
	 * 根据用户ID解锁运维平台用户
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            运维平台用户ID
	 * @return
	 */
	BaseJsonVO unlockAgentAccount(long userId, long id);

	/**
	 * 根据用户ID获取指定管理员的角色列表。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定管理员ID
	 * @return
	 */
	BaseJsonVO getAdminRole(long userId, long id);

	/**
	 * 根据用户ID获取可查看的管理员列表。
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	BaseJsonVO getAdminList(long userId);

	/**
	 * 根据用户ID获取已关联的角色列表。
	 * 
	 * @param userId
	 *            用户ID
	 * @param parentId
	 *            编辑角色时所选角色的父角色ID
	 * @return
	 */
	BaseJsonVO findUserRole(long userId, long parentId);

	/**
	 * @param userId
	 * @param permission
	 * @return
	 */
	BaseJsonVO getSiteList(long userId, String permission);

	/**
	 * 检查指定用户于指定站点是否拥有指定的权限。取权限的或关系。
	 * 
	 * @param userName
	 *            用户账号名
	 * @param permissionList
	 *            指定权限的列表
	 * @param siteList
	 *            指定站点的列表
	 * @return 检查的结果，true为有对应的站点权限，否则为false
	 */
	boolean checkUserAuthorityOfSite(String userName, List<String> permissionList, List<Long> siteList);

	/**
	 * 是否包含权限
	 * @param userId
	 * @param permission
	 * @return
	 */
	public boolean isContainPermission(long userId, String permission);
	
	/**
	 * 获取管理可用区域
	 * @param agentId 管理员id
	 * @param siteId 0 取全部
	 * @param userId 用户id
	 * @return
	 */
	public List<SiteAreaVO> getAreaList(long agentId, long siteId, long userId);
	
	/**
	 * 获取当前登录管理员
	 * @return
	 */
	public AgentDTO getCurrentAgent();

	/**
	 * 判断指定运维平台用户是否有权分配指定的角色列表。
	 * 
	 * @param currentUser 运维平台用户信息
	 * @param roleList 指定的角色列表
	 * @return 如果有权分配这些角色，返回true，否则返回false
	 */
	public boolean hasAuthorityToAssignRole(AgentDTO currentUser, List<CmsRoleVO> roleList);

	/**
	 * 判断是指定角色是否包含权限管理的权限。
	 * 
	 * @param id 角色ID
	 * @param authorityList 权限管理的权限信息
	 * @return 如果包含权限管理的权限，返回true，否则返回false
	 */
	public boolean hasAuthorityPermission(Long id, List<PermissionDTO> authorityList);
	
	/**
	 * 获取区域id列表
	 * @param userId
	 * @param siteId
	 * @return
	 */
	public List<Long> getAreaIdList(long userId, long siteId);
	
	/**
	 * 批量删除管理员账号
	 * @param userId
	 * @param idList
	 * @return
	 */
	public BaseJsonVO deleteAgentAccounts(long userId, List<Long> idList);
}
