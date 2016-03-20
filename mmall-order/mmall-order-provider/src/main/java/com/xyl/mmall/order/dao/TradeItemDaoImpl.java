package com.xyl.mmall.order.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.meta.base.DDBParam;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * @author dingmingliang
 * 
 */
@Repository
public class TradeItemDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<TradeItem> implements TradeItemDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.TradeItemDao#getListByOrderId(long,
	 *      long)
	 */
	public List<TradeItem> getListByOrderId(long orderId, long userId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		return queryObjects(sql);
	}
	
    public List<TradeItem> getListByParentId(long parentId, long userId)
    {
        StringBuilder sql = new StringBuilder(256);
        sql.append(genSelectSql());
        SqlGenUtil.appendExtParamObject(sql, "parentId", parentId);
        if(userId>=0){
        	SqlGenUtil.appendExtParamObject(sql, "userId", userId);
        }
        return queryObjects(sql);
    }

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.TradeItemDao#getTradeItemDTOListWithMinTradeId(long,
	 *      com.xyl.mmall.order.enums.TradeItemPayMethod,
	 *      com.xyl.mmall.framework.enums.PayState[], long[],
	 *      com.netease.print.daojar.meta.base.DDBParam)
	 */
	public List<TradeItem> getTradeItemDTOListWithMinTradeId(long minTradeId, TradeItemPayMethod payMethod,
			PayState[] payStateArray, long[] payTimeRange, DDBParam param) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "payState", payStateArray);
		SqlGenUtil.appendExtParamObject(sql, "tradeItemPayMethod", payMethod);
		sql.append(" And tradeId>").append(minTradeId);
		appendTimeRange(sql, "payTime", payTimeRange);
		return getListByDDBParam(sql.toString(), param);
	}

	/**
	 * 添加付款时间范围的条件
	 * 
	 * @param sql
	 * @param name
	 * @param timeRange
	 */
	private void appendTimeRange(StringBuilder sql, String name, long[] timeRange) {
		if (timeRange != null)
			sql.append(" AND ").append(name).append(">=").append(timeRange[0]).append(" AND ").append(name)
					.append("<=").append(timeRange[1]);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.TradeItemDao#updatePayState(com.xyl.mmall.order.meta.TradeItem,
	 *      com.xyl.mmall.framework.enums.PayState[])
	 */
	public boolean updatePayState(TradeItem tradeItem, PayState[] oldPayStateArray) {
		StringBuilder extWhereSql = new StringBuilder(256);
		extWhereSql.append(" 1=1 ");
		SqlGenUtil.appendExtParamArray(extWhereSql, "payState", oldPayStateArray);
		return PrintDaoUtil.updateObjectByKey(tradeItem, Arrays.asList(new String[] { "payState" }), null, null,
				extWhereSql.toString(), this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.TradeItemDao#setTradeSuccessInEPay(com.xyl.mmall.order.meta.TradeItem,
	 *      com.xyl.mmall.framework.enums.PayState[])
	 */
	public boolean setTradeSuccessInEPay(TradeItem tradeItem, PayState[] oldPayStateArray) {
		StringBuilder extWhereSql = new StringBuilder(256);
		extWhereSql.append(" 1=1 ");
		SqlGenUtil.appendExtParamArray(extWhereSql, "payState", oldPayStateArray);
		return PrintDaoUtil.updateObjectByKey(tradeItem,
				Arrays.asList(new String[] { "payTime", "payState", "orderSn", "payOrderSn" }), null, null,
				extWhereSql.toString(), this);
	}
}
