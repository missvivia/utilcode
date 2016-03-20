/**
 * 
 */
package com.xyl.mmall.mainsite.facade;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xyl.mmall.content.dto.LotteryGiftDTO;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.vo.BubbleActivityVO;
import com.xyl.mmall.mainsite.vo.PresentProductVO;

/**
 * @author hzlihui2014
 *
 */
public interface OnlineActivityFacade {

	/**
	 * 生成一个mmall对象。
	 * 
	 * @param userId
	 *            用户Id
	 * @param userName
	 *            用户名
	 * @param url
	 *            请求页面URL
	 * @return
	 */
	BubbleActivityVO genBubble(long userId, String userName, String url);

	/**
	 * 获取首页商品展示的数据。
	 * 
	 * @param areaId
	 *            区域ID
	 * @return
	 */
	Map<Integer, List<PresentProductVO>> getPresentProductList(long areaId);

	/**
	 * 获取首页最新的中奖纪录。
	 * 
	 * @return
	 */
	List<LotteryGiftDTO> getLotteryGiftList();

	/**
	 * 发送指定类型的口号到用户的手机。
	 * 
	 * @param type
	 *            口号类型
	 * @param phoneNum
	 *            手机号码
	 * @param userIp
	 *            用户IP
	 * @return
	 */
	BaseJsonVO sendSloganMessage(String type, String phoneNum, String userIp);

	/**
	 * 校验口号。
	 * 
	 * @param msg
	 * @return
	 */
	BaseJsonVO verifySlogan(String msg);

	/**
	 * 为用户绑定优惠券礼包。
	 * 
	 * @param userId
	 *            用户ID
	 * @param key
	 *            校验密钥
	 * @return
	 */
	BaseJsonVO bindCouponPack(long userId, String key);
	
	/**
	 * 为用户绑定优惠券礼包。无需校验。
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	boolean bindCouponPack(long userId);

	/**
	 * 获取活动的概况。
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	BaseJsonVO getActivitySummary(long userId);

	/**
	 * 用户戳破一个mmall。
	 * 
	 * @param userId
	 *            用户ID
	 * @param userName
	 *            用户名
	 * @param id
	 *            mmallID
	 * @return
	 */
	BaseJsonVO knockBubble(long userId, String userName, String id);

	/**
	 * 给用户一次抽奖的机会
	 * 
	 * @param userId
	 * @return
	 */
	int giveUserOneLettory(long userId);

	/**
	 * 给用户一个旅行箱
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	boolean giveUserOneSuitcase(long userId, long orderId);

	/**
	 * 发送线上活动短信
	 * 
	 * @return
	 */
	boolean sendOnlineActivitySms();

	/**
	 * 获取线上活动短信的手机号码
	 * 
	 * @return
	 */
	Set<byte[]> getOnlineActivitySms(int i);

	/**
	 * 查询用户中奖信息
	 * 
	 * @param userId
	 * @return
	 */
	List<LotteryGiftDTO> getGiftHitByUserId(long userId);
	
	/**
	 * 用户抽奖处理
	 * 
	 * @param userId
	 * @return
	 */
	BaseJsonVO userLottery(long userId, String userName);
	
	/**
	 * 用户抽奖机会-1
	 * 
	 * @param userId
	 * @return 返回当前用户还剩下的抽奖机会次数。返回-1表示一些异常情况发生。
	 */
	int removeOneUserLottery(long userId);

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
	 * @return
	 */
	int addOneLotteryCnt();

	/**
	 * 获取昨天某类奖品剩余数量
	 * @param giftType
	 * @return
	 */
	Integer getYesterdayGiftLeft(int giftType);
	
	/**
	 * 获取当天某类奖品剩余数量
	 * @param giftType
	 * @return
	 */
	Integer getCurrDateGiftLeft(int giftType);
	
	/**
	 * 某类奖品数量-1
	 * @param curDateFlag true-今天 false-昨天
	 * @param giftType
	 * @return
	 */
	boolean decCurrDateGiftCnt(boolean curDateFlag, int giftType);

	/**
	 * 查询是否用户在指定订单中获取了旅行箱。
	 * 
	 * @param userId
	 *            用户id
	 * @param orderId
	 *            订单id
	 * @return
	 */
	boolean isHasSuitcaseGift(long userId, long orderId);
	
}
