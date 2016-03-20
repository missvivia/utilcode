package com.xyl.mmall.oms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.meta.OmsOrderForm;

/**
 * @author zb
 *
 */
@Repository("OmsOrderFormDao")
public class OmsOrderFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OmsOrderForm> implements OmsOrderFormDao {

	private String sqlUpdateOrderFormState = "update Mmall_Oms_OmsOrderForm set omsOrderFormState=? where omsOrderFormId=? and omsOrderFormState=?";

	private String sqlUpdateOrderFormStateWithoutCheck = "update Mmall_Oms_OmsOrderForm set omsOrderFormState=? where omsOrderFormId=?";

	private String sqlUpdateOrderShipInfo = "update Mmall_Oms_OmsOrderForm set omsOrderFormState=?,shiptime=? where omsOrderFormId=?";

	private String sqlSelectByUserOrderFormId = "select * from Mmall_Oms_OmsOrderForm where userOrderFormId=? and userId=?";

	private String getListByUserOrderFormId = "select * from Mmall_Oms_OmsOrderForm where userOrderFormId=?";

	private String getListByStateAndMinOrderId = "select * from Mmall_Oms_OmsOrderForm where omsOrderFormState=? and omsOrderFormId>? order by omsOrderFormId";

	@Override
	public boolean updateOrderFormState(long omsOrderFormId, OmsOrderFormState oldOmsOrderFormState,
			OmsOrderFormState newOmsOrderFormState) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateOrderFormState, newOmsOrderFormState.getIntValue(),
				omsOrderFormId, oldOmsOrderFormState.getIntValue()) > 0;
	}

	@Override
	public boolean updateOrderFormState(long omsOrderFormId, OmsOrderFormState newOmsOrderFormState) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateOrderFormStateWithoutCheck,
				newOmsOrderFormState.getIntValue(), omsOrderFormId) > 0;
	}

	@Override
	public boolean updateShipTimeAndState(long omsOrderFormId, long shipTime, OmsOrderFormState state) {
		return this.getSqlSupport().excuteUpdate(sqlUpdateOrderShipInfo, state.getIntValue(), shipTime, omsOrderFormId) > 0;
	}

	@Override
	public List<OmsOrderForm> getListByUserOrderFormId(long userOrderFormId, long userId) {
		return this.queryObjects(sqlSelectByUserOrderFormId, userOrderFormId, userId);
	}

	@Override
	public List<OmsOrderForm> getListByUserOrderFormId(long userOrderFormId) {
		return this.queryObjects(getListByUserOrderFormId, userOrderFormId);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.OmsOrderFormDao#getOmsOrderFormListByStateWithMinOrderId(long, com.xyl.mmall.oms.enums.OmsOrderFormState, int)
	 */
	@Override
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState omsOrderFormState, int limit) {
		StringBuilder sb = new StringBuilder(getListByStateAndMinOrderId);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), omsOrderFormState.getIntValue(), minOrderId);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.OmsOrderFormDao#getOmsOrderFormListByStateWithMinOrderId(long, com.xyl.mmall.oms.enums.OmsOrderFormState[], long[], int)
	 */
	@Override
	public List<OmsOrderForm> getOmsOrderFormListByStateWithMinOrderId(long minOrderId,
			OmsOrderFormState[] omsOrderFormStateArray, long[] orderTimeRange, int limit) {

		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamArray(sql, "omsOrderFormState", omsOrderFormStateArray);
		sql.append(" And omsOrderFormId>").append(minOrderId);
		sql.append(" AND CreateTime>=").append(orderTimeRange[0]).append(" AND CreateTime<").append(orderTimeRange[1]);
		this.appendOrderSql(sql, "omsOrderFormId", true);
		this.appendLimitSql(sql, limit, 0);
		return this.queryObjects(sql.toString());

	}
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.OmsOrderFormDao#getOmsOrderFormListByTimeRange(long, long)
	 */
	@Override
	public List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		sql.append(" AND CreateTime>").append(startTime).append(" AND CreateTime<=").append(endTime).append(" ");
		this.appendOrderSql(sql, "omsOrderFormId", true);
		return this.queryObjects(sql.toString());
	}
	
	@Override
	public OmsOrderForm getOmsOrderFormByOrderId(long omsOrderId) {
		StringBuilder sql = new StringBuilder();
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "omsOrderFormId", omsOrderId);
		return this.queryObject(sql.toString());
	}

	@Override
	public int getTotalOrderCountByTime(long beginTime, long endTime, List<Long> warehouseIdList) {
		if (warehouseIdList == null || (warehouseIdList != null && warehouseIdList.size() == 0) ) {
			return 0;
		}
		int count = 0;
		StringBuilder sql = new StringBuilder(256);
		sql.append("select count(omsOrderFormId) as count from Mmall_Oms_OmsOrderForm where createTime >=").append(beginTime)
		.append(" and createTime < ").append(endTime);
		StringBuilder dataSet = new StringBuilder(256);
		dataSet.append("(");
		for (Long id : warehouseIdList) {
			dataSet.append(id).append(",");
		}
		dataSet.deleteCharAt(dataSet.lastIndexOf(","));
		dataSet.append(")");
		sql.append(" and storeAreaId in ").append(dataSet);
		//.append(" and omsOrderFormState <> ").append(OmsOrderFormState.CANCEL.getIntValue())
		//.append(" and omsOrderFormState <> ").append(OmsOrderFormState.UNPICK_CANCEL.getIntValue());
		DBResource dbr = this.getSqlSupport().excuteQuery(sql.toString());
		ResultSet rs = dbr.getResultSet();
		try {
			while(rs.next()) {
				count = rs.getInt("count");
				break;
			}
		} catch (SQLException e) {
			return count;
		} finally {
			if (dbr != null)
				dbr.close();
		}
		return count;
	}
}
