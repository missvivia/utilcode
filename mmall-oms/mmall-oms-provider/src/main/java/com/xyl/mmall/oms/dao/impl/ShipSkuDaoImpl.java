package com.xyl.mmall.oms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;

/**
 * @author zb
 *
 */
@Repository("ShipSkuDao")
public class ShipSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<ShipSkuItemForm> implements ShipSkuDao {
	private String tableName = this.getTableName();

	private String sql_get_total_count_by_shipOrderId = "SELECT sum(skuQuantity) FROM " + tableName
			+ " WHERE shipOrderId=? and supplierId=?";

	private String sql_total_type_of_sku = "SELECT  COUNT(DISTINCT skuId) FROM " + tableName
			+ " WHERE shipOrderId=? and supplierId=?";
	
	private String sql_total_arrive_sku_count_by_poOrderId = "SELECT SUM(arrivedQuantity) FROM " + tableName
			+ " WHERE poOrderId=? and supplierId=?";

	private String sqlQueryByPoOrderId = "select * from Mmall_Oms_ShipSkuItem where poOrderId=? and supplierId=?";

	private String sqlQueryNormalSkuCountInPo = "SELECT skuid,SUM(arrivedNormalCount) as count FROM Mmall_Oms_ShipSkuItem WHERE poorderid=? GROUP BY skuid";

	/**
	 * 获取发货单的sku信息
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getShipSkuList(java.lang.String)
	 */
	@Override
	public List<ShipSkuItemForm> getShipSkuList(String shipOrderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "shipOrderId", shipOrderId);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getShipSkuListByPoOrderId(java.lang.String)
	 */
	@Override
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(String poOrderId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "poOrderId", poOrderId);
		return this.queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getTotalCountByShipOrderId(java.lang.String)
	 */
	@Override
	public int getTotalCountByShipOrderId(String shipOrderId, long supplierId) {
		return this.getSqlSupport().queryCount(sql_get_total_count_by_shipOrderId, shipOrderId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getTotalSkuTypeByShipOrderId(java.lang.String,
	 *      long)
	 */
	@Override
	public int getTotalSkuTypeByShipOrderId(String shipOrderId, long supplierId) {
		return this.getSqlSupport().queryCount(sql_total_type_of_sku, shipOrderId, supplierId);
	}

	@Override
	public boolean updateWarehoureInfo(ShipSkuItemForm shipSku) {
		Set<String> fieldNameSetOfUpdate = new HashSet<>();
		fieldNameSetOfUpdate.add("arrivedQuantity");
		fieldNameSetOfUpdate.add("arrivedDefectiveCount");
		fieldNameSetOfUpdate.add("arrivedNormalCount");
		fieldNameSetOfUpdate.add("shipStates");

		Set<String> fieldNameSetOfWhere = new HashSet<>();
		fieldNameSetOfWhere.add("skuId");
		fieldNameSetOfWhere.add("shipOrderId");

		return SqlGenUtil.update(getTableName(), fieldNameSetOfUpdate, fieldNameSetOfWhere, shipSku, getSqlSupport());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getShipSkuListByPoOrderId(long,
	 *      long)
	 */
	@Override
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(long poOrderId, long supplierId) {
		return this.queryObjects(sqlQueryByPoOrderId, poOrderId, supplierId);
	}

	@Override
	public Map<Long, Integer> getArrivedNormalCountBySkuInPo(long poOrderId) {
		DBResource resource = getSqlSupport().excuteQuery(sqlQueryNormalSkuCountInPo, poOrderId);
		ResultSet rs = resource.getResultSet();
		Map<Long, Integer> skuCountMap = new LinkedHashMap<Long, Integer>();
		try {
			while (rs.next()) {
				Long skuId = rs.getLong(1);
				Integer count = rs.getInt(2);
				skuCountMap.put(skuId, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resource.close();
		}
		return skuCountMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.dao.ShipSkuDao#getTotalArrivedByPoOrderId(java.lang.String)
	 */
	@Override
	public int getTotalArrivedByPoOrderId(String poOrderId, long supplierId) {
		return this.getSqlSupport().queryCount(sql_total_arrive_sku_count_by_poOrderId, poOrderId, supplierId);
	}

}
