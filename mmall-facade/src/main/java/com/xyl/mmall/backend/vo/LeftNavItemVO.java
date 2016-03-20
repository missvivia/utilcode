/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.enums.PermissionType;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class LeftNavItemVO {

	private PermissionDTO permission;

	private List<LeftNavItemVO> children;

	public LeftNavItemVO() {
	}

	public LeftNavItemVO(PermissionDTO permission) {
		this.permission = permission;
	}

	/**
	 * @return the permission
	 */
	@JsonIgnore
	public PermissionDTO getPermission() {
		return permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(PermissionDTO permission) {
		this.permission = permission;
	}

	/**
	 * @return the children
	 */
	public List<LeftNavItemVO> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<LeftNavItemVO> children) {
		this.children = children;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return permission.getUrl();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return permission.getName();
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return permission.getType() == PermissionType.NULL ? null : permission.getType().getDesc();
	}

}
