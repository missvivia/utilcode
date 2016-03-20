/**
 * 
 */
package com.xyl.mmall.content.constants;

import java.math.BigDecimal;

/**
 * @author hzlihui2014
 *
 */
public class OnlineActivityConstants {

	// 开始时间：2015-01-16 10:00:00 - 1421373600000L
	// 测试时间：2015-01-09 00:00:00 - 1420732800000L
	public static long ACTIVITY_START_TIME = 1421373600000L;

	// 结束时间：2015-01-27 00:00:00 - 1422288000000L
	// 测试时间：2015-01-16 00:00:00 - 1421337600000L
	public static long ACTIVITY_END_TIME = 1422288000000L;

	// 结束时间：2015-01-27 00:00:00 - 1422288000000L
	public static long ACTIVITY_V2_START_TIME = 1422288000000L;
	
	// 结束时间：2015-03-01 00:00:00 - 1425139200000L
	public static long ACTIVITY_V2_END_TIME = 1425139200000L;
	
	// 预热结束时间：2015-01-16 10:00:00 - 1421373600000L
	// 测试时间：2015-01-09 00:00:00 - 1420732800000L
	public static long PREHEAT_END_TIME = 1421373600000L;

	// 每个时段18个彩虹mmall
	public static int COLORFUL_BUBBLE_PER_TIME = 18;

	// 预热口号加密密钥
	public static String PREHEAT_KEY = "PreheatKey";

	// 普通mmall活动时段 10, 12, 14, 16, 18, 20
	// 测试mmall活动时段 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
	public static int[] ORD_BUBBLE_ACTIVITY_TIME = { 10, 12, 14, 16, 18, 20 };

	// 彩虹mmall活动时段 12, 18
	// 测试mmall活动时段 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
	public static int[] COL_BUBBLE_ACTIVITY_TIME = { 12, 18 };

	// 满赠活动的金额
	public static BigDecimal SUIT_CASE_GIFT_ORDER_AMOUNT = new BigDecimal(1288);

	// 满赠活动的每日赠品数目
	public static int SUIT_CASE_COUNT_PER_TIME = 58;

	// mmall优惠券的每日发放数目
	public static int BUBBLE_COUPON_COUNT_PER_TIME = 5000;

	// 上线活动短信模板
	public static String ONLIE_ACTIVITY_MESSAGE = "1月16日mmall已华丽变身，快登入mmall使用暗语来兑换你的新宠大礼，还有更多惊喜等你来发现【023.baiwandian.cn】";

	// 获赠旅行箱短信模板
	public static String SUITCASE_GIFT_MESSAGE = "哇，买了那么多新衣服你肯定少了一个漂亮的旅行箱。恭喜你获得“网易登机箱”一个。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	// mmall优惠券中间轮播数量
	public static int MAX_BUBBLE_COUPON_USER_DISPLAY_COUNT = 100;

	// mmall寻找活动的每时段时长分钟数
	public static int FIND_BUBBLE_MIN_TIME = 1;
	
	// mmall寻找活动的每时段时长秒数
	public static int FIND_BUBBLE_SECOND_TIME = 30;

	// 彩虹mmall奖品每日分布
	public static int[] COL_BUBBLE_GIFT_DISTRIBUTION = { 15, 18, 21, 26, 36, 72, 108, 144, 180, 216, 252 };

	// 旅行箱奖品每日分布
	public static int[] SUITCASE_GIFT_DISTRIBUTION = { 20, 25, 30, 40, 58, 116, 174, 232, 290, 348, 406 };

	// 彩虹mmall出现几率
	public static int COL_BUBBLE_PROBABILITY = 5;

	// 普通mmall出现几率
	public static int ORD_BUBBLE_PROBABILITY = 6;

	// 第二张优惠券的普通mmall出现几率
	public static int SECOND_ORD_BUBBLE_PROBABILITY = 3;

	// 彩泡最大SKU单价
	public static BigDecimal MAX_COL_BUBBLE_SKU_PRICE = new BigDecimal(600);
}
