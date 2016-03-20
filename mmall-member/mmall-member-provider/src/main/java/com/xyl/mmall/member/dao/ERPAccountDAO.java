/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.ERPAccount;

/**
 * ERPAccountDAO.java created by yydx811 at 2015年8月3日 上午11:24:34
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public interface ERPAccountDAO extends AbstractDao<ERPAccount> {

	/**
	 * 按appid获取erpaccount
	 * @param appId
	 * @return
	 */
	public ERPAccount getERPAccountByAppId(String appId);

	/**
	 * 获取全部
	 * @return
	 */
	public List<ERPAccount> getAll();
}
