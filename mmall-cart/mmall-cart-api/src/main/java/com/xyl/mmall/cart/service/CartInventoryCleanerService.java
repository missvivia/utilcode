package com.xyl.mmall.cart.service;


/**
 * 
 * 购物车过期数据清理接口
 */
public interface CartInventoryCleanerService {
	
	/**
	 * job任务获取之前处理过的位置及当前需要处理的区域位置
	 * @param areaId
	 * @return 数组位置0表示之前处理过的位置,位置1表示当前需要处理的位置
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
	 * @return
	 */
	public boolean cleanOverTimeCartForJob(int areaId,int posProcessedByCurJob);
	
	/**
	 * 将当前job处理过的区域的位置置为成功
	 * @param cleanSuccessFlag 1:成功 2:失败
	 * @param areaId
	 * @param posProcessedByCurJob 当前job处理过的位置，并且希望将期置为true
	 * @param oldPoint
	 * @return
	 */
	public boolean pointFlagToSuccessOrFail(boolean cleanSuccessFlag,int areaId,int posProcessedByCurJob,int oldPoint);
	
}
