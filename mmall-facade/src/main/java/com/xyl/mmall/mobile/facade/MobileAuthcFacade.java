/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.mobile.facade.vo.MobileFilterChainResourceVO;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.mobile.web.vo.MainSiteUserVO;

/**
 * 手机后台系统用户、认证和权限相关facade。
 * 
 * @author lihui
 *
 */
public interface MobileAuthcFacade {

	/**
	 * 根据用户账号名获取用户详情。
	 * 
	 * @param userName
	 *            用户账号名
	 * @return 用户详情
	 */
	MobileUserVO findUserByUserName(String userName);

	/**
	 * 根据用户ID获取用户详情。
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户详情
	 */
	MobileUserVO findUserByUserId(long userId);

	/**
	 * 获取手机后台系统的权限过滤配置。
	 * 
	 * @return 权限过滤配置
	 */
	List<MobileFilterChainResourceVO> getMobileFilterChainResource();

	/**
	 * 根据手机的访问Token获取对应的登入用户。
	 * 
	 * @param token
	 *            访问Token
	 * @return 登入用户详情
	 */
	MobileUserVO findAuthencatedUser(String token);

	/**
	 * 根据手机的初始化ID获取已保存的手机登录信息。
	 * 
	 * @param id
	 *            手机的初始化ID
	 * @return 手机登录信息
	 */
	MobileInfoDTO findMobileInfoByInitId(String id);

	/**
	 * URS登录成功后创建/更新当前用户信息。
	 * 
	 * @param userName
	 *            登入用户名
	 * @param nickName
	 *            登入用户昵称
	 * @param initId
	 *            手机的初始化ID
	 * @param ursToken
	 *            手机URS登录后的Token
	 * @param mobileToken
	 *            手机的访问Token
	 * @param userIp
	 *            手机的访问用户IP
	 * @return 更新后的用户信息Map
	 */
	Map<String, Object> upsertURSUser(String userName, String nickName, String initId, String ursToken,
			String mobileToken, String userIp);

	/**
	 * 创建/更新手机的初始化ID和密钥。
	 * 
	 * @param id
	 *            手机的初始化ID
	 * @param key
	 *            手机的初始化密钥
	 */
	void saveInitIdAndKey(String id, String key);

	/**
	 * 第三方登录成功后创建/更新当前用户信息。
	 * 
	 * @param userName
	 *            登入用户名
	 * @param nickName
	 *            用户第三方用户昵称
	 * @param profileImage
	 *            用户第三方头像
	 * @param initId
	 *            手机的初始化ID
	 * @param ursToken
	 *            手机URS登录后的Token
	 * @param mobileToken
	 *            手机的访问Token
	 * @param userIp
	 *            手机的访问用户IP
	 * @return 更新后的用户信息Map
	 */
	Map<String, Object> upsertOauthUser(String userName, String nickName, String profileImage, String initId,
			String ursToken, String mobileToken, String userIp);

}