package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderExpInfo;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午9:42:39
 * 
 */
@Repository(value = "orderExpInfoDao")
public class OrderExpInfoDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderExpInfo> implements OrderExpInfoDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderExpInfoDao#getListByOrderIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<OrderExpInfo> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		return queryObjects(sql);
	}

	@Override
	public List<OrderExpInfo> queryInfoListByUserId(long userId, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	@Override
	public List<OrderExpInfo> queryInfoByConsigneeName(String consigneeName, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "consigneeName", consigneeName);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	@Override
	public List<OrderExpInfo> queryInfoByConsigneeMobile(String consigneeMobile, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "consigneeMobile", consigneeMobile);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

	@Override
	public List<OrderExpInfo> queryInfoByConsigneeAddress(String consigneeAddress, DDBParam ddbParam) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "address", consigneeAddress);
		return getListByDDBParam(sql.toString(), ddbParam);
	}

}
