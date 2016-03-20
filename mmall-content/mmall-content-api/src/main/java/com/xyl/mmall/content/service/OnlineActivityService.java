/**
 * 
 */
package com.xyl.mmall.content.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xyl.mmall.content.dto.BubbleActivityDTO;
import com.xyl.mmall.content.dto.LotteryGiftDTO;
import com.xyl.mmall.content.dto.PresentProductDTO;
import com.xyl.mmall.content.meta.LotteryOrderCnt;

/**
 * @author hzlihui2014
 *
 */
public interface OnlineActivityService {

	/**
	 * 获取指定区域的展示商品列表。
	 * 
	 * @param saleAreaId
	 * @return
	 */
	List<PresentProductDTO> getPresentProductList(long saleAreaId);

	/**
	 * 为用户生成一个指定页面的mmall
	 * 
	 * @param userId
	 *            用户ID
	 * @param userName
	 *            用户名
	 * @param url
	 *            页面URL
	 * @return
	 */
	BubbleActivityDTO genBubble(long userId, String userName, String url);

	/**
	 * 获取用户的活动概况
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Object> getUserActivitySummary(long userId);

	/**
	 * 为指定用户戳破一个mmall
	 * 
	 * @param userId
	 * @param userName
	 * @param id
	 * @return
	 */
	Map<String, Object> knockBubble(long userId, String userName, String id);

	/**
	 * 获取最新的用户中奖纪录
	 * 
	 * @return
	 */
	List<LotteryGiftDTO> getLatestLotteryGiftList();

	/**
	 * 给用户一次抽奖的机会
	 * 
	 * @param userId
	 * @return
	 */
	int giveUserOneLettory(long userId);

	/**
	 * 尝试给用户一个旅行箱，并记录订单号
	 * 
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @return
	 */
	boolean giveUserOneSuitcase(long userId, String userName, long orderId);

	/**
	 * 创建一个短信发送的记录key
	 * 
	 * @param userIp
	 * @param phoneNum
	 * 
	 * @return
	 */
	int createSloganMessageKey(String userIp, String phoneNum);

	/**
	 * 获取参与预热的用户的手机号码
	 * 
	 * @param index
	 *            短信分区
	 * @return
	 */
	Set<byte[]> findActivityMsgPhoneSet(int index);

	// ///////////////////////////////
	// 抽奖相关
	// //////////////////////////////

	/**
	 * 用户抽奖机会-1
	 * 
	 * @param userId
	 * @return 返回当前用户还剩下的抽奖机会次数。返回-1表示一些异常情况发生。
	 */
	public int removeOneUserLottery(long userId);

	/**
	 * 保存用户中奖信息
	 * 
	 * @param gift
	 * @return
	 */
	boolean saveGiftHit(LotteryGiftDTO gift);

	/**
	 * 查询用户中奖信息
	 * 
	 * @param userId
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitByUserId(long userId);

	/**
	 * 查询用户某天的中奖信息
	 * 
	 * @param userId
	 * @param hitTime
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitByUserIdAndTime(long userId, long hitTime);

	/**
	 * 查询用户某几天的中奖信息
	 * 
	 * @param userId
	 * @param hitTimeStart
	 * @param hitTimeEnd
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitByUserIdAndTimeRange(long userId, long hitTimeStart, long hitTimeEnd);

	/**
	 * 查询所有中奖信息
	 * 
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitList();

	/**
	 * 查询某天所有中奖信息
	 * 
	 * @param hitTime
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitListByTime(long hitTime);

	/**
	 * 查询某几天所有中奖信息
	 * 
	 * @param hitTimeStart
	 * @param hitTimeEnd
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitListByTimeRange(long hitTimeStart, long hitTimeEnd);

	/**
	 * 抽奖用户数+1
	 * 
	 * @return
	 */
	int addOneLotteryCnt();

	/**
	 * 获取昨天某类奖品剩余数量
	 * 
	 * @param giftType
	 * @return
	 */
	Integer getYesterdayGiftLeft(int giftType);

	/**
	 * 获取当天某类奖品剩余数量
	 * 
	 * @param giftType
	 * @return
	 */
	Integer getCurrDateGiftLeft(int giftType);

	/**
	 * 某类奖品数量-1
	 * 
	 * @param curDateFlag
	 *            true-今天 false-昨天
	 * @param giftType
	 * @return
	 */
	boolean decCurrDateGiftCnt(boolean curDateFlag, int giftType);

	/**
	 * 获取每日预估订单数量列表
	 * 
	 * @return
	 */
	List<LotteryOrderCnt> getLotteryOrderCntList();

	/**
	 * 查找指定用户和订单是否有赠箱
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean isHasSuitcaseGift(long userId, long orderId);
	
	/**
	 * 查找mmall信息
	 * 
	 * @param id
	 *            mmallID
	 * @return
	 */
	String getBubbleInfo(String id);
	
	/**
	 * 查找用户mmall个数
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	int findUserBubble(long userId);

}
