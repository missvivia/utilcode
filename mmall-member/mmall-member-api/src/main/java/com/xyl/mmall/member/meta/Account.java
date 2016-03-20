package com.xyl.mmall.member.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(tableName = "Mmall_Member_Account", desc = "用户账号")
public class Account implements Serializable {

	private static final long serialVersionUID = -8363069882678911191L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private Long id;

	@AnnonOfField(desc = "用户名", type = "VARCHAR(64)", uniqueKey = true, policy = true)
	private String username;

	@AnnonOfField(desc = "密码", type = "VARCHAR(64)")
	private String password;

	@AnnonOfField(desc = "邮箱", type = "VARCHAR(64)")
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
