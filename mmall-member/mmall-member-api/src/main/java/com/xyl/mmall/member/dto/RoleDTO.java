/**
 * 
 */
package com.xyl.mmall.member.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.AgentArea;
import com.xyl.mmall.member.meta.Role;

/**
 * @author lihui
 *
 */
public class RoleDTO extends Role {

	private static final long serialVersionUID = 1L;

	private List<PermissionDTO> permissionList;

	private List<Long> siteList;

	private List<AgentArea> areaList;
	
	public RoleDTO() {
	}

	public RoleDTO(Role obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * @return the permissionList
	 */
	public List<PermissionDTO> getPermissionList() {
		return permissionList;
	}

	/**
	 * @param permissionList
	 *            the permissionList to set
	 */
	public void setPermissionList(List<PermissionDTO> permissionList) {
		this.permissionList = permissionList;
	}

	/**
	 * @return the siteList
	 */
	public List<Long> getSiteList() {
		return siteList;
	}

	/**
	 * @param siteList
	 *            the siteList to set
	 */
	public void setSiteList(List<Long> siteList) {
		this.siteList = siteList;
	}

	public List<AgentArea> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<AgentArea> areaList) {
		this.areaList = areaList;
	}
}
