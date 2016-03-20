/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.member.meta.Agent;
import com.xyl.mmall.member.param.AgentAccountSearchParam;

/**
 * @author lihui
 *
 */
public interface AgentDao extends AbstractDao<Agent> {

	/**
	 * 
	 * @param name
	 * @return
	 */
	Agent findByName(String name);

	/**
	 * @param userId
	 * @param param
	 * @param searchParam
	 * @return
	 */
	List<Agent> findByRoleOwnerId(long userId, DDBParam param, AgentAccountSearchParam searchParam);

	/**
	 * @param userId
	 * @param searchParam
	 * @return
	 */
	int countByRoleOwnerId(long userId, AgentAccountSearchParam searchParam);

	/**
	 * @param intValue
	 * @return
	 */
	List<Agent> findByAgentType(int intValue);

	/**
	 * @param idList
	 * @return
	 */
	List<Agent> findAgentByIdList(List<Long> idList);

	/**
	 * @param arrayList
	 * @return
	 */
	List<Agent> findByRoleIdList(List<Long> arrayList);
}
