/**
 * 
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.UserLoginInfo;

/**
 * @author lihui
 *
 */
public class UserLoginInfoDTO extends UserLoginInfo {

	private static final long serialVersionUID = 1L;

	public UserLoginInfoDTO() {
	}

	public UserLoginInfoDTO(UserLoginInfo obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
