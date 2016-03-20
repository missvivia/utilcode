/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.AgentRole;

/**
 * @author lihui
 *
 */
@Repository
public class AgentRoleDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<AgentRole> implements AgentRoleDao {

	private static Logger logger = Logger.getLogger(AgentDaoSqlImpl.class);
	
	private static final String SQL_FIND_BY_AGENT_NAME = "SELECT r.agentId, r.roleId, r.extraPermissions, r.sites, r.lastModifiedBy, r.lastModifiedTime "
			+ "FROM Mmall_Member_AgentRole r, Mmall_Member_Agent a WHERE r.AgentId = a.Id AND a.Name = ?";

	private static final String SQL_DELETE_BY_AGENT_ID_AND_ROLE_ID = "DELETE FROM Mmall_Member_AgentRole WHERE AgentId=? AND RoleId=?";

	private static final String SQL_DELETE_BY_AGENT_ID = "DELETE FROM Mmall_Member_AgentRole WHERE AgentId=?";

	private static final String SQL_DELETE_BY_ROLE_ID = "DELETE FROM Mmall_Member_AgentRole WHERE RoleId=?";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#findByAgentIdAndRoleId(long,
	 *      long)
	 */
	@Override
	public AgentRole findByAgentIdAndRoleId(long agentId, long roleId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "agentId", agentId);
		SqlGenUtil.appendExtParamObject(sql, "roleId", roleId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#findByAgentName(java.lang.String)
	 */
	@Override
	public List<AgentRole> findByAgentName(String agentName) {
		return queryObjects(SQL_FIND_BY_AGENT_NAME, agentName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#deleteByAgentIdAndRoleId(long,
	 *      long)
	 */
	@Override
	public boolean deleteByAgentIdAndRoleId(long agentId, long roleId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_AGENT_ID_AND_ROLE_ID, agentId, roleId) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#findByAgentId(long)
	 */
	@Override
	public List<AgentRole> findByAgentId(long agentId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "agentId", agentId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#deleteByAgentId(long)
	 */
	@Override
	public boolean deleteByAgentId(long agentId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_AGENT_ID, agentId) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentRoleDao#deleteByRoleId(long)
	 */
	@Override
	public boolean deleteByRoleId(long roleId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_ROLE_ID, roleId) > 0;
	}

	@Override
	public List<Long> getAgentIdByRoleId(long roleId) {
		StringBuilder sql = new StringBuilder("SELECT AgentId FROM ");
		sql.append(this.getTableName()).append(" WHERE RoleId = ").append(roleId);
		DBResource resource = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = (null == resource) ? null : resource.getResultSet();
		if (null == rs) {
			return null;
		}
		try {
			List<Long> idList = new ArrayList<Long>();
			while (rs.next()) {
				long id = rs.getLong(1);
				idList.add(id);
			}
			rs.close();
			return idList;
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			resource.close();
		}
		return null;
	}

}
