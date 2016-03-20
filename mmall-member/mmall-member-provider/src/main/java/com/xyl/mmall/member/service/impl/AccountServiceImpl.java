package com.xyl.mmall.member.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.xyl.mmall.member.dao.AccountDao;
import com.xyl.mmall.member.dto.AccountDTO;
import com.xyl.mmall.member.meta.Account;
import com.xyl.mmall.member.service.AccountService;

@Repository
public class AccountServiceImpl implements AccountService{
	
	private static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Resource
	private AccountDao accountDao;
	
	@Override
	public AccountDTO createAccount(AccountDTO accountDTO) {
		Account account = accountDao.getAccountByUserName(accountDTO.getUsername());
		if (account != null) {
			log.error(accountDTO.getUsername() + " account exists!");
			return null;
		}
		account = accountDao.addAccount(accountDTO);
		return account == null ? null : new AccountDTO(account);
	}

	@Override
	public boolean deleteAccountByUserName(String userName) {
		return accountDao.deleteAccountByUserName(userName);
	}

	@Override
	public boolean updateAccount(AccountDTO accountDTO) {
		return accountDao.updateAccount(accountDTO);
	}

	@Override
	public AccountDTO findAccountByUserName(String userName) {
		Account account = accountDao.getAccountByUserName(userName);
		return account == null ? null : new AccountDTO(account);
	}

	@Override
	public boolean matchesPassword(String password, AccountDTO accountDTO) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(10);
		return bCrypt.matches(password, "$2a$10$" + accountDTO.getPassword());
	}
}
