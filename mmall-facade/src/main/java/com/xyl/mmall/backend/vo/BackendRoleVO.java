/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xyl.mmall.member.dto.RoleDTO;

/**
 * @author lihui
 *
 */
@JsonInclude(Include.NON_NULL)
public class BackendRoleVO {

	private RoleDTO role;

	private List<DealerPermissionVO> accessList;

	public BackendRoleVO() {
		role = new RoleDTO();
	}

	public BackendRoleVO(RoleDTO role) {
		this.setRole(role);
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(RoleDTO role) {
		this.role = role;
	}

	/**
	 * @return the role
	 */
	@JsonIgnore
	public RoleDTO getRole() {
		return role;
	}

	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return role.getDisplayName();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		role.setDisplayName(name);
	}

	/**
	 * 
	 * @return
	 */
	public Long getId() {
		return role.getId();
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		role.setId(id);
	}

	/**
	 * 
	 * @return
	 */
	public long getAddTime() {
		return role.getCreateTime();
	}

	/**
	 * 
	 * @return
	 */
	public long getEditTime() {
		return role.getLastModifiedTime();
	}

	/**
	 * @return the accessList
	 */
	public List<DealerPermissionVO> getAccessList() {
		return accessList;
	}

	/**
	 * @param accessList
	 *            the accessList to set
	 */
	public void setAccessList(List<DealerPermissionVO> accessList) {
		this.accessList = accessList;
	}

	/**
	 * 
	 * @return the editor
	 */
	public String getEditor() {
		return role.getOwnerName();
	}

}
