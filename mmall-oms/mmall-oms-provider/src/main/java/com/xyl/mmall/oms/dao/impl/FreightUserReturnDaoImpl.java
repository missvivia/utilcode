package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.FreightUserReturnDao;
import com.xyl.mmall.oms.meta.FreightUserReturn;

@Repository("freightUserReturnDao")
public class FreightUserReturnDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<FreightUserReturn> implements
FreightUserReturnDao {

	private String TABLE_NAME = this.getTableName();

	private String getByExpressInfo = "select * from ".concat(TABLE_NAME).concat(
			" where expressCompany=? and expressNO=?");
	
	private String queryFreight = "select * from ".concat(TABLE_NAME).concat(
			" where expressCompany=? and warehouseId=? and wmsReceivedTime >= ? and wmsReceivedTime <?");
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.FreightDao#getByExpressInfo(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public FreightUserReturn getByExpressInfo(String expressCompany, String expressNo) {
		return this.queryObject(getByExpressInfo, expressCompany, expressNo);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.FreightUserReturnDao#queryFreight(java.lang.String, long, long, long)
	 */
	@Override
	public List<FreightUserReturn> queryFreight(String expressCompany, long warehouseId, long startTime, long endTime) {
		return this.queryObjects(queryFreight, expressCompany, warehouseId, startTime, endTime);
	}

}
