/**
 * 
 */
package com.xyl.mmall.member.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.meta.Dealer;

/**
 * @author lihui
 *
 */
@Repository
public class DealerDaoSqlImpl extends PolicyObjectDaoSqlBaseOfAutowired<Dealer> implements DealerDao {

	private static Logger logger = Logger.getLogger(DealerDaoSqlImpl.class);
	
	private static final String SQL_FIND_BY_ROLE_OWNER_ID = "SELECT DISTINCT r.id, r.name,  r.regTime, r.mobile, r.realName, r.empNumber, r.department, "
			+ "r.accountStatus, r.supplierId, r.dealerType, r.lastLoginTime, r.lastModifiedBy, r.lastModifiedTime "
			+ "FROM Mmall_Member_Dealer r, Mmall_Member_DealerRole a, Mmall_Member_Role e WHERE a.DealerId = r.Id AND e.id = a.RoleId AND e.OwnerId = ? group by r.id";

	private static final String SQL_COUNT_BY_ROLE_OWNER_ID = "SELECT COUNT(DISTINCT r.Id) FROM Mmall_Member_Dealer r, Mmall_Member_DealerRole a, "
			+ "Mmall_Member_Role e WHERE a.DealerId = r.Id AND e.id = a.RoleId AND e.OwnerId = ?";

	private static final String SQL_UPDATE_SUPPLIER_ACCOUNT_STATUS = "UPDATE Mmall_Member_Dealer SET lastModifiedBy = ?, lastLoginTime = ?, accountStatus = ? WHERE supplierId = ?";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#findByName(java.lang.String)
	 */
	@Override
	public Dealer findByName(String name) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "name", name);
		return queryObject(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#findBySupplierId(long)
	 */
	@Override
	public List<Dealer> findBySupplierId(long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#findByRoleOwnerId(long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Dealer> findByRoleOwnerId(long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_FIND_BY_ROLE_OWNER_ID);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql.toString(), userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#countByRoleOwnerId(long)
	 */
	@Override
	public int countByRoleOwnerId(long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_COUNT_BY_ROLE_OWNER_ID);
		return getSqlSupport().queryCount(sql.toString(), userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#lockAllSupplierAccount(long,
	 *      long)
	 */
	@Override
	public boolean lockAllSupplierAccount(long supplierId, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(SQL_UPDATE_SUPPLIER_ACCOUNT_STATUS);
		return getSqlSupport().excuteUpdate(sql.toString(), userId, System.currentTimeMillis(),
				AccountStatus.LOCKED.getIntValue(), supplierId) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#findDealerByIdList(java.util.List)
	 */
	@Override
	public List<Dealer> findDealerByIdList(List<Long> idList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamColl(sql, "id", idList);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#findBySupplierId(long,
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	@Override
	public List<Dealer> findBySupplierId(long supplierId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendDDBParam(sql, param, null);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.dao.DealerDao#countBySupplierId(long)
	 */
	@Override
	public int countBySupplierId(long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		return getSqlSupport().queryCount(sql.toString());
	}
}
