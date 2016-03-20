/**
 * 
 */
package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.Permission;

/**
 * @author lihui
 *
 */
public class PermissionDTO extends Permission {

	private static final long serialVersionUID = 1L;

	public PermissionDTO() {
	}

	public PermissionDTO(Permission obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
