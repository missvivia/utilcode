package com.xyl.mmall.member.service;

import com.xyl.mmall.member.dto.AccountDTO;


public interface AccountService {
	
	/**
	 * 创建账号
	 * @param accountDTO
	 */
	public AccountDTO createAccount(AccountDTO accountDTO);
	
	
	/**
	 * 修改
	 * @param accountDTO
	 */
	public boolean updateAccount(AccountDTO accountDTO);
	
	/**
	 * 根据用户名删账号
	 * @param email
	 * @return
	 */
	public boolean deleteAccountByUserName(String userName);
	
	
	/**
	 * 根据用户名取账号
	 * @param userName
	 * @return
	 */
	public AccountDTO findAccountByUserName(String userName);
	
	/**
	 * 校验密码
	 * @param password
	 * @param accountDTO
	 * @return
	 */
	public boolean matchesPassword(String password, AccountDTO accountDTO);
}
