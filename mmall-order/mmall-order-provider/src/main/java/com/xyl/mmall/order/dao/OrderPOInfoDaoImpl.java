package com.xyl.mmall.order.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderPOInfo;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class OrderPOInfoDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderPOInfo> implements OrderPOInfoDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderPOInfoDao#getListByPoId(long,
	 *      long, com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<OrderPOInfo> getListByPoId(long minId, long poId, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "poId", poId);
		sql.append(" And id>").append(minId);
		return getListByDDBParam(sql.toString(), param);
	}
}
