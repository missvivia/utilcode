package com.xyl.mmall.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderLogistics;

@Repository
public class OrderLogisticsDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderLogistics> implements OrderLogisticsDao {

	@Override
	public List<OrderLogistics> getOrderLogisticsByOrderIds(long orderId) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		return queryObjects(sql);
	}
}
