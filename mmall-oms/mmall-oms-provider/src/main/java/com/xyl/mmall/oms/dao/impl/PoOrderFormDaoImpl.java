/**
 * PO单Dao操作
 */
package com.xyl.mmall.oms.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PoOrderFormDao;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.meta.PoOrderForm;

/**
 * @author hzzengdan
 *
 */
@Repository("PoOrderFormDao")
public class PoOrderFormDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PoOrderForm> implements PoOrderFormDao {

	private String tableName = this.getTableName();

	private String sqlSelectPoOrderList = "SELECT * FROM "
			+ tableName
			+ " WHERE poOrderId=? OR (createTime>=? AND createTime<=?) OR (startTime>=? AND startTime<=?) OR (endTime>=? AND endTime<=?)";

	private String sqlSelectPoOrerByMuli = "SELECT * FROM "
			+ tableName
			+ " WHERE (createTime>=? AND createTime<=?) AND (startTime>=? AND startTime<=?)  ";
	
	private String addPoOrderCommand = "update ".concat(tableName).concat(" SET command = command | ? WHERE PoOrderId = ? ");
	
	private String getNotGen2Return = "SELECT * FROM ".concat(tableName).concat(" WHERE command&?!=? and endTime+?<? ");

	/**
	 * 获取po单列表(jit模式)
	 * 
	 * @see com.xyl.mmall.oms.dao.PoOrderFormDao#getPoOrderList(java.lang.String,
	 *      long, long, long, long, long, long)
	 */
	@Override
	public List<PoOrderForm> getPoOrderList(String poOrderId, long createStartTime, long createEndTime,
			long openSaleStartTime, long openSaleEndTime, long stopSaleStartTime, long stopSaleEndTime) {

		return queryObjects(sqlSelectPoOrderList, poOrderId, createStartTime, createEndTime, openSaleStartTime,
				openSaleEndTime, stopSaleStartTime, stopSaleEndTime);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PoOrderFormDao#getPoOrderDetail(java.lang.String)
	 */
	@Override
	public PoOrderForm getPoOrderById(String poOrderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "poOrderId", poOrderId);
		return queryObject(sql);
	}

	@Override
	public List<PoOrderForm> getPickOrderListByMultiplePoOrder(String[] poOrderId, String createStartTime,
			String createEndTime, String openSaleStartTime, String openSaleEndTime,long supplierid) {
		String veryBeginningTime = OmsConstants.LONG_BEFORE;
		String veryFutrueTime = OmsConstants.LONG_AFTER;
		/**
		 * 如果时间选项为空，则对应的将开始时间赋值为很早以前，结束时间为很就以后,便于统一sql
		 */
		if (createStartTime == null || "".equals(createStartTime.trim()))
			createStartTime = veryBeginningTime;
		if (createEndTime == null || "".equals(createEndTime.trim()))
			createEndTime = veryFutrueTime;
		if (openSaleStartTime == null || "".equals(openSaleStartTime.trim()))
			openSaleStartTime = veryBeginningTime;
		if (openSaleEndTime == null || "".equals(openSaleEndTime.trim()))
			openSaleEndTime = veryFutrueTime;
		String sql = sqlSelectPoOrerByMuli;
		if (poOrderId != null && poOrderId.length > 0) {
			String subSql = "";
			for (String poId : poOrderId) {
				subSql += "'" + poId + "'" + ",";
			}
			sql = sql + " AND poOrderId in ( " + subSql.substring(0, subSql.length() - 1) + ")";
		}
		sql = sql + " AND supplierId=" + supplierid;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PoOrderForm> list = null;
		try {
			list = queryObjects(sql, sdf.parse(createStartTime).getTime(), sdf.parse(createEndTime).getTime(), sdf.parse(openSaleStartTime).getTime(), sdf.parse(openSaleEndTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.PoOrderFormDao#isNotGen2Return()
	 */
	@Override
	public List<PoOrderForm> getNotGen2ReturnAfterPoEnd(long incTime,long compareTime) {
		return queryObjects(getNotGen2Return, PoOrderForm.COMMAND_GEN2RETURN,PoOrderForm.COMMAND_GEN2RETURN,incTime,compareTime);
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.dao.PoOrderFormDao#addPoOrderCommand(long)
	 */
	@Override
	public boolean addPoOrderCommand(long poOrderId, long command) {
		return this.getSqlSupport().excuteUpdate(addPoOrderCommand, command,poOrderId) > 0;
	}

}
