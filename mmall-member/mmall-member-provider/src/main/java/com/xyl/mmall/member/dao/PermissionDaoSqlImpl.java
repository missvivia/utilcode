/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.Permission;

/**
 * @author lihui
 *
 */
@Repository
public class PermissionDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Permission> implements PermissionDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.PermissionDao#findByCategory(int)
	 */
	@Override
	@Cacheable("permissionsOfCategory")
	public List<Permission> findByCategory(int category) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		DDBParam param = new DDBParam();
		param.setAsc(true);
		param.setOrderColumn("Id");
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.PermissionDao#findByCategoryAndPermission(int,
	 *      java.lang.String)
	 */
	@Override
	@Cacheable("permissionOfCategoryAndDes")
	public Permission findByCategoryAndPermission(int category, String permission) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendExtParamObject(sql, "permission", permission);
		return queryObject(sql);
	}

}
