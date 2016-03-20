package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.RejectPackageSkuDao;
import com.xyl.mmall.oms.enums.RejectPackageState;
import com.xyl.mmall.oms.meta.RejectPackageSku;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("rejectPackageSkuDao")
public class RejectPackageSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<RejectPackageSku> implements
		RejectPackageSkuDao {
	private String TABLE_NAME = getTableName();

	private String updateStateByPackageId = "update ".concat(TABLE_NAME).concat(
			" set state=?,packageStateUpdateTime=? where rejectPackageId=? and state=?");

	private String getByPackageId = "select * from ".concat(TABLE_NAME).concat(" where rejectPackageId=?");

	@Override
	public boolean updateStateByPackageId(long rejectPackageId, RejectPackageState newState, RejectPackageState oldState) {
		return this.getSqlSupport().excuteUpdate(updateStateByPackageId, newState.getIntValue(),
				System.currentTimeMillis(), rejectPackageId, oldState.getIntValue()) > 0;
	}

	@Override
	public List<RejectPackageSku> getByPackageId(long rejectPackageId) {
		return this.queryObjects(getByPackageId, rejectPackageId);
	}

}
