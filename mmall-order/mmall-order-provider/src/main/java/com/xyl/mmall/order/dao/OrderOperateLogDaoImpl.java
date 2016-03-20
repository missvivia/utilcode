/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.order.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderOperateLog;

/**
 * OrderOperateLogDaoImpl.java created by yydx811 at 2015年6月11日 下午12:10:55
 * 这里对类或者接口作简要描述
 *
 * @author yydx811
 */
@Repository
public class OrderOperateLogDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderOperateLog> implements
		OrderOperateLogDao {

	@Override
	public List<OrderOperateLog> queryOperateLog(OrderOperateLog operateLog, String startTime, String endTime) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "OrderId", operateLog.getOrderId());
		if (operateLog.getBusinessId() > 0l) {
			SqlGenUtil.appendExtParamObject(sql, "BusinessId", operateLog.getBusinessId());
		}
		if (StringUtils.isNotBlank(startTime)) {
			sql.append(" AND CreateTime >= '").append(startTime).append("'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" AND CreateTime < '").append(endTime).append("'");
		}
		appendOrderSql(sql, "CreateTime", true);
		return this.queryObjects(sql.toString());
	}
}
