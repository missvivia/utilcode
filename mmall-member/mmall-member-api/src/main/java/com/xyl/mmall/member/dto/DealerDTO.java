/**
 * 
 */
package com.xyl.mmall.member.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.Dealer;

/**
 * @author lihui
 *
 */
public class DealerDTO extends Dealer {

	private static final long serialVersionUID = 1L;

	private List<RoleDTO> roleList;
	
	/**
	 * 新增account表时的密码
	 */
	private String password;
	
	/**
	 * 编辑时密码是否修改  默认N， 改变Y
	 */
	private String passwordIsChange = "N";

	public DealerDTO() {
	}

	public DealerDTO(Dealer obj) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordIsChange() {
		return passwordIsChange;
	}

	public void setPasswordIsChange(String passwordIsChange) {
		this.passwordIsChange = passwordIsChange;
	}
	
	
	

}
