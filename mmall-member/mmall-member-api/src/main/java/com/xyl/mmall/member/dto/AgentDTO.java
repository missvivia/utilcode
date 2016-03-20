/**
 * 
 */
package com.xyl.mmall.member.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.Agent;

/**
 * @author lihui
 *
 */
public class AgentDTO extends Agent {

	private static final long serialVersionUID = 1L;

	private List<RoleDTO> roleList;

	/** 是否更改密码，1是，其余无效. */
	private int isModifyPassword;

	/** 密码. */
	private String password;
	
	public AgentDTO() {
	}

	public AgentDTO(Agent obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * @return the roleList
	 */
	public List<RoleDTO> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList
	 *            the roleList to set
	 */
	public void setRoleList(List<RoleDTO> roleList) {
		this.roleList = roleList;
	}

	public int getIsModifyPassword() {
		return isModifyPassword;
	}

	public void setIsModifyPassword(int isModifyPassword) {
		this.isModifyPassword = isModifyPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
