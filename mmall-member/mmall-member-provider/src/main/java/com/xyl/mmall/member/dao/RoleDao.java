/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.member.meta.Role;

/**
 * @author lihui
 *
 */
public interface RoleDao extends AbstractDao<Role> {

	/**
	 * @param category
	 * @param ownerId
	 * @param param
	 * @return
	 */
	List<Role> findByCategoryAndOwnerId(int category, long ownerId, DDBParam param);

	/**
	 * 
	 * @param category
	 * @param ownerId
	 * @return
	 */
	int countByCategoryAndOwnerId(int category, long ownerId);

	/**
	 * @param category
	 * @param param
	 * @return
	 */
	List<Role> findByCategory(int category, DDBParam param);

	/**
	 * 
	 * @param category
	 * @return
	 */
	int countByCategory(int category);

	/**
	 * 
	 * @param dealerId
	 * @return
	 */
	List<Role> findByDealerId(long dealerId);

	/**
	 * 
	 * @param agentId
	 * @return
	 */
	List<Role> findByAgentId(long agentId);

	/**
	 * 
	 * @param dealerName
	 * @return
	 */
	List<Role> findByDealerName(String dealerName);

	/**
	 * 
	 * @param agentName
	 * @return
	 */
	List<Role> findByAgentName(String agentName);

	/**
	 * @param intValue
	 * @return
	 */
	Role findByCategoryAndParentIdIsNull(int category);

	/**
	 * @param category
	 * @param parentId
	 * @return
	 */
	List<Role> findByCategoryAndParentId(int category, long parentId);
	
	public Role getBackendAdmin();
}
