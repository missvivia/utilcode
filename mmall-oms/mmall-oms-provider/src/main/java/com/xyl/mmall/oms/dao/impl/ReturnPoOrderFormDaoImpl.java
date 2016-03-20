/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("ReturnPoOrderFormDao")
public class ReturnPoOrderFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnPoOrderForm> implements
		ReturnPoOrderFormDao {
	private String sqlUpdateReturnOrderState = "update " + this.getTableName()
			+ " set state=?,updateTime=? where poReturnOrderId=? and state=?";

	private String getByIdAndSupplierId = "select * from " + this.getTableName()
			+ " where poReturnOrderId=? and supplierId=?";

	@Override
	public ReturnPoOrderForm getByIdAndSupplierId(long returnOrderId, long supplierId) {
		return queryObject(getByIdAndSupplierId, returnOrderId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ReturnPoOrderFormDao#updateState(long,
	 *      com.xyl.mmall.oms.enums.PoReturnOrderState)
	 */
	@Override
	public boolean updateState(long orderId, PoReturnOrderState state, PoReturnOrderState oldState) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateReturnOrderState, state.getIntValue(),System.currentTimeMillis(), orderId,
				oldState.getIntValue()) > 0;
	}

	private long count(String sql) {
		return this.getSqlSupport().queryCount(sql.toString());
	}

	private String genSelectSql(PoRetrunOrderQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		genWhereSql(sql, params);
		if (params.isPageable()) {
			sql.append(" ORDER BY createTime desc ");
			sql.append(" limit ").append(params.getOffset()).append(",").append(params.getLimit()).append(" ");
		}
		return sql.toString();
	}

	private String genCountSql(PoRetrunOrderQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		genWhereSql(sql, params);
		return sql.toString();
	}

	private void genWhereSql(StringBuilder sql, PoRetrunOrderQueryParamDTO params) {
		Long[] supplierIds = params.getSupplierIdArray();
		Long[] returnOrderIds = params.getPoReturnOrderIdArray();
		PoReturnOrderState[] states = params.getStateArray();
		// 历史原因名字命名不准确，这个查询是指退货单的创建时间
		long[] orderCreateRang = params.getPoStartTimeRang();

		if (orderCreateRang != null) {
			if (orderCreateRang[0] != 0) {
				sql.append(" AND createTime >= ").append(orderCreateRang[0]);
			}

			if (orderCreateRang[1] != 0) {
				sql.append(" AND createTime <= ").append(orderCreateRang[1]);
			}
		}
		if (supplierIds != null && supplierIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "supplierId", supplierIds);
		}
		if (states != null && states.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "state", states);
		}
		if (returnOrderIds != null && returnOrderIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "poReturnOrderId", returnOrderIds);
		}
		Long[] warehouseIds = params.getWarehouseIdArray();
		if (warehouseIds != null && warehouseIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "warehouseId", warehouseIds);
		}
		if (params.getPoOrderId() > 0) {
			SqlGenUtil.appendExtParamObject(sql, "poOrderId", params.getPoOrderId());
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ReturnPoOrderFormDao#querReturn(com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO)
	 */
	public PageableList<ReturnPoOrderForm> querReturn(PoRetrunOrderQueryParamDTO params) {
		List<ReturnPoOrderForm> data = queryObjects(genSelectSql(params));
		PageableList<ReturnPoOrderForm> list = null;
		if (params.isPageable()) {
			long total = count(genCountSql(params));
			list = new PageableList<ReturnPoOrderForm>(data, params.getLimit(), params.getOffset(), total);
		} else {
			list = new PageableList<ReturnPoOrderForm>(data);
		}
		return list;
	}

	public long countByParams(PoRetrunOrderQueryParamDTO params) {
		return count(genCountSql(params));
	}

	@Override
	public boolean update(PoReturnOrderState state, ReturnPoOrderForm poReturnOrder) {
		StringBuilder sql = new StringBuilder();
		poReturnOrder.setUpdateTime(System.currentTimeMillis());
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), new String[] { "receiverAddress", "shipBoxQTY",
				"expressCompany", "state", "expressPhone", "volume", "weight", "shipTime", "realCount","updateTime" },
				new String[] { "poReturnOrderId" }, poReturnOrder));
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

}
