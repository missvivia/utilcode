/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.member.meta.AgentArea;

/**
 * AgentAreaDao.java created by yydx811 at 2015年7月23日 上午10:08:58
 * 管理员区域dao
 *
 * @author yydx811
 */
public interface AgentAreaDao extends AbstractDao<AgentArea> {

	/**
	 * 获取管理可用区域
	 * @param agentId
	 * @param siteId 0 取全部
	 * @return
	 */
	public List<AgentArea> getAgentAreaList(long agentId, long siteId);
	
	/**
	 * 删除
	 * @param agentId
	 * @return
	 */
	public int deleteAgentArea(long agentId);
	
	
	/**
	 * 根据站点区域Id获取agentId List
	 * @param siteId 
	 * @return
	 */
	public List<Long> getAgentIdListByAreaIds(List<Long>areaIds);
	
}
