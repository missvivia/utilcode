package com.xyl.mmall.order.service;

import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 
 * @author hzjiangww
 * 
 */
public interface OrderUnreadService {

	/**
	 * @param userId
	 * @param type
	 * @return
	 */
	public int getUnReadOrderNumber(long userId,long time, OrderFormState[] stateArray,long[] orderTimeRange);

	/**
	 * @param userId
	 * @param type
	 * @return
	 */
	public long getUnReadTime(long userId, int type);
	/**
	 * @param userId
	 * @param type
	 * @return
	 */
	public boolean updateReadTime(long userId, int type);

}
