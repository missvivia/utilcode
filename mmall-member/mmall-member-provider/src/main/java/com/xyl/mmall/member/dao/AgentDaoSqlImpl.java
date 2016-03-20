/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.Agent;
import com.xyl.mmall.member.param.AgentAccountSearchParam;

/**
 * @author lihui
 *
 */
@Repository
public class AgentDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Agent> implements AgentDao {

	private static Logger logger = Logger.getLogger(AgentDaoSqlImpl.class);
	
	private static String accountList = "SELECT DISTINCT r.id, r.name, r.regTime, r. mobile, r.accountStatus, r.agentType, r.realName, "
			+ "r.empNumber, r.department, r.lastLoginTime, r.lastModifiedBy, r.lastModifiedTime, r.email FROM Mmall_Member_Agent r, "
			+ "Mmall_Member_AgentRole a, Mmall_Member_Role e WHERE a.AgentId = r.Id AND e.id = a.RoleId ";
	
	private static String countAccountList = "SELECT COUNT(DISTINCT r.Id) FROM Mmall_Member_Agent r, Mmall_Member_AgentRole a, "
			+ "Mmall_Member_Role e WHERE a.AgentId = r.Id AND e.id = a.RoleId ";
	
	private static final String SQL_FIND_BY_ROLE_ID_LIST = "SELECT DISTINCT r.id, r.name, r.regTime, r. mobile, r.accountStatus, r.agentType, "
			+ "r.realName, r.empNumber, r.department, r.lastLoginTime, r.lastModifiedBy, r.lastModifiedTime, r.email FROM Mmall_Member_Agent r, "
			+ "Mmall_Member_AgentRole a WHERE a.AgentId = r.Id ";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#findByName(java.lang.String)
	 */
	@Override
	public Agent findByName(String name) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "name", name);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#findByRoleOwnerId(long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Agent> findByRoleOwnerId(long userId, DDBParam param, AgentAccountSearchParam searchParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(accountList);
		createListSQL(sql, userId, searchParam);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql.toString());
	}

	private void createListSQL(StringBuilder sql, long userId, AgentAccountSearchParam searchParam) {
		if (userId > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "e.OwnerId", userId);
		}
		if (searchParam.getSiteId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "a.Sites", String.valueOf(searchParam.getSiteId()));
		}
		if (searchParam.getRoleId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "e.Id", searchParam.getRoleId());
		}
		if (searchParam.getStatus() >= 0) {
			SqlGenUtil.appendExtParamObject(sql, "r.accountStatus", searchParam.getStatus());
		}
		if (StringUtils.isNotBlank(searchParam.getSearchValue())) {
			sql.append(" AND r.name LIKE '%").append(searchParam.getSearchValue().replaceAll("'", "\\\\'")).append("%'");
		}
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#countByRoleOwnerId(long)
	 */
	@Override
	public int countByRoleOwnerId(long userId, AgentAccountSearchParam searchParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(countAccountList);
		createListSQL(sql, userId, searchParam);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#findByAgentType(int)
	 */
	@Override
	public List<Agent> findByAgentType(int intValue) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "agentType", intValue);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#findAgentByIdList(java.util.List)
	 */
	@Override
	public List<Agent> findAgentByIdList(List<Long> idList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "id", idList);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.AgentDao#findByRoleIdList(java.util.List)
	 */
	@Override
	public List<Agent> findByRoleIdList(List<Long> idList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_BY_ROLE_ID_LIST);
		SqlGenUtil.appendExtParamColl(sql, "a.RoleId", idList);
		return queryObjects(sql);
	}
}
