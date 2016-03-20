/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.List;
import java.util.Set;

/**
 * @author hzlihui2014
 *
 */
public interface BubbleDao {

	/**
	 * 创建一个mmall
	 * 
	 * @param id
	 *            mmallID
	 * @param type
	 *            mmall类型
	 * @param url
	 *            页面url
	 * @return
	 */
	public boolean createBubble(String id, int type, String url);

	/**
	 * 查找一个mmall
	 * 
	 * @param id
	 *            mmallID
	 * @return
	 */
	public String findBubble(String id);

	/**
	 * 删除一个mmall
	 * 
	 * @param id
	 *            mmallID
	 * @return
	 */
	public boolean removeBubble(String id);

	/**
	 * 将一个mmall绑定给用户
	 * 
	 * @param id
	 * @param userId
	 * @param url
	 * @return
	 */
	public int bindOneBubbleToUser(long userId, int type, String url);

	/**
	 * 查找一个用户的mmall
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public int findUserBubble(long userId, int type);
	

	/**
	 * 查找一个用户找到的mmall的页面列表
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public Set<byte[]> findUserBubbleUrlSet(long userId, int type);

	/**
	 * 将指定个数mmall从用户记录中删除
	 * 
	 * @param id
	 * @param userId
	 * @param num
	 * @return
	 */
	public int deleteBubbleFromUser(long userId, int type, int num);

	/**
	 * 抢占一个彩色mmall
	 * 
	 * @return
	 */
	public int seizeOneColBubble();

	/**
	 * 释放一个彩色mmall
	 * 
	 * @return
	 */
	public int releaseOneColBubble();

	/**
	 * 抢占一个普通mmall优惠券
	 * 
	 * @return
	 */
	public int seizeOneBubbleCoupon();

	/**
	 * 释放一个普通mmall优惠券
	 * 
	 * @return
	 */
	public int releaseOneBubbleCoupon();

	/**
	 * 获取最新的mmall优惠券领取人的账号列表
	 * 
	 * @return
	 */
	public List<String> getLatestBubbleUserList(int limit);

	/**
	 * 添加一个mmall优惠券领取人的账号
	 * 
	 * @return
	 */
	public String addUserToBubbleCouponList(String userName);
	
	/**
	 * 查找用户最新的mmall时间。
	 * 
	 * @param userName
	 * @return
	 */
	public long findUserLastBubbleTime(String userName);
	
	/**
	 * 获取mmall出现的概率值
	 * 
	 * @param type
	 *            mmall类型
	 * @return
	 */
	public int getBubbleProbability(String type);
	
	/**
	 * 获取彩虹mmall的本日数值
	 * 
	 * @return
	 */
	public int getColBubbleNumOfToday();

	/**
	 * 获取mmall优惠券的本日数值
	 * 
	 * @return
	 */
	public int getBubbleCouponOfToday();
}
