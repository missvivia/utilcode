package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.RejectPackageDao;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.meta.RejectPackage;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("rejectPackageDao")
public class RejectPackageDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RejectPackage> implements
		RejectPackageDao {
	private String TABLE_NAME = getTableName();

	private String updateState = "update ".concat(TABLE_NAME).concat(
			" set state=?,stateUpdateTime=? where rejectPackageId=? and state=?");

	private String getByExpressInfo = "select * from ".concat(TABLE_NAME).concat(" where expressCompany=? and expressNO=?");
	
	private String queryByCreateTime = "select * from ".concat(TABLE_NAME).concat(" where createTime>=? and createTime<?");

	@Override
	public boolean updateState(long rejectPackageId, RejectPackageState newState, RejectPackageState oldState) {
		return this.getSqlSupport().excuteUpdate(updateState, newState.getIntValue(),
				System.currentTimeMillis(), rejectPackageId, oldState.getIntValue()) > 0;
	}

	@Override
	public RejectPackage getByExpressInfo(String expressCompany, String expressNO) {
		return this.queryObject(getByExpressInfo, expressCompany, expressNO);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.RejectPackageDao#queryByCreateTime(long, long)
	 */
	@Override
	public List<RejectPackage> queryByCreateTime(long startTime, long endTime) {
		return this.queryObjects(queryByCreateTime, startTime, endTime);
	}
	
}
