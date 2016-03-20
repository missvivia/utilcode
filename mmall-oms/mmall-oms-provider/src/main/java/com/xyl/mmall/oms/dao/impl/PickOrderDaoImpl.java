package com.xyl.mmall.oms.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.PickOrderForm;

/**
 * @author zb
 *
 */
@Repository("PickOrderDao")
public class PickOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PickOrderForm> implements PickOrderDao {

	private String tableName = this.getTableName();

	private String sql_update_pickState_by_pickOrderId = "UPDATE " + tableName
			+ " set pickState=? , modifyTime=unix_timestamp(now())*1000 where pickOrderId=?";

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickOrderDao#getPickOrder(java.lang.String)
	 */
	@Override
	public PickOrderForm getPickOrder(String pickOrderId, long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());

		if (StringUtils.isBlank(pickOrderId)) {
			return null;
		}
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "pickOrderId", pickOrderId);
		return this.queryObject(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickOrderDao#updatePickOrder(com.xyl.mmall.oms.dto.PickOrderDTO)
	 */
	@Override
	public boolean updatePickOrder(PickOrderDTO pickOrderDTO) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("ExportTimes");
		fieldNameSetOfUpdate.add("firstExportTime");
//		fieldNameSetOfUpdate.add("JITFlag");
		fieldNameSetOfUpdate.add("ModifyTime");
		fieldNameSetOfUpdate.add("PickState");
//		fieldNameSetOfUpdate.add("PickTotalQuantity");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("PickOrderId");

		pickOrderDTO.setModifyTime(System.currentTimeMillis());

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, pickOrderDTO,
				getSqlSupport());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickOrderDao#updatePickSkuState(java.lang.String,
	 *      com.xyl.mmall.oms.enums.PickStateType)
	 */
	@Override
	public boolean updatePickSkuState(String pickOrderId, PickStateType type) {
		return this.getSqlSupport().excuteUpdate(sql_update_pickState_by_pickOrderId, type.getIntValue(), pickOrderId) >= 0;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickOrderDao#getPickListBySupplierIdAndTime(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<PickOrderForm> getPickListBySupplierIdAndTime(long supplierId, long startTime, long endTime) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		sql.append(" AND CreateTime >= ").append(startTime);
		sql.append(" AND CreateTime <= ").append(endTime);
		return this.queryObjects(sql);
	}
}
