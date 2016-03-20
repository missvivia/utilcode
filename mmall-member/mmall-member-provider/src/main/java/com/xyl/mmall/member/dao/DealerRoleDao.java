/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.DealerRole;

/**
 * @author lihui
 *
 */
public interface DealerRoleDao extends AbstractDao<DealerRole> {

	/**
	 * 
	 * @param dealerId
	 * @param roleId
	 * @return
	 */
	DealerRole findByDealerIdAndRoleId(long dealerId, long roleId);

	/**
	 * 
	 * @param dealerName
	 * @return
	 */
	List<DealerRole> findByDealerName(String dealerName);

	/**
	 * 
	 * @param dealerId
	 * @param roleId
	 * @return
	 */
	boolean deleteByDealerIdAndRoleId(long dealerId, long roleId);

	/**
	 * @param dealerId
	 * @return
	 */
	List<DealerRole> findByDealerId(long dealerId);

	/**
	 * @param dealerId
	 */
	boolean deleteByDealerId(long dealerId);

	/**
	 * @param roleId
	 */
	boolean deleteByRoleId(long roleId);
}
