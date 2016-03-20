package com.xyl.mmall.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderPackageRefund;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午10:24:50
 * 
 */
@Repository
public class OrderPackageRefundDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderPackageRefund> implements
		OrderPackageRefundDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPackageRefundDao#getListByOrderIdAndUserId(long,
	 *      long)
	 */
	public List<OrderPackageRefund> getListByOrderIdAndUserId(long orderId, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return queryObjects(sql);
	}

}