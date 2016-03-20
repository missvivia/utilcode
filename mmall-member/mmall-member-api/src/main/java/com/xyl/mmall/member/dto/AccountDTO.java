package com.xyl.mmall.member.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.member.meta.Account;

public class AccountDTO extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3939088966736882623L;
	
	public AccountDTO() {
	}
	
	public AccountDTO(Account account) {
		ReflectUtil.convertObj(this, account, false);
	}

}
