/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.AgentPermissionVO;
import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.cms.vo.CmsFilterChainResourceVO;

/**
 * 运维平台认证和权限相关facade。
 * 
 * @author lihui
 *
 */
public interface CmsAuthcFacade {

	/**
	 * 根据运维平台用户账号名获取用户信息。
	 * 
	 * @param name
	 *            用户账号名
	 * @return 用户信息
	 */
	AgentVO findAgentByName(String name);

	/**
	 * 根据运维平台用户ID获取用户信息。
	 * 
	 * @param id
	 *            用户ID
	 * @return 用户信息
	 */
	AgentVO findAgentById(long id);

	/**
	 * 获取运维平台系统的权限过滤配置
	 * 
	 * @return
	 */
	List<CmsFilterChainResourceVO> getCmsFilterChainResource();

	/**
	 * 根据运维平台用户账号名获取用户权限
	 * 
	 * @param userName
	 *            用户账号名
	 * @return 用户权限
	 */
	List<AgentPermissionVO> findAgentPermissionByName(String userName);

}
