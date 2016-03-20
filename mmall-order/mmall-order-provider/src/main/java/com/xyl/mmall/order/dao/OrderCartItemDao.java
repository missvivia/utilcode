package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderCartItem;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月12日 上午9:49:14
 * 
 */
public interface OrderCartItemDao extends AbstractDao<OrderCartItem> {

	/**
	 * 读取某个订单下的OrderCartItem记录集合
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<OrderCartItem> getListByOrderId(long userId, long orderId);

	/**
	 * 
	 * @param userId
	 * @param orderIdColl
	 * @return
	 */
	public List<OrderCartItem> getListByOrderIdsAndUserId(long userId, Collection<Long> orderIdColl);

	/**
	 * 读取某个包裹下的OrderCartItem记录集合
	 * 
	 * @param userId
	 * @param ordPkgId
	 * @return
	 */
	public List<OrderCartItem> getListByOrderPackageId(long userId, long ordPkgId);

	/**
	 * 将一批OrderCartItem列表,更新为同一个PackageId
	 * 
	 * @param objList
	 * @param packageId
	 * @return
	 */
	public boolean updatePackageId(List<OrderCartItem> objList, long packageId);

	/**
	 * 获得某个订单下的PackageId==0的记录数
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public int getCountOfZeroPackageId(long userId, long orderId);

	/**
	 * 获得某个订单下的PackageId>0的记录数
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public int getCountOfUnZeroPackageId(long userId, long orderId);
}
