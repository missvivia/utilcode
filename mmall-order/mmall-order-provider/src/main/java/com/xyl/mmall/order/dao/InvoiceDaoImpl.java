package com.xyl.mmall.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.Invoice;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月5日下午1:26:25
 */
@Repository
public class InvoiceDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<Invoice> implements InvoiceDao {

	public boolean updateState(Invoice obj) {
		String sql = "UPDATE " + getTableName() + " SET state = " + obj.getState().getIntValue()
				+ " WHERE businessId =" + obj.getBusinessId();
		return this.getSqlSupport().excuteUpdate(sql)>0;
	}
	
	public List<Invoice> queryInvoiceByOrderId(long orderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}
}
