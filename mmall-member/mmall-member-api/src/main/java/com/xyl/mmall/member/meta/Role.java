/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.member.enums.AuthzCategory;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_Role", desc = "运维平台角色信息")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "显示用的角色名", type = "VARCHAR(64)")
	private String displayName;

	@AnnonOfField(desc = "权限", type = "VARCHAR(512)")
	private String permissions;

	@AnnonOfField(desc = "角色类别,0:商家后台管理角色, 1:运维平台管理角色")
	private AuthzCategory category;

	@AnnonOfField(desc = "角色所有者的ID")
	private long ownerId;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "最后修改人Id")
	private long lastModifiedBy;

	@AnnonOfField(desc = "最后修改时间")
	private long lastModifiedTime;

	@AnnonOfField(desc = "该角色的上级角色，如果为0则表示ROOT角色")
	private long parentId;
	
	@AnnonOfField(desc = "角色所有者的用户名", inDB = false)
	private String ownerName;

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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the permissions
	 */
	public String getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the category
	 */
	public AuthzCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(AuthzCategory category) {
		this.category = category;
	}

	/**
	 * @return the ownerId
	 */
	public long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            the ownerId to set
	 */
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
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

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

}
