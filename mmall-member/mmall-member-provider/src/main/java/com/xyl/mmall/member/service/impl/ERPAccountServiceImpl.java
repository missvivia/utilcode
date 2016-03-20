/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.util.UUIDUtil;
import com.xyl.mmall.member.dao.ERPAccountDAO;
import com.xyl.mmall.member.meta.ERPAccount;
import com.xyl.mmall.member.service.ERPAccountService;

/**
 * ERPAccountServiceImpl.java created by yydx811 at 2015年8月3日 上午11:34:52
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Service
public class ERPAccountServiceImpl implements ERPAccountService {

	@Autowired
	private ERPAccountDAO erpAccountDAO;
	
	private static final String ERP_ACCOUNT_PREFIX = "xylerp";
	
	@Override
	@Cacheable(value = "erpAccountCache", key = "#appId")
	public ERPAccount getERPAccountByAppId(String appId) {
		return erpAccountDAO.getERPAccountByAppId(appId);
	}

	@Override
	public List<ERPAccount> getAll() {
		return erpAccountDAO.getAll();
	}

	@Override
	public ERPAccount addERPAccount(String businessIds) {
		ERPAccount account = new ERPAccount();
		long id = erpAccountDAO.allocateRecordId();
		if (id < 1l) {
			return null;
		}
		account.setId(id);
		account.setAppId(ERP_ACCOUNT_PREFIX + id);
		account.setAppIdKey(UUIDUtil.generateUUID());
		account.setBusinessIds(businessIds);
		return erpAccountDAO.addObject(account);
	}
}
