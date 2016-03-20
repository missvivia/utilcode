package com.xyl.mmall.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.dbsupport.DBResource;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.dao.PolicyObjectDaoSqlBaseOfAutowired;
import com.xyl.mmall.order.meta.OrderSku;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月15日 下午2:30:37
 * 
 */
@Repository
public class OrderSkuDaoImpl extends PolicyObjectDaoSqlBaseOfAutowired<OrderSku> implements OrderSkuDao {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuDao#getListByOrderIdsAndUserId(long,
	 *      java.util.Collection)
	 */
	public List<OrderSku> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl) {
		StringBuilder sql = new StringBuilder(64);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuDao#getListByOrderId(long,
	 *      long, java.lang.Boolean)
	 */
	public List<OrderSku> getListByOrderId(long orderId, long userId, Boolean isOrder) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "orderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isOrder", isOrder);
		return queryObjects(sql);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuDao#getListByPackageId(long,
	 *      long, java.lang.Boolean)
	 */
	@Override
	public List<OrderSku> getListByPackageId(long pacakgeId, long userId, Boolean isOrder) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "packageId", pacakgeId);
		SqlGenUtil.appendExtParamObject(sql, "userId", userId);
		SqlGenUtil.appendExtParamObject(sql, "isOrder", isOrder);
		return queryObjects(sql);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuDao#updatePackageId(java.util.List,
	 *      long)
	 */
	public boolean updatePackageId(List<OrderSku> objList, long packageId) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("UPDATE ").append(getTableName()).append(" SET PackageId=").append(packageId).append(" WHERE 1=0 ");
		for (OrderSku obj : objList) {
			sql.append(" OR ( ");
			SqlGenUtil.appendExtParamObject(sql, "id", obj.getId());
			SqlGenUtil.appendExtParamObject(sql, "userId", obj.getUserId());
			sql.append(" )");
		}
		return getSqlSupport().updateRecord(sql.toString());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.dao.OrderSkuDao#getListByKeys(java.util.List)
	 */
	public List<OrderSku> getListByKeys(List<OrderSku> objList) {
		StringBuilder sql = new StringBuilder(256);
		sql.append("SELECT * FROM ").append(getTableName()).append(" WHERE 1=0 ");
		for (OrderSku obj : objList) {
			sql.append(" OR ( ");
			SqlGenUtil.appendExtParamObject(sql, "id", obj.getId());
			SqlGenUtil.appendExtParamObject(sql, "userId", obj.getUserId());
			sql.append(" )");
		}
		return queryObjects(sql);
	}

	@Override
	public OrderSku getOrderSkuByUserIdAndOrderIdAndSkuId(long userId, long orderId, long skuId) {
		StringBuilder sql = new StringBuilder(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, "UserId", userId);
		SqlGenUtil.appendExtParamObject(sql, "OrderId", orderId);
		SqlGenUtil.appendExtParamObject(sql, "SkuId", skuId);
		
		return this.queryObject(sql.toString());
	}
	
	public Map<Long, Integer> getProductSaleNumMapByOrderIds(
			Collection<Long> orderIdColl) {
		 Map<Long, Integer> reslutMap = new HashMap<Long, Integer>();
		 StringBuilder sql = new StringBuilder(64);
		 sql.append("select count(TotalCount) as count ,skuId from Mmall_Order_OrderSku where 1=1 ");
		 SqlGenUtil.appendExtParamColl(sql, "orderId", orderIdColl);
		 sql.append(" group by SkuId ");
		 DBResource resource = getSqlSupport().excuteQuery(sql.toString());
         ResultSet rs = resource.getResultSet();
         try {
             while(rs != null && rs.next()){
            	 reslutMap.put(rs.getLong("skuId"), rs.getInt("count"));
             }
         } catch (SQLException e ) {
                 e.printStackTrace();
         } finally {
                 resource.close();
         }
		 return reslutMap;
	}
}
