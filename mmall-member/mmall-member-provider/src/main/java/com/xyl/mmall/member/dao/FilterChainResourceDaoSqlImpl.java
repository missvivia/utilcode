/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.FilterChainResource;

/**
 * @author lihui
 *
 */
@Repository
public class FilterChainResourceDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<FilterChainResource> implements
		FilterChainResourceDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.FilterChainResourceDao#findByCategory(int)
	 */
	@Override
	public List<FilterChainResource> findByCategory(int category) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		DDBParam param = new DDBParam();
		param.setOrderColumn("OrderBy");
		param.setAsc(true);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}
}
