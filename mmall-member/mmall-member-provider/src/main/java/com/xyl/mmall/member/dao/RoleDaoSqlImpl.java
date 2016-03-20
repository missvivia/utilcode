/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.Role;

/**
 * @author lihui
 *
 */
@Repository
public class RoleDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Role> implements RoleDao {

	private static final String SQL_FIND_DEALER_ROLE_BY_DEALER_ID = "SELECT r.id, r.displayName, r.permissions, r.category, r.ownerId, r.createTime, r.lastModifiedBy, r.lastModifiedTime, r.parentId "
			+ "FROM Mmall_Member_Role r, Mmall_Member_DealerRole a  WHERE a.RoleId = r.Id AND a.DealerId = ?";

	private static final String SQL_FIND_AGENT_ROLE_BY_AGENT_ID = "SELECT r.id, r.displayName, r.permissions, r.category, r.ownerId, r.createTime, r.lastModifiedBy, r.lastModifiedTime, r.parentId "
			+ "FROM Mmall_Member_Role r, Mmall_Member_AgentRole a WHERE a.RoleId = r.Id AND a.AgentId = ?";

	private static final String SQL_FIND_DEALER_ROLE_BY_DEALER_NAME = "SELECT r.id, r.displayName, r.permissions, r.category, r.ownerId, r.createTime, r.lastModifiedBy, r.lastModifiedTime, r.parentId "
			+ "FROM Mmall_Member_Role r ,Mmall_Member_DealerRole a, Mmall_Member_Dealer d  WHERE a.RoleId = r.Id AND a.DealerId = d.Id AND d.Name = ?";

	private static final String SQL_FIND_AGENT_ROLE_BY_AGENT_NAME = "SELECT r.id, r.displayName, r.permissions, r.category, r.ownerId, r.createTime, r.lastModifiedBy, r.lastModifiedTime, r.parentId "
			+ "FROM Mmall_Member_Role r, Mmall_Member_AgentRole a, Mmall_Member_Agent d WHERE a.RoleId = r.Id AND a.AgentId = d.Id AND d.Name = ?";

	private static final String SQL_FIND_ADMIN_ROLE_BY_CATEGORY = "SELECT id, displayName, permissions, category, ownerId, createTime, lastModifiedBy, lastModifiedTime, parentId  "
			+ "FROM Mmall_Member_Role WHERE Category = ? AND ParentId = 0";

	private static final String SQL_FIND_ADMIN_ROLE = "SELECT id, displayName, permissions, category, ownerId, createTime, lastModifiedBy, lastModifiedTime, parentId  "
			+ "FROM Mmall_Member_Role WHERE Id = 1004104";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByCategoryAndOwnerId(int,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Role> findByCategoryAndOwnerId(int category, long ownerId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendExtParamObject(sql, "ownerId", ownerId);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#countByCategoryAndOwnerId(int,
	 *      long)
	 */
	@Override
	public int countByCategoryAndOwnerId(int category, long ownerId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendExtParamObject(sql, "ownerId", ownerId);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByDealerId(long)
	 */
	@Override
	public List<Role> findByDealerId(long dealerId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_DEALER_ROLE_BY_DEALER_ID);
		return queryObjects(sql.toString(), dealerId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByAgentId(long)
	 */
	@Override
	public List<Role> findByAgentId(long agentId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_AGENT_ROLE_BY_AGENT_ID);
		return queryObjects(sql.toString(), agentId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByCategory(int,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Role> findByCategory(int category, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#countByCategory(int)
	 */
	@Override
	public int countByCategory(int category) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(this.genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByCategoryAndParentIdIsNull(int)
	 */
	@Override
	public Role findByCategoryAndParentIdIsNull(int category) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_ADMIN_ROLE_BY_CATEGORY);
		return queryObject(sql.toString(), category);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByCategoryAndParentId(int,
	 *      long)
	 */
	@Override
	public List<Role> findByCategoryAndParentId(int category, long parentId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "category", category);
		SqlGenUtil.appendExtParamObject(sql, "parentId", parentId);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByDealerName(java.lang.String)
	 */
	@Override
	public List<Role> findByDealerName(String dealerName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_DEALER_ROLE_BY_DEALER_NAME);
		return queryObjects(sql.toString(), dealerName);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.RoleDao#findByAgentName(java.lang.String)
	 */
	@Override
	public List<Role> findByAgentName(String agentName) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_AGENT_ROLE_BY_AGENT_NAME);
		return queryObjects(sql.toString(), agentName);
	}

	@Override
	public Role getBackendAdmin() {
		StringBuilder sql = new StringBuilder(SQL_FIND_ADMIN_ROLE);
		return queryObject(sql.toString());
	}

}
