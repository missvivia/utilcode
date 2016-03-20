package com.xyl.mmall.order.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.enums.InvoiceInOrdState;
import com.xyl.mmall.order.meta.InvoiceInOrd;
import com.xyl.mmall.order.util.OrderUtil;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class InvoiceInOrdDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<InvoiceInOrd> implements InvoiceInOrdDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdDao#getListByOrderIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<InvoiceInOrd> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdDao#updateState(com.xyl.mmall.order.meta.InvoiceInOrd)
	 */
	public boolean updateState(InvoiceInOrd obj) {
		Collection<String> fieldNameCollOfUpdate = Arrays.asList(new String[] { "state" });
		return PrintDaoUtil.updateObjectByKey(obj, fieldNameCollOfUpdate, null, this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.InvoiceInOrdDao#getInvoiceInOrdByOrderTimeRangeWithMinOrderId(long,
	 *      com.xyl.mmall.order.enums.InvoiceInOrdState, long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<InvoiceInOrd> getInvoiceInOrdByOrderTimeRangeWithMinOrderId(long minOrderId, InvoiceInOrdState state,
			long[] orderTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "state", state);
		sql.append(" And orderId>").append(minOrderId);
		OrderUtil.appendOrderTimeRange(sql, orderTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}
}
