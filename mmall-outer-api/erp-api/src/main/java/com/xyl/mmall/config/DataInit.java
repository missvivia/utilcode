/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.config;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import com.xyl.mmall.member.meta.ERPAccount;
import com.xyl.mmall.member.service.ERPAccountService;
import com.xyl.mmall.utils.ERPAccountUtils;

/**
 * DataInit.java created by yydx811 at 2015年8月3日 下午4:33:03
 * erpaccount初始化
 *
 * @author yydx811
 */
public class DataInit implements InitializingBean {

	@Resource
	private ERPAccountService erpAccountService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// 初始化erp account
		List<ERPAccount> erpAccountList = erpAccountService.getAll();
		if (CollectionUtils.isNotEmpty(erpAccountList)) {
			ERPAccountUtils.getInstance().initMap(erpAccountList);
		}
	}

}
