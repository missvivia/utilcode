/**
 * 
 */
package com.xyl.mmall.security.service;

import java.util.Map;

import com.xyl.mmall.security.token.MobileAccessToken;

/**
 * @author lihui
 *
 */
public interface MobileAuthcService {

	/**
	 * 
	 * @param pdtVersion
	 * @param mac
	 * @param deviceType
	 * @param systemName
	 * @param systemVersion
	 * @param resolution
	 * @param uniqueID
	 * @return
	 */
	Map<String, String> getInitAppIdKey(String pdtVersion, String mac, String deviceType, String systemName,
			String systemVersion, String resolution, String uniqueID);

	/**
	 * 
	 * @param id
	 * @param params
	 * @param initKey
	 * @param request
	 * @return
	 */
	Map<String, String> loginForMob(String id, String params, String initKey, String userIp);

	/**
	 * 
	 * @param id
	 * @param params
	 * @return
	 */
	Map<String, String> logoutForMob(String id, String params);

	/**
	 * @param result
	 * @param initKey
	 * @return
	 */
	Map<String, String> decryptURSToken(String result, String initKey);

	/**
	 * @param result
	 * @param initKey
	 * @return
	 */
	String encryptMobileResult(String result, String initKey);

	/**
	 * @param id
	 * @param ursTokenStr
	 * @param userName
	 * @param initKey
	 * @return
	 */
	MobileAccessToken validateOAuthToken(String id, String ursTokenStr, String userName, String initKey);

	/**
	 * @param userName
	 * @param key
	 * @return
	 */
	String genMobileToken(String userName, String key);

	/**
	 * @param initId
	 * @param ursTokenStr
	 * @param userIp
	 * @return
	 */
	MobileAccessToken validateURSToken(String initId, String ursTokenStr, String userIp);

	/**
	 * @param params
	 * @param params
	 * @param initKey
	 * @return
	 */
	Map<String, String> exchangeURSToken(String id, String params, String initKey);

	/**
	 * @param id
	 * @param params
	 * @param initKey
	 * @param username
	 * @return
	 */
	MobileAccessToken getUserInfoFromExt(String id, String params, String initKey, String username);
	
	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @return
	 */
	public Map<String, String> loginFromMobile(String userName, String password);
}
