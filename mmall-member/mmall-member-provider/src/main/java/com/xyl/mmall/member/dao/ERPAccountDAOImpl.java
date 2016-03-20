/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.ERPAccount;

/**
 * ERPAccountDAOImpl.java created by yydx811 at 2015年8月3日 上午11:25:10
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Repository
public class ERPAccountDAOImpl extends PolicyObjectDaoSqlBaseOfAutowired<ERPAccount> implements ERPAccountDAO {

	@Override
	public ERPAccount getERPAccountByAppId(String appId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "AppId", appId);
		return queryObject(sql.toString());
	}
	
	@Override
	public List<ERPAccount> getAll() {
		return super.getAll();
	}
}
