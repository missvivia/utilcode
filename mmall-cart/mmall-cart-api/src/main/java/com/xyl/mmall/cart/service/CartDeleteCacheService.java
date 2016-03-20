package com.xyl.mmall.cart.service;

import java.util.Date;

/**
 * 购物车定时删除接口
 * @author hzzhaozhenzuo
 *
 */
public interface CartDeleteCacheService {

	/**
	 * 将某个sku加入到delete清除区域
	 * 
	 * @param type 为null时，存储只按区域分组
	 * @param userId
	 * @param areaId
	 * @param addDate
	 * @return
	 */
	public boolean addCartItemToDeleteCache(String type, long userId, int areaId, Date addDate);

	/**
	 * 删除某个区域中某个非有效队列数据
	 * 
	 * @param areaId
	 *            区域id
	 * @param type
	 *            类型，如：超时队列,失效队列等
	 * @return
	 */
	public boolean deleteCartItemShouldRemove(int areaId, String type);

}
