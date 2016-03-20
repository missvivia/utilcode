/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PoOrderFormDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormSkuDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.meta.PoOrderForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("ReturnPoOrderFormSkuDao")
public class ReturnPoOrderFormSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ReturnPoOrderFormSku> implements
		ReturnPoOrderFormSkuDao {

	@Autowired
	private PoOrderFormDao poOrderDao;

	private String tableName = this.getTableName();

	private String sql_get_n_return_sku_count_by_poOrder_id = "SELECT SUM(count) FROM " + tableName
			+ " WHERE poOrderId=? and type=?";

	private String updateState = "update " + tableName + " set state = ? where poReturnOrderId = ? and state = ?";

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ReturnPoOrderFormSkuDao#getListByPoReturnOrderId(long)
	 */
	@Override
	public List<ReturnPoOrderFormSku> getListByPoReturnOrderId(long poReturnOrderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "poReturnOrderId", poReturnOrderId);
		return queryObjects(sql);
	}

	private long count(String sql) {
		return this.getSqlSupport().queryCount(sql.toString());
	}

	protected String genSelectSql(PoRetrunSkuQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		genWhereSql(sql, params);

		if (params.isPageable()) {
			sql.append(" ORDER BY createTime ");
			sql.append(" limit ").append(params.getOffset()).append(",").append(params.getLimit()).append(" ");
		}
		return sql.toString();
	}

	protected String genCountSql(PoRetrunSkuQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		genWhereSql(sql, params);
		return sql.toString();
	}

	private void genWhereSql(StringBuilder sql, PoRetrunSkuQueryParamDTO params) {
		long[] poStartTimeRang = params.getPoStartTimeRang();
		// ddb不支持 AND poOrderId in (select poOrderId from Mmall_Oms_PoOrderForm
		// where 1=1 AND startTime >= ? AND startTime < ?)
		if (poStartTimeRang != null && poStartTimeRang.length == 2 && poStartTimeRang[0] >0 && poStartTimeRang[1] > 0) {
			poStartTimeRang[0] = (poStartTimeRang[0] == 0) ? OmsConstants.LONG_BEFORE_LONG : poStartTimeRang[0];
			poStartTimeRang[1] = (poStartTimeRang[1] == 0) ? OmsConstants.LONG_AFTER_LONG : poStartTimeRang[1];
			List<PoOrderForm> poOrderForms = poOrderDao.getPoOrderList(null, 0, 0, poStartTimeRang[0],
					poStartTimeRang[1], 0, 0);
			if (poOrderForms != null && poOrderForms.size() > 0) {
				for (PoOrderForm poOrderForm : poOrderForms) {
					params.addPoOrderId(Long.parseLong(poOrderForm.getPoOrderId()));
				}
			} else {
				// 额... 没有符合条件的po，查询结果肯定为null，赋值一个绝对不成立的条件值
				params.setPoOrderIds(null);
				params.addPoOrderId(-1);
			}
		}
		Long[] poIds = params.getPoOrderIdArray();
		Long[] supplierIds = params.getSupplierIdArray();
		ReturnType[] types = params.getTypeArray();
		PoReturnOrderState[] states = params.getStateArray();
		if (poIds != null && poIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "poOrderId", poIds);
		}
		if (supplierIds != null && supplierIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "supplierId", supplierIds);
		}
		if (types != null && types.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "type", types);
		}
		if (states != null && states.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "state", states);
		}
		Long[] warehouseIds = params.getWarehouseIdArray();
		if (warehouseIds != null && warehouseIds.length > 0) {
			SqlGenUtil.appendExtParamArray(sql, "warehouseId", warehouseIds);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.WarehouseReturnDao#querReturn(com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO)
	 */
	@Override
	public PageableList<ReturnPoOrderFormSku> querReturnSku(PoRetrunSkuQueryParamDTO params) {
		List<ReturnPoOrderFormSku> data = queryObjects(genSelectSql(params));
		PageableList<ReturnPoOrderFormSku> list = null;
		if (params.isPageable()) {
			long total = count(genCountSql(params));
			list = new PageableList<ReturnPoOrderFormSku>(data, params.getLimit(), params.getOffset(), total);
		} else {
			list = new PageableList<ReturnPoOrderFormSku>(data);
		}
		return list;
	}

	public long countByParams(PoRetrunSkuQueryParamDTO params) {
		return count(genCountSql(params));
	}
	
	public long countSkuCountByParams(PoRetrunSkuQueryParamDTO params) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT SUM(count) FROM ").append(tableName).append(" where 1=1 ");
		genWhereSql(sql, params);
		return count(sql.toString());
	}
	

	@Override
	public int getTotalNReturnOfPoOrderId(String poOrderId, ReturnType type) {
		return this.getSqlSupport().queryCount(sql_get_n_return_sku_count_by_poOrder_id, poOrderId, type.getIntValue());
	}

	@Override
	public boolean updatePoReturnOrderSku(ReturnPoOrderFormSku sku) {
		StringBuilder sql = new StringBuilder();
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), new String[] { "shipTime", "realCount",
				"realDefectiveCount", "realNormalCount","state" }, new String[] { "poReturnOrderId", "skuId" }, sku));
		return this.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	@Override
	public boolean updateState(long poReturnOrderId, PoReturnOrderState oldState, PoReturnOrderState newState) {
		return this.getSqlSupport().excuteUpdate(updateState, newState.getIntValue(), poReturnOrderId, oldState.getIntValue()) > 0;
	}

}
