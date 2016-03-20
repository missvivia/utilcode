/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.meta.DealerRole;

/**
 * @author lihui
 *
 */
@Repository
public class DealerRoleDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<DealerRole> implements DealerRoleDao {

	private static final String SQL_FIND_BY_DEALER_NAME = "SELECT r.dealerId, r.roleId, r.extraPermissions, r.lastModifiedBy, r.lastModifiedTime "
			+ "FROM Mmall_Member_DealerRole r, Mmall_Member_Dealer a WHERE r.DealerId = a.Id AND a.Name = ?";

	private static final String SQL_DELETE_BY_DEALER_ID_AND_ROLE_ID = "DELETE FROM Mmall_Member_DealerRole WHERE DealerId=? AND RoleId=?";

	private static final String SQL_DELETE_BY_DEALER_ID = "DELETE FROM Mmall_Member_DealerRole WHERE DealerId=?";

	private static final String SQL_DELETE_BY_ROLE_ID = "DELETE FROM Mmall_Member_DealerRole WHERE RoleId=?";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#findByDealerIdAndRoleId(long,
	 *      long)
	 */
	@Override
	public DealerRole findByDealerIdAndRoleId(long dealerId, long roleId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "dealerId", dealerId);
		SqlGenUtil.appendExtParamObject(sql, "roleId", roleId);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#findByDealerName(java.lang.String)
	 */
	@Override
	public List<DealerRole> findByDealerName(String dealerName) {
		return queryObjects(SQL_FIND_BY_DEALER_NAME, dealerName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#deleteByDealerIdAndRoleId(long,
	 *      long)
	 */
	@Override
	public boolean deleteByDealerIdAndRoleId(long dealerId, long roleId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_DEALER_ID_AND_ROLE_ID, dealerId, roleId) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#findByDealerId(long)
	 */
	@Override
	public List<DealerRole> findByDealerId(long dealerId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "dealerId", dealerId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#deleteByDealerId(long)
	 */
	@Override
	public boolean deleteByDealerId(long dealerId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_DEALER_ID, dealerId) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerRoleDao#deleteByRoleId(long)
	 */
	@Override
	public boolean deleteByRoleId(long roleId) {
		return getSqlSupport().excuteUpdate(SQL_DELETE_BY_ROLE_ID, roleId) > 0;
	}

}
