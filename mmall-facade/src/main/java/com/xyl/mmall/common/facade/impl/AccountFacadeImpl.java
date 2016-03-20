/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.common.facade.impl;

import javax.annotation.Resource;

import com.xyl.mmall.common.facade.AccountFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.service.AccountService;

/**
 * AccountFacadeImpl.java created by yydx811 at 2015年7月24日 上午10:17:16
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Facade
public class AccountFacadeImpl implements AccountFacade {

	@Resource
	private AccountService accountService;
	
	@Override
	public AccountDTO findAccountByUserName(String userName) {
		return accountService.findAccountByUserName(userName);
	}

	@Override
	public boolean updateAccount(AccountDTO accountDTO) {
		return accountService.updateAccount(accountDTO);
	}

	@Override
	public boolean matchesPassword(String password, AccountDTO accountDTO) {
		return accountService.matchesPassword(password, accountDTO);
	}
}
