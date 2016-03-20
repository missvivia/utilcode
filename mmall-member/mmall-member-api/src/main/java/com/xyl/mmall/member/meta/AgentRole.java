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
@AnnonOfClass(tableName = "Mmall_Member_AgentRole", desc = "运维平台用户角色信息")
public class AgentRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "运维平台用户Id", policy = true)
	private long agentId;

	@AnnonOfField(desc = "运维平台角色Id", policy = true)
	private long roleId;

	@AnnonOfField(desc = "额外权限", type = "VARCHAR(255)", notNull = false)
	private String extraPermissions;

	@AnnonOfField(desc = "管理的站点", type = "VARCHAR(255)")
	private String sites;

	@AnnonOfField(desc = "最后修改人Id")
	private long lastModifiedBy;

	@AnnonOfField(desc = "最后修改时间")
	private long lastModifiedTime;

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
	 * @return the agentId
	 */
	public long getAgentId() {
		return agentId;
	}

	/**
	 * @param agentId
	 *            the agentId to set
	 */
	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the extraPermissions
	 */
	public String getExtraPermissions() {
		return extraPermissions;
	}

	/**
	 * @param extraPermissions
	 *            the extraPermissions to set
	 */
	public void setExtraPermissions(String extraPermissions) {
		this.extraPermissions = extraPermissions;
	}

	/**
	 * @return the sites
	 */
	public String getSites() {
		return sites;
	}

	/**
	 * @param sites
	 *            the sites to set
	 */
	public void setSites(String sites) {
		this.sites = sites;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public long getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the lastModifiedTime
	 */
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
