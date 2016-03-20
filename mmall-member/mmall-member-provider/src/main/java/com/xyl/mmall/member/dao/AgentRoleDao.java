/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.AgentRole;

/**
 * @author lihui
 *
 */
public interface AgentRoleDao extends AbstractDao<AgentRole> {

	/**
	 * 
	 * @param agentId
	 * @param roleId
	 * @return
	 */
	AgentRole findByAgentIdAndRoleId(long agentId, long roleId);

	/**
	 * @param agentName
	 * @return
	 */
	List<AgentRole> findByAgentName(String agentName);

	/**
	 * 
	 * @param agentId
	 * @param roleId
	 * @return
	 */
	boolean deleteByAgentIdAndRoleId(long agentId, long roleId);

	/**
	 * @param agentId
	 * @return
	 */
	List<AgentRole> findByAgentId(long agentId);

	/**
	 * @param agentId
	 */
	boolean deleteByAgentId(long agentId);

	/**
	 * @param roleId
	 */
	boolean deleteByRoleId(long roleId);
	
	public List<Long> getAgentIdByRoleId(long roleId);
}
