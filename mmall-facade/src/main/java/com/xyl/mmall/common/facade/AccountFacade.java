/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.common.facade;

import com.xyl.mmall.member.dto.AccountDTO;

/**
 * AccountFacade.java created by yydx811 at 2015年7月24日 上午10:14:54
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface AccountFacade {

	/**
	 * 按用户名获取账号信息
	 * @param userName
	 * @return
	 */
	public AccountDTO findAccountByUserName(String userName);
	
	/**
	 * 更新账号
	 * @param accountDTO
	 * @return
	 */
	public boolean updateAccount(AccountDTO accountDTO);
	
	/**
	 * 校验密码
	 * @param password
	 * @param accountDTO
	 * @return
	 */
	public boolean matchesPassword(String password, AccountDTO accountDTO);
}
