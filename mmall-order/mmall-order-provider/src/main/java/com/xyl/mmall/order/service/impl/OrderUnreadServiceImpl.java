package com.xyl.mmall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.UnReadOrderDao;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.service.OrderUnreadService;

/**
 * @author hzjiangww
 * 
 */
@Service("orderUnreadService")
public class OrderUnreadServiceImpl implements OrderUnreadService {

	@Autowired
	UnReadOrderDao unReadOrderDao;

	@Autowired
	private OrderFormDao orderFormDao;

	@Override
	public int getUnReadOrderNumber(long userId, long time, OrderFormState[] stateArray,long[] orderTimeRange) {
		return orderFormDao.queryOrderFormCount(userId, true, stateArray,orderTimeRange);
		
	}

	@Override
	public boolean updateReadTime(long userId, int type) {
		return unReadOrderDao.updateTime(userId, type, System.currentTimeMillis());
	}

	@Override
	public long getUnReadTime(long userId, int type) {
		long time = unReadOrderDao.getLastReadTIme(userId, type);
		return time;
	}
}
