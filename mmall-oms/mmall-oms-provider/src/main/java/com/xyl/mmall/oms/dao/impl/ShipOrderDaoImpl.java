package com.xyl.mmall.oms.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.ShipOrderForm;

/**
 * @author zb
 *
 */
@Repository
public class ShipOrderDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ShipOrderForm> implements ShipOrderDao {
	/**
	 * 表名
	 */
	private String tableName = this.getTableName();

	private String sqlUpdateShipOrderStates = "UPDATE " + tableName + " SET shipState = ? WHERE shipOrderId=?";

	private String sql_get_shipOrder_by_pickOrderId = "SELECT * FROM " + tableName
			+ " WHERE pickOrderId=? and supplierId=?";

	private String sqlQueryByCreateTimeAndState = "select * from Mmall_Oms_ShipOrder where createtime<? and shipState=? order by createTime desc";

	private String sql_get_shipOrder_by_supplierid = "SELECT * FROM " + tableName + " WHERE supplierId=?";

	private String sqlSelectByCollectTime = "select * from Mmall_Oms_ShipOrder where collectTime>? and collectTime<=?";

	private String sqlSelectBySupplierIdAndCollectTime = "select * from Mmall_Oms_ShipOrder where supplierId=? and collectTime>? and collectTime<=?";

	/**
	 * 更新发货单信息
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipOrderDao#updateShipOrderInfo(com.xyl.mmall.oms.meta.ShipOrderForm)
	 */
	@Override
	public boolean updateWarehoureInfo(ShipOrderForm shipOrder) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("arrivalTime");
		fieldNameSetOfUpdate.add("shipState");
		fieldNameSetOfUpdate.add("arrivedCount");
		fieldNameSetOfUpdate.add("shipState");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("shipOrderId");
		shipOrder.setModifyTime(System.currentTimeMillis());

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, shipOrder, getSqlSupport());
	}

	/**
	 * 获取单条发货单
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipOrderDao#getShipOrderByShipId(java.lang.String)
	 */
	@Override
	public ShipOrderForm getShipOrderByShipId(String shipOrderId, long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "shipOrderId", shipOrderId);
		return queryObject(sql);
	}

	@Override
	public ShipOrderForm getShipOrderByPickOrderId(String pickOrderId, long supplierId) {
		return queryObject(sql_get_shipOrder_by_pickOrderId, pickOrderId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipOrderDao#getListByCreateTime(long,
	 *      com.xyl.mmall.oms.enums.ShipStateType, int)
	 */
	@Override
	public List<ShipOrderForm> getListByCreateTime(long createTime, ShipStateType state, int limit) {
		StringBuilder sb = new StringBuilder(sqlQueryByCreateTimeAndState);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), createTime, state.getIntValue());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipOrderDao#updateShipStateType(long,
	 *      com.xyl.mmall.oms.enums.ShipStateType)
	 */
	@Override
	public boolean updateShipStateType(String shipOrderId, ShipStateType shipState) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateShipOrderStates, shipState.getIntValue(), shipOrderId) > 0;
	}

	@Override
	public List<ShipOrderForm> getListBySupplierId(long supplierId) {
		return this.queryObjects(sql_get_shipOrder_by_supplierid, supplierId);
	}

	@Override
	public ShipOrderForm getShipOrderByShipId(String shipOrderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "shipOrderId", shipOrderId);
		return queryObject(sql);
	}

	@Override
	public List<ShipOrderForm> getShipOrderFormListByStateWithMinOrderId(String minOrderId,
			ShipStateType[] shipStateTypeArray, long[] createTimeRange, int limit) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "shipState", shipStateTypeArray);
		sql.append(" And shipOrderId>'").append(minOrderId).append("'");
		sql.append(" AND CreateTime>=").append(createTimeRange[0]).append(" AND CreateTime<")
				.append(createTimeRange[1]);
		this.appendOrderSql(sql, "shipOrderId", true);
		this.appendLimitSql(sql, limit, 0);
		return this.queryObjects(sql.toString());
	}

	@Override
	public List<ShipOrderForm> getListByCollectTime(long startCollectTime, long endCollectTime, int limit, int offset) {
		StringBuilder sb = new StringBuilder(sqlSelectByCollectTime);
		this.appendLimitSql(sb, limit, offset);
		return this.queryObjects(sb.toString(), startCollectTime, endCollectTime);
	}

	@Override
	public List<ShipOrderForm> getListByCollectTime(long supplierId, long startCollectTime, long endCollectTime) {
		return this.queryObjects(sqlSelectBySupplierIdAndCollectTime, supplierId, startCollectTime, endCollectTime);
	}
}
