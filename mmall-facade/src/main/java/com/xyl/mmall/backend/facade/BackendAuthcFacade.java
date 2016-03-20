/**
 * 
 */
package com.xyl.mmall.backend.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.backend.vo.BackendFilterChainResourceVO;
import com.xyl.mmall.backend.vo.DealerPermissionVO;
import com.xyl.mmall.backend.vo.DealerVO;

/**
 * 商家后台认证和权限相关facade。
 * 
 * @author lihui
 *
 */
public interface BackendAuthcFacade {

	/**
	 * 根据商家后台用户账号名获取用户信息。
	 * 
	 * @param name
	 *            用户账号名
	 * @return 用户信息
	 */
	DealerVO findDealerByName(String name);

	/**
	 * 根据商家后台用户ID获取用户信息。
	 * 
	 * @param id
	 *            用户ID
	 * @return 用户信息
	 */
	DealerVO findDealerById(long id);

	/**
	 * 根据商家后台用户帐号名获取用户权限列表。
	 * 
	 * @param name
	 *            商家后台用户帐号名
	 * @return 用户权限列表
	 */
	List<DealerPermissionVO> findDealerPermissionByName(String name);

	/**
	 * 获取商家后台系统的权限过滤配置。
	 * 
	 * @return 权限过滤配置内容
	 */
	List<BackendFilterChainResourceVO> getBackendFilterChainResource();

	/**
	 * 检查指定用户是否拥有查询数据分析功能的权限。
	 * 
	 * @param userName
	 *            用户名
	 * @param timestamp
	 *            请求时的时间
	 * @param key
	 *            加密token
	 * @return
	 */
	Map<String, Object> checkDataAuthority(String userName, long timestamp, String key);

}
