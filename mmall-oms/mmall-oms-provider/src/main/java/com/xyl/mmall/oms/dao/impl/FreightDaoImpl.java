package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.FreightDao;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;
import com.xyl.mmall.oms.meta.Freight;

@Repository("freightDao")
public class FreightDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Freight> implements FreightDao {

	private String TABLE_NAME = this.getTableName();

	private String getByExpressInfo = "select * from ".concat(TABLE_NAME).concat(
			" where expressCompany=? and expressNO=?");

	private String updatePackageState = "update ".concat(TABLE_NAME).concat(
			" set packageState=?,codCharge=?,stateUpdateTime=? where id=? and packageState=?");

	private String queryFreight = "select * from ".concat(TABLE_NAME).concat(
			" where expressCompany=? and warehouseId=? and shipTime >= ? and shipTime <?");

	@Override
	public Freight getByExpressInfo(String expressCompany, String expressNo) {
		return this.queryObject(getByExpressInfo, expressCompany, expressNo);
	}

	@Override
	public boolean updatePackageStateAndCodCharge(OmsOrderPackageState newState, double codCharge, long freightId,
			OmsOrderPackageState oldState) {
		return this.getSqlSupport().excuteUpdate(updatePackageState, newState.getIntValue(), codCharge,
				System.currentTimeMillis(), freightId, oldState.getIntValue()) > 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightDao#queryFreight(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<Freight> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime) {
		return this.queryObjects(queryFreight, expressCompany, warehouseId, startTime, endTime);
	}

}
