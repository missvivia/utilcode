/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * @author hzlihui2014
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_UniqueUser", desc = "主站用户名唯一约束表")
public class UniqueUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long userId;

	@AnnonOfField(desc = "用户名", type = "VARCHAR(64)", policy = true, uniqueKey = true)
	private String userName;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
