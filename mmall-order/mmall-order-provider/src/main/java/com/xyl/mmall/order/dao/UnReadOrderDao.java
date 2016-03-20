package com.xyl.mmall.order.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.meta.UnReadOrderList;

/**
 * @author hzjiangww
 *
 */
public interface UnReadOrderDao extends AbstractDao<UnReadOrderList> {

	/**
	 * 获得最后一次阅读的时间
	 * @param userId
	 * @param type
	 * @return
	 */
	public long getLastReadTIme(long userId, int type);
	
	/**
	 * 更新阅读时间
	 * @param userId
	 * @param type
	 * @param time
	 * @return
	 */
	public boolean updateTime(long userId,int type,long time);
}
