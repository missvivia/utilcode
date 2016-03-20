/**
 * 
 */
package com.xyl.mmall.security.service;

import java.util.Map;

/**
 * @author lihui
 *
 */
public interface MobileTokenService {

	/**
	 * 校验手机访问Token是否有效。
	 * 
	 * @param token
	 *            手机访问Token
	 * @return 校验结果
	 */
	String validateMobleToken(String token);

	/**
	 * 手机无缝登录接口。
	 * 
	 * @param id
	 *            手机初始化ID
	 * @param params
	 *            登录参数
	 * @param userIp
	 *            用户IP
	 * @return
	 */
	Map<String, Object> mobileSeamlessRelogin(String id, String params, String userIp);
}
