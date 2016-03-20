package com.xyl.mmall.order.dao;

import java.util.Collection;
import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.OrderSkuCartItem;

/**
 * @author dingmingliang
 * 
 */
public interface OrderSkuCartItemDao extends AbstractDao<OrderSkuCartItem> {

	/**
	 * 根据orderCartItemId,读取相关的OrderSkuCartItem对象
	 * 
	 * @param userId
	 * @param cartId
	 * @return
	 */
	public OrderSkuCartItem getObjectByCartId(long userId, long cartId);

	/**
	 * 根据orderCartItemId,读取相关的OrderSkuCartItem对象
	 * 
	 * @param userId
	 * @param orderCartItemIdColl
	 * @return
	 */
	public List<OrderSkuCartItem> getListByOrderCartItemIdsAndUserId(long userId, Collection<Long> orderCartItemIdColl);
}
