package com.xyl.mmall.cart.service.impl;

import java.util.Date;

/**
 * 购物车清除操作接口
 * 只负责将有效队列中，超过20分钟的sku移除到失败队列
 * <p>
 * 含delete与20分钟失效功能
 * @author hzzhaozhenzuo
 *
 */
public interface CartCleanCacheOperInf {
	
	/**
	 * 每次用户往购物车放入数据时，都需要往cache中保存用户购物车对应的有效时间
	 * @param userId 用户id
	 * @param areaId 省区域id 
	 * @param updateTime 当前用户更新时间
	 * @return
	 */
	public boolean addCartUpdateTimeToCache(long userId,int areaId,Date updateTime);
	
	/**
	 * job任务获取当前可以处理的区域位置
	 * @param areaId
	 * @return
	 */
	public int[] getPositionShouldProcessedByCurrentJob(int areaId);
	
	/**
	 * 设置区域位置，表明该区域由job调用方来处理
	 * @param areaId
	 * @param posProcessedByCurJob
	 * @param oldPoint
	 * @return
	 */
	public boolean setUpPoint(int areaId,int posProcessedByCurJob,int oldPoint);
	
	/**
	 * 为job定时清理提供的接口
	 * 这里的areaId为之后分区域分布式清除作预留
	 * @param areaId
	 * @param posProcessedByCurJob
	 * @return
	 */
	public boolean cleanOverTimeCartForJob(int areaId,int posProcessedByCurJob);
	
	/**
	 * 将当前job处理过的区域的位置置为成功
	 * @param cleanSuccessFlag 1:成功 2:失败
	 * @param areaId
	 * @param distributeProces
	 * @param posProcessedByCurJob 当前job处理过的位置，并且希望将期置为true
	 * @param oldPoint
	 * @return
	 */
	public boolean pointFlagToSuccessOrFail(boolean cleanSuccessFlag,int areaId,int posProcessedByCurJob,int oldPoint);
}
