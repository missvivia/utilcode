/**
 * 
 */
package com.xyl.mmall.security.thirdparty;

import com.xyl.mmall.security.token.MobileAccessToken;

/**
 * 第三方认证App提供的调用接口
 * 
 * @author lihui
 *
 */
public interface ThirdPartyApp {

	/**
	 * 根据UID和accessToken获取不同第三方认证用户的昵称等信息。
	 * 
	 * @param uid
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	MobileAccessToken retrieveNickName(String uid, String accessToken) throws Exception;
}
