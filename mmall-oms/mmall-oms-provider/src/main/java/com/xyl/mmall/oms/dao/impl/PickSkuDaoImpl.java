/**
 * 
 */
package com.xyl.mmall.oms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * @author hzzengdan
 * @date 2014-09-17
 */
@Repository("PickSkuDao")
public class PickSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<PickSkuItemForm> implements PickSkuDao {

	private String tableName = this.getTableName();

	private String sql_get_pickSku_by_poOrderId = "SELECT * FROM " + tableName
			+ " where poOrderId=? and pickTime>=? and pickTime<=? ";

	private String sql_get_unPickSku_by_poOrder = "SELECT * FROM " + tableName + " where poOrderId=? and pickOrderId=?";

	private String sql_get_PickSku_by_poOrder = "SELECT * FROM " + tableName + " where poOrderId=? and supplierId=?";

	private String sql_update_pickSku_type_by_pickOrderId = "UPDATE " + tableName
			+ " SET pickStates=? where pickOrderId=?";

	private String sql_delete_unPickSku_omsOrderFormId = "delete from " + tableName
			+ " where omsOrderFormId=? and pickOrderId='0'";

	private String sqlUpdatePickSku = "update Mmall_Oms_PickSkuItem set pickOrderId=? ,pickTime=?  where id=? and pickOrderId='0'";

	private String sqlSelectBySupplierId = "select * from Mmall_Oms_PickSkuItem where supplierId=? and pickOrderId='0'";

	private String sqlSelectUnPickByCreateTime = "select * from Mmall_Oms_PickSkuItem where createTime<? and pickOrderId='0' order by createTime desc";

	private String getByOmsOrderFormId = "select * from Mmall_Oms_PickSkuItem where omsOrderFormId=?";

	private String sqlSelectByPoOrderIdAndOriSupplierId = "select * from Mmall_Oms_PickSkuItem where poOrderId=? and orisupplierId=?";

	private String sqlSelectOmsOrderFormIdInPo = "select distinct(omsorderformid) as omsorderformid from Mmall_Oms_PickSkuItem where poOrderId=?";

	/**
	 * 获取拣货单sku详情
	 * 
	 * @see com.xyl.mmall.oms.dao.PickSkuDao#getPickSkuList(java.lang.String)
	 */
	@Override
	public List<PickSkuItemForm> getPickSkuList(String pickOrderId, long supplierId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "pickOrderId", pickOrderId);
		SqlGenUtil.appendExtParamObject(sql, "supplierId", supplierId);
		return queryObjects(sql);
	}

	@Override
	public PickSkuItemForm getShipSkuBySkuIdAndPoId(String skuId, String poId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "skuId", skuId);
		SqlGenUtil.appendExtParamObject(sql, "poOrderId", poId);
		return this.queryObject(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickSkuDao#getPickSkuListByPoOrder(java.lang.String)
	 */
	@Override
	public List<PickSkuItemForm> getPickSkuListByPoOrder(String poOrderId, String pickOrderId, String pickStartTime,
			String pickEndTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PickSkuItemForm> list = null;
		try {
			if (pickOrderId != null && !"".equals(pickOrderId.trim())) {
				String sql = sql_get_pickSku_by_poOrderId + " and pickOrderId=?";
				list = queryObjects(sql, poOrderId, pickStartTime, pickEndTime, pickOrderId);
			} else {
				list = queryObjects(sql_get_pickSku_by_poOrderId, poOrderId, sdf.parse(pickStartTime).getTime(), sdf
						.parse(pickEndTime).getTime());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<PickSkuItemForm> getPickSkuByPoOrderId(String poOrderId, long supplierId) {
		return queryObjects(sql_get_PickSku_by_poOrder, poOrderId, supplierId);
	}

	@Override
	public List<PickSkuItemForm> getUnPickSkuByPoOrderId(String poOrderId) {
		return queryObjects(sql_get_unPickSku_by_poOrder, poOrderId, "0");
	}

	@Override
	public boolean updatePickSkuState(String pickOrderId, PickStateType type) {
		return this.getSqlSupport().excuteUpdate(sql_update_pickSku_type_by_pickOrderId, type.getIntValue(),
				pickOrderId) > 0;
	}

	@Override
	public boolean deleteUnPickSkuByOmsOrderFormId(long omsOrderFormId) {
		return this.getSqlSupport().excuteUpdate(sql_delete_unPickSku_omsOrderFormId, omsOrderFormId) > 0;
	}

	@Override
	public List<PickSkuItemForm> getUnPickSkuByPoSupplierId(long supplierId) {
		return this.queryObjects(sqlSelectBySupplierId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.PickSkuDao#updateUnPickSkuStateById(long,
	 *      java.lang.String, long)
	 */
	@Override
	public boolean updateUnPickSkuStateById(long id, String pickOrderId, long pickTime) {
		return this.getSqlSupport().excuteUpdate(sqlUpdatePickSku, pickOrderId, pickTime, id) > 0;
	}

	@Override
	public List<PickSkuItemForm> getUnPickListByCreateTime(long createTime, int limit) {
		StringBuilder sb = new StringBuilder(sqlSelectUnPickByCreateTime);
		this.appendLimitSql(sb, limit, 0);
		return this.queryObjects(sb.toString(), createTime);
	}

	@Override
	public List<PickSkuItemForm> getByOmsOrderFormId(long omsOrderFormId) {
		return this.queryObjects(getByOmsOrderFormId, omsOrderFormId);
	}

	@Override
	public List<PickSkuItemForm> getPickSkuByPoOrderIdAndOriSupplierId(String poOrderId, long supplierId) {
		return this.queryObjects(sqlSelectByPoOrderIdAndOriSupplierId, poOrderId, supplierId);
	}

	@Override
	public List<Long> getOmsOrderFormIdListInPo(long poId) {
		DBResource resource = getSqlSupport().excuteQuery(sqlSelectOmsOrderFormIdInPo, poId);
		ResultSet rs = resource.getResultSet();
		List<Long> omsOrderFormIdList = new ArrayList<Long>();
		try {
			while (rs.next()) {
				Long orderFormId = rs.getLong(1);
				omsOrderFormIdList.add(orderFormId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return omsOrderFormIdList;
	}

}
