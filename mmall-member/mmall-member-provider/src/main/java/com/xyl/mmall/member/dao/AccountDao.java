package com.xyl.mmall.member.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.Account;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月10日下午3:14:38
 */
public interface AccountDao extends AbstractDao<Account>{
	
	/**
	 * 根据用户名取得账号
	 * @param userName
	 * @return
	 */
	public Account getAccountByUserName(String userName);
	
	/**
	 * 根据用户名删账号
	 * @param userName
	 * @return
	 */
	public boolean deleteAccountByUserName(String userName);
	
	/**
	 * 添加账号
	 * @param account
	 * @return
	 */
	public Account addAccount(Account account);
	
	/**
	 * 更新账号密码
	 * @param account
	 * @return
	 */
	public boolean updateAccount(Account account);

}
