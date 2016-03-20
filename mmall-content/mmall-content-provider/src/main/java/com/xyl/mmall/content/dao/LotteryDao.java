/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.Set;

/**
 * @author hzlihui2014
 *
 */
public interface LotteryDao {

	/**
	 * 获取用户的抽奖数
	 * 
	 * @param userId
	 * @return
	 */
	int findUserLottery(long userId);

	/**
	 * 增加一个用户的抽奖数
	 * 
	 * @param userId
	 * @return
	 */
	int addOneUserLottery(long userId);

	/**
	 * 消耗一个用户的抽奖数
	 * 
	 * @param userId
	 * @return
	 */
	int removeOneUserLottery(long userId);

	/**
	 * 抢占一个旅行箱
	 * 
	 * @return
	 */
	public int seizeOneSuitcase();

	/**
	 * 释放一个旅行箱
	 * 
	 * @return
	 */
	public int releaseOneSuitcase();

	/**
	 * 记录一个短信密钥
	 * 
	 * @param userIp
	 *            用户Ip
	 * @param phoneNum
	 *            用户手机号码
	 * @return
	 */
	public int createActivityMsgKey(String userIp, String phoneNum);

	/**
	 * 查询参加预热活动的手机号码
	 * 
	 * @return
	 */
	Set<byte[]> findActivityMsgPhoneSet(int index);

	///////////////////////////
	// 抽奖相关
	//////////////////////////
	
	/**
	 * 抽奖用户数+1
	 * @return
	 */
	int addOneLotteryCnt();
}
