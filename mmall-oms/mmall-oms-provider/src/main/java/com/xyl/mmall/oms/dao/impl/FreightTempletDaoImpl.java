/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.oms.dao.FreightTempletDao;
import com.xyl.mmall.oms.meta.FreightTemplet;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("freightTempletDao")
public class FreightTempletDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<FreightTemplet> implements
		FreightTempletDao {
	private String TABLE_NAME = this.getTableName();

	private String getByOriginAndExpressCompany = "select * from ".concat(TABLE_NAME).concat(
			" where expressCompany=? and originId=? and targetId = ?");

	private String isEducationDistrict = "SELECT count(*) FROM Mmall_IP_RemoteArea WHERE Type = ? and (code = ? or ParentCode = ?) ";

	@Override
	public List<FreightTemplet> getAll() {
		return queryObjects(genSelectSql());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightTempletDao#getByOriginAndExpressCompany(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public FreightTemplet getByOriginTargetAndExpressCompany(String expressCompany, long origin, long target) {
		return this.queryObject(getByOriginAndExpressCompany, expressCompany, origin, target);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightTempletDao#getByOriginAnyTargetAndExpressCompany(java.lang.String,
	 *      long)
	 */
	@Override
	public FreightTemplet getByOriginAnyTargetAndExpressCompany(String expressCompany, long origin) {
		return this.queryObject(getByOriginAndExpressCompany, expressCompany, origin, 0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightTempletDao#isEducationDistrict(long,
	 *      long)
	 */
	@Override
	public boolean isEducationDistrict(ExpressCompany expressCompany, long districtId, long cityId) {
		return this.getSqlSupport().queryCount(isEducationDistrict, expressCompany.getIntValue(), districtId, cityId) > 0;
	}
}
