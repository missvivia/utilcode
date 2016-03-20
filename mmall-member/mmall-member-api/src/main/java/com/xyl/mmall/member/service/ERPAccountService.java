/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.service;

import java.util.List;

import com.xyl.mmall.member.meta.ERPAccount;

/**
 * ERPAccountService.java created by yydx811 at 2015年8月3日 上午10:57:30
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface ERPAccountService {

	/**
	 * 获取appid
	 * @param appId
	 * @return
	 */
	public ERPAccount getERPAccountByAppId(String appId);
	
	/**
	 * 获取全部
	 * @return
	 */
	public List<ERPAccount> getAll();
	
	/**
	 * 添加erp账号
	 * @param businessIds
	 * @return
	 */
	public ERPAccount addERPAccount(String businessIds);
}
