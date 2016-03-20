/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.param;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * AgentAccountSearchParam.java created by yydx811 at 2015年7月31日 下午1:12:20
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class AgentAccountSearchParam implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -3699875330198939595L;

	/** 站点id. */
	private long siteId = 0;

	/** 角色id. */
	private long roleId = 0;

	/** 状态，-1取全部，0正常，1锁定. */
	private int status = -1;

	/** 搜索条件. */
	private String searchValue = "";

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public boolean isHashParam() {
		if (this.roleId > 0l) {
			return true;
		}
		if (this.siteId > 0l) {
			return true;
		}
		if (this.status >= 0) {
			return true;
		}
		if (StringUtils.isNotBlank(this.searchValue)) {
			return true;
		}
		return false;
	}
}
