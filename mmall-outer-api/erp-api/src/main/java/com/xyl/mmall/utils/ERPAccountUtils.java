/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xyl.mmall.member.meta.ERPAccount;

/**
 * ERPAccountUtils.java created by yydx811 at 2015年8月3日 下午5:17:26
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
public class ERPAccountUtils {

	private static Map<String, ERPAccount> ERPAccountMap = new ConcurrentHashMap<String, ERPAccount>();
	
	public static ERPAccountUtils getInstance() {
		return holder.ERP_ACCOUNT_UTILS;
	}
	
	private static class holder {
		private static final ERPAccountUtils ERP_ACCOUNT_UTILS = new ERPAccountUtils();
	}
	
	public void initMap(List<ERPAccount> erpAccountList) {
		for (ERPAccount erpAccount : erpAccountList) {
			ERPAccountMap.put(erpAccount.getAppId(), erpAccount);
		}
	}
	
	/**
	 * 获取erpaccount
	 * @param appId
	 * @return
	 */
	public static ERPAccount getERPAccountByAppId(String appId) {
		return ERPAccountMap.get(appId);
	}
	
	/**
	 * 获取erp绑定商家id
	 * @param appId
	 * @return
	 */
	public static String getERPAccountBusinessIdsByAppId(String appId) {
		ERPAccount erpAccount = ERPAccountMap.get(appId);
		return erpAccount == null ? null : erpAccount.getBusinessIds();
	}
	
	public void addOne(ERPAccount erpAccount) {
		ERPAccountMap.put(erpAccount.getAppId(), erpAccount);
	}
}
