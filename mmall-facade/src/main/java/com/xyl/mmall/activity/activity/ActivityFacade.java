package com.xyl.mmall.activity.activity;

/**
 * 
 * @author hzzhanghui
 *
 */
public interface ActivityFacade {

	/**
	 * Check whether user has already hit prize.
	 * 
	 * @param userId
	 * @param activityStartTime  Start time of activity
	 * @return
	 */
	boolean isUserAlreadHitPrize(long userId, long activityStartTime);
}
