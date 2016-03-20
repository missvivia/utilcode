/**
 * 
 */
package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.enums.PermissionFunctionType;
import com.xyl.mmall.member.enums.PermissionType;

/**
 * @author lihui
 *
 */
@AnnonOfClass(tableName = "Mmall_Member_Permission", desc = "权限信息")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(primary = true, desc = "id")
	private long id;

	@AnnonOfField(desc = "功能名称", type = "VARCHAR(64)")
	private String name;

	@AnnonOfField(desc = "接口路径", type = "VARCHAR(64)")
	private String url;

	@AnnonOfField(desc = "操作权限表达式", type = "VARCHAR(64)")
	private String permission;

	@AnnonOfField(desc = "该权限的上级权限，如果为0则表示根权限")
	private long parentId;

	@AnnonOfField(desc = "权限类型")
	private PermissionType type;

	@AnnonOfField(desc = "权限的操作类型，0:左边导航条，1:按钮等操作权限")
	private PermissionFunctionType functionType;

	@AnnonOfField(desc = "权限类别,0:商家后台管理角色, 1:运维平台管理角色")
	private AuthzCategory category;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the type
	 */
	public PermissionType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(PermissionType type) {
		this.type = type;
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
	 * @return the functionType
	 */
	public PermissionFunctionType getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType
	 *            the functionType to set
	 */
	public void setFunctionType(PermissionFunctionType functionType) {
		this.functionType = functionType;
	}

}
