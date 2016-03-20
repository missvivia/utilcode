/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.content.meta.SuitcaseGift;

/**
 * @author hzlihui2014
 *
 */
public interface SuitcaseGiftDao extends AbstractDao<SuitcaseGift> {

	/**
	 * 根据用户ID和订单ID查找用户是否有赠箱。
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	List<SuitcaseGift> findByUserIdAndOrderId(long userId, long orderId);
}
