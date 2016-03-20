/**
 * 
 */
package com.xyl.mmall.mainsite.facade;

import java.util.List;

import com.xyl.mmall.mainsite.vo.MainSiteFilterChainResourceVO;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;

/**
 * 主站系统用户、认证和权限相关facade。
 * 
 * @author lihui
 *
 */
public interface MainSiteAuthcFacade {

	/**
	 * 根据用户ID获取用户详情。
	 * 
	 * @param id
	 *            用户ID
	 * @return 用户详情
	 */
	MainSiteUserVO findUserByUserId(long id);
	
	/**
	 * 根据用户账号名获取用户详情。
	 * 
	 * @param name
	 *            用户账号名
	 * @return 用户详情
	 */
	MainSiteUserVO findUserByUserName(String name);

	/**
	 * 根据用户名更新并获取用户详情。如果为首次登入，则创建新的用户详情记录。
	 * 
	 * @param userName
	 *            用户账号名
	 * @param userName
	 *            用户昵称
	 * @return 用户详情
	 */
	MainSiteUserVO upsertUser(String userName, String nickName);

	/**
	 * 获取主站系统的权限过滤配置。
	 * 
	 * @return 权限过滤配置
	 */
	List<MainSiteFilterChainResourceVO> getMainSiteFilterChainResource();

	/**
	 * 更新主站用户的昵称。
	 * 
	 * @param userName
	 *            用户名
	 * @param nickName
	 *            用户昵称
	 * @return 用户昵称
	 */
	String updateNickName(String userName, String nickName);

}
