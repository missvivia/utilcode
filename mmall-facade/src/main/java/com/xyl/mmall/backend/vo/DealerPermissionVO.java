/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.PermissionDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class DealerPermissionVO {

	private PermissionDTO permission;

	private List<DealerPermissionVO> children;

	public DealerPermissionVO() {
		permission = new PermissionDTO();
	}

	public DealerPermissionVO(PermissionDTO permission) {
		this.permission = permission;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(PermissionDTO permission) {
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
	 * @return the children
	 */
	public List<DealerPermissionVO> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<DealerPermissionVO> children) {
		this.children = children;
	}

	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return permission.getName();
	}

	/**
	 * 
	 * @return the parent
	 */
	public long getParent() {
		return permission.getParentId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		permission.setId(id);
	}

	/**
	 * 
	 * @return the Id
	 */
	public long getId() {
		return permission.getId();
	}

}
