package com.xyl.mmall.order.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.meta.InvoiceInOrdSupplier;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class InvoiceInOrdSupplierDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<InvoiceInOrdSupplier> implements
		InvoiceInOrdSupplierDao {

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#
	 *      queryInvoiceByOrderIdAndUserId(long, long, DDBParam param)
	 */
	@Override
	public List<InvoiceInOrdSupplier> queryInvoiceByOrderIdAndUserId(long orderId, long userId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#queryInvoiceByOrderIdAndUserId(long)
	 */
	public List<InvoiceInOrdSupplier> queryInvoiceByOrderIdAndUserId(long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#getCount(long,
	 *      com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public int getCount(long supplierId, InvoiceInOrdSupplierState state) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		return getSqlSupport().queryCount(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#getListByOrderTimeRange(long,
	 *      long[], com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public List<InvoiceInOrdSupplier> getListByOrderTimeRangeAndState(long supplierId, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#getListByTitle(long,
	 *      java.lang.String, long[],
	 *      com.xyl.mmall.order.enums.InvoiceInOrdSupplierState)
	 */
	public List<InvoiceInOrdSupplier> getListByTitle(long supplierId, String title, long[] orderTimeRange,
			InvoiceInOrdSupplierState state, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		SqlGenUtil.appendExtParamObject(sql, "title", title);
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdSupplierDao#updateExpInfoAndState(com.xyl.mmall.order.meta.InvoiceInOrdSupplier)
	 */
	public boolean updateExpInfoAndState(InvoiceInOrdSupplier obj) {
		Set<String> setOfWhere = new HashSet<>();
		setOfWhere.add("orderId");
		setOfWhere.add("supplierId");
		Set<String> setOfUpdate = new HashSet<>();
		setOfUpdate.add("barCode");
		setOfUpdate.add("expressCompanyName");
		setOfUpdate.add("state");

		StringBuilder sql = new StringBuilder(64);
		sql.append(SqlGenUtil.genUpdateSql(getTableName(), setOfUpdate, setOfWhere, obj));
		return getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}
}
