/**
 * 
 */
package com.xyl.mmall.member.service;

import com.xyl.mmall.member.dto.MobileInfoDTO;

/**
 * 手机登录信息相关服务。
 * 
 * @author lihui
 *
 */
public interface MobileInfoService {

	/**
	 * 创建/更新手机登录信息，其中包含初始化的ID和密钥。
	 * 
	 * @param initId
	 *            手机初始化是分配的ID
	 * @param initKey
	 *            手机初始化是分配的密钥
	 * @return 创建/更新后的手机登录信息
	 */
	MobileInfoDTO upsertMobileInfo(String initId, String initKey);

	/**
	 * 更新手机登录信息中的用户ID、Token等信息。
	 * 
	 * @param initId
	 *            手机初始化是分配的ID
	 * @param token
	 *            手机登录后获取的URS Token
	 * @param mobileToken
	 *            手机应用的访问Token
	 * @return 更新后的手机登录信息
	 */
	MobileInfoDTO updateUserInfo(String initId, String token, long userId, String mobileToken);

	/**
	 * 根据初始化ID查找手机登录信息。
	 * 
	 * @param id
	 *            手机初始化是分配的ID
	 * @return 手机登录信息
	 */
	MobileInfoDTO findMobileInfoByInitId(String id);

	/**
	 * 根据手机访问token查找手机登录信息。
	 * 
	 * @param token
	 *            手机访问token
	 * @return 手机登录信息
	 */
	MobileInfoDTO findMobileInfoByInitIdAndToken(String token);
}
