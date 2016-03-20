/**
 * 
 */
package com.xyl.mmall.cms.vo;

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
public class CmsRoleVO {

	private RoleDTO role;

	private List<AgentPermissionVO> accessList;

	private List<SiteCMSVO> siteList;

	public CmsRoleVO() {
		role = new RoleDTO();
	}

	public CmsRoleVO(RoleDTO role) {
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
	 * @return
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
	public List<AgentPermissionVO> getAccessList() {
		return accessList;
	}

	/**
	 * @param accessList
	 *            the accessList to set
	 */
	public void setAccessList(List<AgentPermissionVO> accessList) {
		this.accessList = accessList;
	}

	/**
	 * @return the siteList
	 */
	public List<SiteCMSVO> getSiteList() {
		return siteList;
	}

	/**
	 * @param siteList
	 *            the siteList to set
	 */
	public void setSiteList(List<SiteCMSVO> siteList) {
		this.siteList = siteList;
	}

	/**
	 * 
	 * @return the editor
	 */
	public String getEditor() {
		return role.getOwnerName();
	}

	/**
	 * 
	 * @return
	 */
	public long getParent() {
		return role.getParentId();
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(long parent) {
		role.setParentId(parent);
	}
}
