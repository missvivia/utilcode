/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.Permission;

/**
 * @author lihui
 *
 */
public interface PermissionDao extends AbstractDao<Permission> {

	/**
	 * 
	 * @param category
	 * @return
	 */
	List<Permission> findByCategory(int category);

	/**
	 * @param intValue
	 * @param permission
	 * @return
	 */
	Permission findByCategoryAndPermission(int category, String permission);
}
