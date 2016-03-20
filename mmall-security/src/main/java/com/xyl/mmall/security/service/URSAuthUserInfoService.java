/**
 * 
 */
package com.xyl.mmall.security.service;

/**
 * @author lihui
 *
 */
public interface URSAuthUserInfoService {

	/**
	 * 获取URS用户的昵称。
	 * 
	 * @param userName
	 *            URS用户名
	 * @return 用户的昵称
	 */
	String getNicknameFromURS(String userName);
}
