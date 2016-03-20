/**
 * 
 */
package com.xyl.mmall.member.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.DealerRole;

/**
 * @author lihui
 *
 */
public class DealerRoleDTO extends DealerRole {

	private static final long serialVersionUID = 1L;

	private List<PermissionDTO> permissionList;

	public DealerRoleDTO() {
	}

	public DealerRoleDTO(DealerRole obj) {
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
}
