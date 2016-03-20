/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OrderTraceDao;
import com.xyl.mmall.oms.meta.OrderTrace;

/**
 * @author hzzengchengyuan
 *
 */
@Repository("OrderLogisticsTraceDao")
public class OrderTraceDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderTrace> implements OrderTraceDao {

	@Override
	public boolean exist(OrderTrace trace) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genCountSql());
		SqlGenUtil.appendExtParamObject(sql, "expressNO", trace.getExpressNO());
		SqlGenUtil.appendExtParamObject(sql, "expressCompany", trace.getExpressCompany());
		SqlGenUtil.appendExtParamObject(sql, "operate", trace.getOperate());
		SqlGenUtil.appendExtParamObject(sql, "childOperate", trace.getChildOperate());
		SqlGenUtil.appendExtParamObject(sql, "time", trace.getTime());
		return this.getSqlSupport().queryCount(sql.toString()) > 0;
	}

	@Override
	public List<OrderTrace> getTrace(String expressCompany, String expressNO) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "expressNO", expressNO);
		SqlGenUtil.appendExtParamObject(sql, "expressCompany", expressCompany);
		return queryObjects(sql.toString());
	}

}
