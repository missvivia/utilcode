/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_UserLoginInfo", desc = "主站用户登录信息")
public class UserLoginInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id", notNull = true, policy = true)
	private long userId;

	@AnnonOfField(desc = "登录的时间", notNull = true)
	private long loginTime;

	@AnnonOfField(desc = "登录的IP", type = "VARCHAR(16)")
	private String loginIp;

	@AnnonOfField(desc = "登录的IP", inDB = false)
	private String loginProvince;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the loginTime
	 */
	public long getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime
	 *            the loginTime to set
	 */
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * @param loginIp
	 *            the loginIp to set
	 */
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	/**
	 * @return the loginProvince
	 */
	public String getLoginProvince() {
		return loginProvince;
	}

	/**
	 * @param loginProvince
	 *            the loginProvince to set
	 */
	public void setLoginProvince(String loginProvince) {
		this.loginProvince = loginProvince;
	}

}
