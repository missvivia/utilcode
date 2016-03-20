/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.meta.UserPoint;

/**
 * UserPointDao.java created by yydx811 at 2015年12月23日 下午12:49:50
 * 用户积分dao
 *
 * @author yydx811
 */
public interface UserPointDao extends AbstractDao<UserPoint> {

	/**
	 * 更新用户积分
	 * @param userPoint
	 * @return
	 */
	public boolean updateUserPoint(UserPoint userPoint);
	
	/**
	 * 获取用户积分
	 * @param userId
	 * @return
	 */
	public UserPoint getUserPointByUserId(long userId);
}
