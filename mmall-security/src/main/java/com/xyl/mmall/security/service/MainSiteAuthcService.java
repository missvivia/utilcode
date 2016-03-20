/**
 * 
 */
package com.xyl.mmall.security.service;

import com.xyl.mmall.security.token.MobileAccessToken;

/**
 * @author lihui
 *
 */
public interface MainSiteAuthcService {

	/**
	 * 获取第三方登录用户的昵称等信息。第三方应用的类型可以根据用户名判断。 例如新浪微博的用户名为：736463712@sina.163.com,
	 * QQ的用户名为： 123usj29sdf761i23ghy@tencent.163.com等等。
	 * 
	 * @param userName
	 *            第三方登录用户名
	 * @return 用户在第三方应用中的昵称
	 */
	MobileAccessToken getNickNameFromExt(String userName);

}
