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
@AnnonOfClass(tableName = "Mmall_Member_MobileInfo", desc = "手机客户端信息")
public class MobileInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "初始化时分配的ID", type = "VARCHAR(128)", uniqueKey = true, policy = true)
	private String initId;

	@AnnonOfField(desc = "初始化时分配的key", type = "VARCHAR(128)")
	private String initKey;

	@AnnonOfField(desc = "初始化的时间")
	private long initTime;

	@AnnonOfField(desc = "当前登录用户ID", notNull = false)
	private long userId;

	@AnnonOfField(desc = "URS访问Token", type = "VARCHAR(255)", notNull = false)
	private String ursToken;

	@AnnonOfField(desc = "手机应用访问Token", type = "VARCHAR(255)", notNull = false)
	private String mobileToken;

	@AnnonOfField(desc = "手机应用访问Token过期时间", notNull = false)
	private long expiredTime;

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
	 * @return the initId
	 */
	public String getInitId() {
		return initId;
	}

	/**
	 * @param initId
	 *            the initId to set
	 */
	public void setInitId(String initId) {
		this.initId = initId;
	}

	/**
	 * @return the initKey
	 */
	public String getInitKey() {
		return initKey;
	}

	/**
	 * @param initKey
	 *            the initKey to set
	 */
	public void setInitKey(String initKey) {
		this.initKey = initKey;
	}

	/**
	 * @return the initTime
	 */
	public long getInitTime() {
		return initTime;
	}

	/**
	 * @param initTime
	 *            the initTime to set
	 */
	public void setInitTime(long initTime) {
		this.initTime = initTime;
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
	 * @return the ursToken
	 */
	public String getUrsToken() {
		return ursToken;
	}

	/**
	 * @param ursToken
	 *            the ursToken to set
	 */
	public void setUrsToken(String ursToken) {
		this.ursToken = ursToken;
	}

	/**
	 * @return the mobileToken
	 */
	public String getMobileToken() {
		return mobileToken;
	}

	/**
	 * @param mobileToken
	 *            the mobileToken to set
	 */
	public void setMobileToken(String mobileToken) {
		this.mobileToken = mobileToken;
	}

	/**
	 * @return the expiredTime
	 */
	public long getExpiredTime() {
		return expiredTime;
	}

	/**
	 * @param expiredTime
	 *            the expiredTime to set
	 */
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

}
