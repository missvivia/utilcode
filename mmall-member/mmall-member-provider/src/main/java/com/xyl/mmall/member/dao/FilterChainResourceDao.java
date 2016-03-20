/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.FilterChainResource;

/**
 * @author lihui
 *
 */
public interface FilterChainResourceDao extends AbstractDao<FilterChainResource> {

	/**
	 * 
	 * @param category
	 * @return
	 */
	List<FilterChainResource> findByCategory(int category);
}
