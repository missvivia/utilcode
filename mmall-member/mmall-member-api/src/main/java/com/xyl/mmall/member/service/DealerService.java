/**
 * 
 */
package com.xyl.mmall.member.service;

import java.util.List;

import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.DealerRoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;

/**
 * 商家后台用户相关服务。
 * 
 * @author lihui
 *
 */
public interface DealerService {

	/**
	 * 为指定供应商关联新的用户账号。
	 * 
	 * @param name
	 *            被关联的账号名。
	 * @param supplierId
	 *            供应商ID
	 * @return 关联后的后台运维账号信息
	 */
	DealerDTO assignNewDealerOwner(String name, long supplierId);

	/**
	 * 停用指定的供应商的指定管理员账号。
	 * 
	 * @param name
	 *            被停用的管理员账号名
	 * @param supplierId
	 *            供应商ID
	 * @param userId
	 *            操作人
	 * @return
	 */
	void suspendDealerOwner(String name, long supplierId, long userId);

	/**
	 * 创建/更新供应商后台普通用户信息。
	 * 
	 * @param dealerDTO
	 *            后台用户信息
	 * @param userId
	 *            ‘操作人
	 * @return 创建/更新后的商家后台用户信息
	 */
	DealerDTO upsertDealerEmployee(DealerDTO dealerDTO, long userId);

	/**
	 * 根据用户ID查找商家后台用户信息。
	 * 
	 * @param id
	 *            商家后台用户ID
	 * @return 商家后台用户信息
	 */
	DealerDTO findDealerById(long id);

	/**
	 * 根据用户ID列表查找商家后台用户信息。
	 * 
	 * @param idList
	 *            商家后台用户ID列表
	 * @return 商家后台用户信息
	 */
	List<DealerDTO> findDealerById(List<Long> idList);

	/**
	 * 根据用户ID查找包含角色信息的商家后台用户信息。
	 * 
	 * @param 商家后台用户ID
	 * @return 商家后台用户信息
	 */
	DealerDTO findDealerWithRoleById(long id);

	/**
	 * 根据商家登录名查找商家后台用户信息。
	 * 
	 * @param name
	 *            商家后台用户登录名
	 * @return 商家后台用户信息
	 */
	DealerDTO findDealerByName(String name);

	/**
	 * 根据供应商ID查找所有该商家后台的普通用户
	 * 
	 * @param supplierId
	 *            供应商ID
	 * @return 普通用户的信息列表
	 */
	List<DealerDTO> findAllDealerEmployee(long supplierId);

	/**
	 * 创建/更新商家后台用户的用户组信息。
	 * 
	 * @param dealerRole
	 *            商家后台用户的用户组信息
	 * @return 创建/更新后的商家后台用户的用户组信息
	 */
	DealerRoleDTO upsertDealerRole(DealerRoleDTO dealerRole);

	/**
	 * 删除指定商家后台用户的用户组。
	 * 
	 * @param dealerRole
	 *            被删除的商家后台用户的用户组
	 */
	void deleteDealerRole(DealerRoleDTO dealerRole);

	/**
	 * 根据用户组创建人分页查找商家后台用户的信息列表
	 * 
	 * @param userId
	 *            创建人ID
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 商家后台用户的信息列表
	 */
	List<DealerDTO> findDealerByRoleOwner(long userId, int limit, int offset);

	/**
	 * 根据用户组创建人分页查找商家后台用户的数量
	 * 
	 * @param userId
	 *            创建人ID
	 * @return 商家后台用户的数量
	 */
	int countDealerByRoleOwner(long userId);

	/**
	 * 删除指定商家后台用户。
	 * 
	 * @param dealerId
	 *            被删除的商家后台用户ID
	 */
	void deleteDealerById(long dealerId);

	/**
	 * 根据用户组ID删除指定的商家后台用户组
	 * 
	 * @param roleId
	 *            被删除的商家后台用户组
	 */
	void deleteDealerRoleByRoleId(long roleId);

	/**
	 * 更新指定商家后台用户的状态。
	 * 
	 * @param userId
	 *            操作人
	 * @param id
	 *            指定更新的后台用户
	 * @param status
	 *            更新后的用户状态
	 */
	void updateDealerAccountStatus(long userId, long id, AccountStatus status);
	
	
	/**
	 * 删除相关商家后台用户信息，包含dealer和role表
	 * @return
	 */
	public void deleteDealerInfoByBusinessID(long businessId) throws ServiceException;

}
