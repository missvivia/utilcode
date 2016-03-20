package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderSku;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月15日 下午2:28:16
 * 
 */
public interface OrderSkuDao extends AbstractDao<OrderSku> {

	/**
	 * 读取某个订单id对应的全部OrderSku记录
	 * 
	 * @param orderId
	 * @param userId
	 * @param isOrder
	 *            是否和订单直接关联
	 * @return
	 */
	public List<OrderSku> getListByOrderId(long orderId, long userId, Boolean isOrder);
	
	/**
	 * 
	 * @param userId
	 * @param orderIdColl
	 * @return
	 */
	public List<OrderSku> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl);
	
	/**
	 * 读取某个包裹id对应的全部OrderSku记录
	 * 
	 * @param pacakgeId
	 * @param userId
	 * @param isOrder
	 *            是否和订单直接关联
	 * @return
	 */
	public List<OrderSku> getListByPackageId(long pacakgeId, long userId, Boolean isOrder);

	/**
	 * 将一批OrderSku列表,更新为同一个PackageId
	 * 
	 * @param objList
	 * @param packageId
	 * @return
	 */
	public boolean updatePackageId(List<OrderSku> objList, long packageId);

	/**
	 * 批量读取OrderSku记录集合
	 * 
	 * @param objList
	 * @return
	 */
	public List<OrderSku> getListByKeys(List<OrderSku> objList);
	
	/**
	 * 
	 * @param userId
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public OrderSku getOrderSkuByUserIdAndOrderIdAndSkuId(long userId, long orderId, long skuId);
	
	/**
	 * 
	 * 根据订单获取商品销售数量
	 * @param orderIdColl
	 * @return
	 */
	public Map<Long, Integer> getProductSaleNumMapByOrderIds(Collection<Long> orderIdColl);
}
