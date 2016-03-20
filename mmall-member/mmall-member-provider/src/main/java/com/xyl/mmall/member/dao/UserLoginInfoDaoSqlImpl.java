/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.UserLoginInfo;

/**
 * @author lihui
 *
 */
@Repository
public class UserLoginInfoDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<UserLoginInfo> implements
		UserLoginInfoDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.UserLoginInfoDao#findByUserId(long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<UserLoginInfo> findByUserId(long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

}
