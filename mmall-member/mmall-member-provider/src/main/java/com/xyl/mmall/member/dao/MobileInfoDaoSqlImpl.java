/**
 * 
 */
package com.xyl.mmall.member.dao;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.MobileInfo;

/**
 * @author lihui
 *
 */
@Repository
public class MobileInfoDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<MobileInfo> implements MobileInfoDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.MobileInfoDao#findByInitId(java.lang.String)
	 */
	@Override
	public MobileInfo findByInitId(String initId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "initId", initId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.MobileInfoDao#findByInitIdAndMobileToken(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public MobileInfo findByMobileToken(String token) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "mobileToken", token);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.MobileInfoDao#findByInitIdAndMobileToken(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public MobileInfo findByInitIdAndMobileToken(String initId, String token) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "initId", initId);
		SqlGenUtil.appendExtParamObject(sql, "mobileToken", token);
		return queryObject(sql);
	}

}
