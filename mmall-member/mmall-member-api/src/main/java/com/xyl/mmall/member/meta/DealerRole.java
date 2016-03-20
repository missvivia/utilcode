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
@AnnonOfClass(tableName = "Mmall_Member_DealerRole", desc = "商家后台用户角色信息")
public class DealerRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商家后台用户Id", policy = true)
	private long dealerId;

	@AnnonOfField(desc = "商家后台角色Id", policy = true)
	private long roleId;

	@AnnonOfField(desc = "额外权限", type = "VARCHAR(255)", notNull = false)
	private String extraPermissions;

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
	 * @return the dealerId
	 */
	public long getDealerId() {
		return dealerId;
	}

	/**
	 * @param dealerId
	 *            the dealerId to set
	 */
	public void setDealerId(long dealerId) {
		this.dealerId = dealerId;
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
