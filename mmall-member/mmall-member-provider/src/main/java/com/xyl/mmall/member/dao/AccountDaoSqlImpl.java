package com.xyl.mmall.member.dao;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.Account;

@Repository
public class AccountDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Account> implements AccountDao {
	
	private static Logger logger = LoggerFactory.getLogger(AccountDaoSqlImpl.class);

	@Override
	public Account getAccountByUserName(String userName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "username", userName);
		return queryObject(sql);
	}

	@Override
	public boolean deleteAccountByUserName(String userName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genDeleteSql());
		SqlGenUtil.appendExtParamObject(sql, "username", userName);
		return super.getSqlSupport().updateRecords(sql.toString()) > 0;
	}

	@Override
	public Account addAccount(Account account) {
		account.setPassword(bCryptPassword(account.getPassword()));
		account.setId(this.allocateRecordId());
		return this.addObject(account);
	}

	@Override
	public boolean updateAccount(Account account) {
		logger.info("update password! username : " + account.getUsername());
		String password = bCryptPassword(account.getPassword());
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("username");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("password");
		account.setPassword(password);
		StringBuilder sql = new StringBuilder(128);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, account));
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	// 加密
	private String bCryptPassword(String password) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(10);
		return bCrypt.encode(password).substring(7);
	}
}
