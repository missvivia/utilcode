/**
 * 
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.UserProfile;

/**
 * @author lihui
 *
 */
public class UserProfileDTO extends UserProfile {

	private static final long serialVersionUID = 1L;

	private long lastLoginTime;

	public UserProfileDTO() {
	}

	public UserProfileDTO(UserProfile obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * @param userProfile
	 * @param lastLoginTime
	 */
	public UserProfileDTO(UserProfile obj, long lastLoginTime) {
		ReflectUtil.convertObj(this, obj, false);
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the lastLoginTime
	 */
	public long getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime
	 *            the lastLoginTime to set
	 */
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

}
