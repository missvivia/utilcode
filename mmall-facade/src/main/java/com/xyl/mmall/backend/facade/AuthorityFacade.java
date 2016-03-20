/**
 * 
 */
package com.xyl.mmall.backend.facade;

import com.xyl.mmall.backend.vo.BackendRoleVO;
import com.xyl.mmall.backend.vo.DealerVO;
import com.xyl.mmall.framework.vo.BaseJsonVO;

/**
 * 权限管理相关facade。
 * 
 * @author lihui
 *
 */
public interface AuthorityFacade {

	/**
	 * 根据用户ID分页获取可查看的用户组数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 分页用户组
	 */
	BaseJsonVO getUserGroup(long userId, int limit, int offset);

	/**
	 * 根据用户ID获取可分配的权限列表数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 可分配的权限列表数据
	 */
	BaseJsonVO getAccessList(long userId);

	/**
	 * 创建/更新商家后台用户组。
	 * 
	 * @param roleDetial
	 *            用户组内容
	 * @param userId
	 *            操作人
	 * @return 更新后的用户组数据
	 */
	BaseJsonVO saveUserGroup(BackendRoleVO roleDetial, long userId);

	/**
	 * 根据用户ID获取指定用户组详情。
	 * 
	 * @param userId
	 *            用户ID
	 * @param groupId
	 *            指定用户组ID
	 * @return 用户组详情
	 */
	BaseJsonVO findUserGroup(long userId, long groupId);

	/**
	 * 根据用户ID删除指定用户组。
	 * 
	 * @param userId
	 *            用户ID
	 * @param groupId
	 *            指定用户组ID
	 * @return
	 */
	BaseJsonVO deleteUserGroup(long userId, long groupId);

	/**
	 * 根据用户ID分页获取可查看的用户列表数据。
	 * 
	 * @param userId
	 *            用户ID
	 * @param limit
	 *            分页大小
	 * @param offset
	 *            分页位置
	 * @return 分页用户列表
	 */
	BaseJsonVO getUserList(long userId, int limit, int offset);

	/**
	 * 根据用户ID创建/更新用户信息。
	 * 
	 * @param dealer
	 *            用户信息
	 * @param userId
	 *            用户ID
	 * @return 更新后的用户信息
	 */
	BaseJsonVO saveDealerUser(DealerVO dealer, long userId);

	/**
	 * 根据用户ID获取指定商家后台用户信息。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定商家后台用户ID
	 * @return 用户信息
	 */
	BaseJsonVO findDealerUser(long userId, long id);

	/**
	 * 根据用户ID删除指定商家后台用户。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定商家后台用户ID
	 * @return
	 */
	BaseJsonVO deleteDealerUser(long userId, long id);

	/**
	 * 根据用户ID锁定指定商家后台用户。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定商家后台用户ID
	 * @return
	 */
	BaseJsonVO lockDealerUser(long userId, long id);

	/**
	 * 根据用户ID解锁指定商家后台用户。
	 * 
	 * @param userId
	 *            用户ID
	 * @param id
	 *            指定商家后台用户ID
	 * @return
	 */
	BaseJsonVO unlockDealerUser(long userId, long id);

}
