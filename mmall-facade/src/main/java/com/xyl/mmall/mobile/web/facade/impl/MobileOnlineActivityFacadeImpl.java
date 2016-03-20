/**
 * 
 */
package com.xyl.mmall.mobile.web.facade.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.content.dto.BubbleActivityDTO;
import com.xyl.mmall.content.dto.LotteryGiftDTO;
import com.xyl.mmall.content.dto.PresentProductDTO;
import com.xyl.mmall.content.enums.GiftType;
import com.xyl.mmall.content.enums.PreheatSlogan;
import com.xyl.mmall.content.meta.LotteryOrderCnt;
import com.xyl.mmall.content.service.OnlineActivityService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.mobile.web.facade.MobileOnlineActivityFacade;
import com.xyl.mmall.mobile.web.vo.BubbleActivityVO;
import com.xyl.mmall.mobile.web.vo.LotteryGiftVO;
import com.xyl.mmall.mobile.web.vo.PresentProductVO;
import com.xyl.mmall.order.dto.OrderFormBrief2DTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.security.utils.DigestUtils;

/**
 * @author hzlihui2014
 *
 */
@Facade("mobileOnlineActivityFacade")
public class MobileOnlineActivityFacadeImpl implements MobileOnlineActivityFacade {

	private static final String COL_BUBBLE_MSG = "恭喜你捕捉到了最稀有的彩虹mmall！您已获得一次免单特权！mmall客服将会尽快和你确认相关中奖信息。396个免单召唤更多小伙伴来抢吧~";
	
	@Autowired
	private OnlineActivityService onlineActivityService;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private MessagePushFacade messagePushFacade;

	@Autowired
	private OrderBriefService orderBriefService;

	@Value("${pack.coupon.code}")
	private String packCouponCode;

	@Value("${pack.coupon.v2.code}")
	private String packCouponV2Code;
	
	@Value("${bubble.coupon.code}")
	private String bubbleCouponCode;
	
	@Value("${lottery.coupon.code}")
	private String lotteryCouponCode;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#genBubble(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public BubbleActivityVO genBubble(long userId, String userName, String url) {
		// 先生成一个mmall
		BubbleActivityDTO dto = onlineActivityService.genBubble(userId, userName, url);
		// 检查是否要显示给用户
		return dto == null || unluckyColBubble(dto, userId) ? null : new BubbleActivityVO(dto);
	}

	/**
	 * 判断是否在未登录或者未提交订单的时候生成了一个彩虹mmall
	 * 
	 * @param dto
	 * @param userId
	 * @return
	 */
	private boolean unluckyColBubble(BubbleActivityDTO dto, long userId) {
		// 拿到一个彩虹mmall，但用户没登录，或者没有成功购买过商品
		return dto.getType() == 1 && (userId < 0 || !hasActivityOrder(userId));
	}

	/**
	 * 检查用户在活动期间是否有下过订单
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean hasActivityOrder(long userId) {
		long[] orderTimeRange = { OnlineActivityConstants.ACTIVITY_START_TIME, System.currentTimeMillis() };
		RetArg retArg = orderBriefService.queryOrderFormBriefDTOListByStateWithUserId(userId, null, orderTimeRange,
				new DDBParam());
		List<OrderFormBriefDTO> orderFormBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
		if (CollectionUtils.isNotEmpty(orderFormBriefDTOList)) {
			for (OrderFormBriefDTO order : orderFormBriefDTOList) {
				if (order.getPayState() == PayState.ONLINE_PAYED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#getPresentProductList(long)
	 */
	@Override
	public Map<Integer, List<PresentProductVO>> getPresentProductList(long areaId) {
		List<PresentProductDTO> productList = onlineActivityService.getPresentProductList(areaId);
		Map<Integer, List<PresentProductVO>> resultMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(productList)) {
			for (PresentProductDTO product : productList) {
				if (resultMap.containsKey(product.getCategory())) {
					resultMap.get(product.getCategory()).add(new PresentProductVO(product));
				} else {
					List<PresentProductVO> newList = new ArrayList<>();
					newList.add(new PresentProductVO(product));
					resultMap.put(product.getCategory(), newList);
				}
			}
		}
		return resultMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#sendSloganMessage(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public BaseJsonVO sendSloganMessage(String type, String phoneNum, String userIp) {
		BaseJsonVO result = new BaseJsonVO();
		// 校验手机号码
		if (StringUtils.isEmpty(phoneNum) || !(phoneNum.length() == 11 && phoneNum.startsWith("1"))) {
			result.setCode(400);
			result.setMessage("请输入有效的手机号码！");
			return result;
		}
		// 校验口号类型
		PreheatSlogan userSlogan = getPreheatSloganDesc(type);
		if (userSlogan == PreheatSlogan.NULL) {
			result.setCode(400);
			result.setMessage("无效的口号类型！");
			return result;
		}
		int msgCount = onlineActivityService.createSloganMessageKey(userIp, phoneNum);
		if (msgCount < 0) {
			result.setCode(400);
			result.setMessage("短信发送失败，请稍后重试！");
		} else if (msgCount > 60) {
			result.setCode(400);
			result.setMessage("您的短信发送次数过多，请稍后重试！");
		} else {
			try {
				messagePushFacade.sendSms(phoneNum, buildSms(userSlogan.getDesc()));
				result.setCode(ErrorCode.SUCCESS);
			} catch (Exception e) {
				result.setCode(400);
				result.setMessage("短信发送失败，请稍后重试！");
			}
		}
		return result;
	}

	/**
	 * 组装短信内容
	 * 
	 * @param desc
	 * @return
	 */
	private String buildSms(String desc) {
		return "“" + desc + "”这是一句神奇的暗语！1月16日记得来找百万店023.baiwandian.cn兑换新宠大礼包吧~";
	}

	/**
	 * @param type
	 * @return
	 */
	private PreheatSlogan getPreheatSloganDesc(String type) {
		if (StringUtils.isNumeric(type)) {
			for (PreheatSlogan item : PreheatSlogan.values()) {
				if (item.getIntValue() == Integer.parseInt(type))
					return item;
			}
		}
		return PreheatSlogan.NULL;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#verifySlogan(java.lang.String)
	 */
	@Override
	public BaseJsonVO verifySlogan(String msg) {
		BaseJsonVO result = new BaseJsonVO();
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isNotBlank(msg) && PreheatSlogan.getPreheatSloganByDesc(msg) != PreheatSlogan.NULL) {
			resultMap.put("p", genSloganKey(msg));
			resultMap.put("success", Boolean.TRUE);
		} else {
			resultMap.put("success", Boolean.FALSE);
		}
		result.setResult(resultMap);
		result.setCode(ErrorCode.SUCCESS);
		return result;
	}

	/**
	 * @param msg
	 * @return
	 */
	private String genSloganKey(String msg) {
		// 使用指定密钥对当前时间戳和slogan加密
		return DigestUtils.byte2Hex(DigestUtils.encryptDES(msg + ":" + System.currentTimeMillis(),
				OnlineActivityConstants.PREHEAT_KEY.getBytes()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#bindCouponPack(long,
	 *      java.lang.String)
	 */
	@Override
	public BaseJsonVO bindCouponPack(long userId, String key) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		if (userId <= 0) {
			result.setMessage("请先登录才可以领取礼包！");
			return result;
		}
		// 校验key，并绑优惠券礼包给用户。
		if (validSloganKey(key)) {
			String couponCodes = getPackCouponCodes();
			if(StringUtils.isBlank(couponCodes)){
				result.setMessage("该活动已过期，谢谢关注！");
				return result;
			}
			String packCoupos[] = StringUtils.split(couponCodes, ",");
			List<String> couponCodeList = new ArrayList<>();
			for (String couponCode : packCoupos) {
				couponCodeList.add(couponCode);
			}
			// 查询指定优惠券是否被绑定
			int couponPackCount = userCouponService.getUserCouponCountByCode(userId, packCoupos[0]);
			if (couponPackCount != 0) {
				result.setMessage("您的账号已经收到过礼包，无法重复接收。");
			}else{
				boolean bindResult = userCouponService.batchBindUserCouponWithFixDay(userId, couponCodeList, 0, true);
				if(!bindResult){
					result.setMessage("礼包接收失败！");
				}
			}
		}
		return result;
	}

	/**
	 * 获取活动优惠券礼包的代码
	 * 
	 * @return
	 */
	private String getPackCouponCodes() {
		long currentTime = System.currentTimeMillis();
		if (currentTime < OnlineActivityConstants.ACTIVITY_END_TIME
				&& currentTime > OnlineActivityConstants.ACTIVITY_START_TIME) {
			return packCouponCode;
		} else if (currentTime < OnlineActivityConstants.ACTIVITY_V2_END_TIME
				&& currentTime > OnlineActivityConstants.ACTIVITY_V2_START_TIME) {
			return packCouponV2Code;
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#bindCouponPack(long)
	 */
	@Override
	public boolean bindCouponPack(long userId) {
		if (System.currentTimeMillis() < OnlineActivityConstants.ACTIVITY_START_TIME
				|| System.currentTimeMillis() > OnlineActivityConstants.ACTIVITY_V2_END_TIME) {
			return false;
		}
		String couponCodes = getPackCouponCodes();
		if(StringUtils.isBlank(couponCodes)){
			return false;
		}
		String[] packCoupos = StringUtils.split(couponCodes, ",");
		List<String> couponCodeList = new ArrayList<>();
		for (String couponCode : packCoupos) {
			couponCodeList.add(couponCode);
		}
		// 查询指定优惠券是否被绑定
		int couponPackCount = userCouponService.getUserCouponCountByCode(userId, packCoupos[0]);
		if (couponPackCount != 0) {
			return false;
		}
		return userCouponService.batchBindUserCouponWithFixDay(userId, couponCodeList, 0, true);
	}
	
	/**
	 * 校验绑定礼包的key
	 * 
	 * @param key
	 * @return
	 */
	private boolean validSloganKey(String key) {
		return true;
//		if (StringUtils.isBlank(key)) {
//			return false;
//		}
//		try {
//			// 校验key是否有效
//			String result = DigestUtils.decryptDES(DigestUtils.hex2Byte(key),
//					OnlineActivityConstants.PREHEAT_KEY.getBytes());
//			String[] results = StringUtils.split(result, ":");
//			return PreheatSlogan.getPreheatSloganByDesc(results[0]) != PreheatSlogan.NULL
//					&& System.currentTimeMillis() > Long.parseLong(results[1]);
//		} catch (Throwable e) {
//			return false;
//		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#getActivitySummary(long)
	 */
	@Override
	public BaseJsonVO getActivitySummary(long userId) {
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		Map<String, Object> summaryInfo = onlineActivityService.getUserActivitySummary(userId);
		if (null != summaryInfo.get("lotteryRecords")) {
			@SuppressWarnings("unchecked")
			List<LotteryGiftDTO> giftList = (List<LotteryGiftDTO>) summaryInfo.get("lotteryRecords");
			List<LotteryGiftVO> giftVOList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(giftList)) {
				for (LotteryGiftDTO dto : giftList) {
					giftVOList.add(new LotteryGiftVO(dto));
				}
			}
			summaryInfo.put("lotteryRecords", giftVOList);
		}
		summaryInfo.putAll(getActivityTime());
		// 返回电话号码
		UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
		summaryInfo.put(
				"phoneNum",
				null == userProfile.getMobile() ? null
						: (StringUtils.left(userProfile.getMobile(), 3) + "****" + StringUtils.right(
								userProfile.getMobile(), 4)));
		result.setResult(summaryInfo);
		return result;
	}

	/**
	 * 返回本次mmall活动的结束时间和下次开始时间的map。 如果已结束，结束时间返回0。
	 * 
	 * @return
	 */
	private Map<String, Long> getActivityTime() {
		Map<String, Long> timeMap = new HashMap<>();
		// 初始化下次活动时间
		Calendar calNext = Calendar.getInstance();
		calNext.set(Calendar.MINUTE, 0);
		calNext.set(Calendar.SECOND, 0);
		calNext.set(Calendar.MILLISECOND, 0);
		// 初始化当前时间
		Calendar calNow = Calendar.getInstance();
		// 准备记录上次活动时间段
		int lastActivityTime = 0;
		// 遍历活动时间段
		for (int activityTIme : OnlineActivityConstants.ORD_BUBBLE_ACTIVITY_TIME) {
			// 设置下次活动时间的小时值
			calNext.set(Calendar.HOUR_OF_DAY, activityTIme);
			// 检查该次时间段是否还未到来
			if (calNext.after(calNow)) {
				// 找到还未到来的活动时段，记录距离下次活动的时间
				timeMap.put("nextStartTime", calNext.getTimeInMillis() - calNow.getTimeInMillis());
				// 检查是否记录上次活动时间段
				if (lastActivityTime == 0) {
					// 首次活动时间段还未到来，结束时间为0
					timeMap.put("currentEndTime", 0L);
				} else {
					// 已有上次活动时间段，初始化上次活动时间结束时间。
					Calendar calLastEnd = Calendar.getInstance();
					// 活动时长1.5分钟
					calLastEnd.set(Calendar.MINUTE, OnlineActivityConstants.FIND_BUBBLE_MIN_TIME);
					calLastEnd.set(Calendar.SECOND, OnlineActivityConstants.FIND_BUBBLE_SECOND_TIME);
					calLastEnd.set(Calendar.MILLISECOND, 0);
					// 上次活动时间段设置好
					calLastEnd.set(Calendar.HOUR_OF_DAY, lastActivityTime);
					// 检查是否已经结束
					if (calLastEnd.after(calNow)) {
						// 未结束，记录结束时间
						timeMap.put("currentEndTime", calLastEnd.getTimeInMillis() - calNow.getTimeInMillis());
					} else {
						// 已结束，返回0
						timeMap.put("currentEndTime", 0L);
					}
				}
				// 检查结束，退出
				break;
			} else if (activityTIme == OnlineActivityConstants.ORD_BUBBLE_ACTIVITY_TIME[OnlineActivityConstants.ORD_BUBBLE_ACTIVITY_TIME.length - 1]) {
				// 已超过本日活动最后时段，返回明日开始时间
				calNext.add(Calendar.DAY_OF_MONTH, 1);
				calNext.set(Calendar.HOUR_OF_DAY, OnlineActivityConstants.ORD_BUBBLE_ACTIVITY_TIME[0]);
				timeMap.put("nextStartTime", calNext.getTimeInMillis() - calNow.getTimeInMillis());
				timeMap.put("currentEndTime", 0L);
			}
			// 该时段已过，记录为上次时段，并开始检查下个时段
			lastActivityTime = activityTIme;
		}
		return timeMap;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#knockBubble(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public BaseJsonVO knockBubble(long userId, String userName, String id) {
		// 记录用户戳的mmall。
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		if (userId < 0) {
			// 登录后才可以戳mmall
			result.setMessage("请先登录才可以戳mmall！");
		}
		Map<String, Object> resultMap = new HashMap<>();
		if (userId > 0) {
			// 先检查mmall的类型
			String bubbleInfo = onlineActivityService.getBubbleInfo(id);
			if (StringUtils.isNotBlank(bubbleInfo)) {
				String[] infoArr = StringUtils.split(bubbleInfo, "-");
				// 如果是彩虹mmall
				if (infoArr.length == 2 && Integer.parseInt(infoArr[0]) == 1) {
					// 查询订单，限制最大单品金额
					long[] orderTimeRange = { OnlineActivityConstants.ACTIVITY_START_TIME, System.currentTimeMillis() };
					RetArg retArg = orderBriefService.queryOrderFormBrief2DTOListByStateWithUserId(userId, null,
							orderTimeRange, new DDBParam());
					@SuppressWarnings("unchecked")
					List<OrderFormBrief2DTO> orderFormBriefDTOList = RetArgUtil.get(retArg, ArrayList.class);
					if (CollectionUtils.isNotEmpty(orderFormBriefDTOList)) {
						// 遍历所有订单
						for (OrderFormBrief2DTO order : orderFormBriefDTOList) {
							// 只检查在线支付成功的订单
							if (order.getPayState() == PayState.ONLINE_PAYED) {
								// 遍历每个订单中的单品
								for (OrderSkuDTO orderSku : order.getOrdSkuList()) {
									// 如果单品价格超过上限，抢泡失败
									if (null != orderSku.getRprice()
											&& orderSku.getRprice().compareTo(
													OnlineActivityConstants.MAX_COL_BUBBLE_SKU_PRICE) >= 0) {
										resultMap.put("type", 1);
										resultMap.put("success", Boolean.FALSE);
										resultMap.put("bubbleNum", onlineActivityService.findUserBubble(userId));
										result.setResult(resultMap);
										return result;
									}
								}
							}
						}
					}
				}
			}
			// 尝试戳mmall
			resultMap = onlineActivityService.knockBubble(userId, userName, id);
			// 如果戳泡达到10个，并且成功抢到优惠券，为用户绑定一个mmall优惠券
			if (null != resultMap.get("bubbleCoupon") && (Boolean) resultMap.get("bubbleCoupon")) {
				boolean bindSuccess = userCouponService.bindUserCouponWithFixDay(userId, bubbleCouponCode, 0, true);
				if (!bindSuccess) {
					// 绑券失败
					result.setMessage("优惠券接收失败！");
				}
			}
			// 如果戳到了彩虹mmall，发送短信
			if (1 == (Integer) resultMap.get("type") && (Boolean) resultMap.get("success")) {
				UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
				if (userProfile != null && !StringUtils.isEmpty(userProfile.getMobile())) {
					messagePushFacade.sendSms(userProfile.getMobile(), COL_BUBBLE_MSG);
				}
			}
		} else {
			resultMap.put("bubbelNum", 0);
			resultMap.put("success", Boolean.FALSE);
		}
		result.setResult(resultMap);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#getLotteryGiftList()
	 */
	@Override
	public List<LotteryGiftDTO> getLotteryGiftList() {
		return onlineActivityService.getLatestLotteryGiftList();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#giveUserOneLettory(long)
	 */
	@Override
	public int giveUserOneLettory(long userId) {
		if (userId <= 0) {
			return 0;
		}
		return onlineActivityService.giveUserOneLettory(userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#giveUserOneSuitcase(long,
	 *      long)
	 */
	@Override
	public boolean giveUserOneSuitcase(long userId, long orderId) {
		if (userId <= 0 || orderId <= 0) {
			return false;
		}
		UserProfileDTO userProfile = userProfileService.findUserProfileById(userId);
		if (userProfile == null || StringUtils.isEmpty(userProfile.getUserName())) {
			return false;
		}
		boolean result = onlineActivityService.giveUserOneSuitcase(userId, userProfile.getUserName(), orderId);
		if (result && !StringUtils.isEmpty(userProfile.getMobile())) {
			this.messagePushFacade.sendSms(userProfile.getMobile(), OnlineActivityConstants.SUITCASE_GIFT_MESSAGE);
		}
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#sendOnlineActivitySms()
	 */
	@Override
	public boolean sendOnlineActivitySms() {
		// 短信号码随机存储在100个分区内，发送时依次读取
		for (int i = 0; i < 100; i++) {
			Set<byte[]> phoneNumberList = onlineActivityService.findActivityMsgPhoneSet(i);
			if (CollectionUtils.isNotEmpty(phoneNumberList)) {
				for (byte[] phoneNum : phoneNumberList) {
					messagePushFacade.sendSms(new String(phoneNum), OnlineActivityConstants.ONLIE_ACTIVITY_MESSAGE);
				}
			}
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#getOnlineActivitySms(int)
	 */
	@Override
	public Set<byte[]> getOnlineActivitySms(int i) {
		return onlineActivityService.findActivityMsgPhoneSet(i);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.OnlineActivityFacade#isHasSuitcaseGift(long,
	 *      long)
	 */
	@Override
	public boolean isHasSuitcaseGift(long userId, long orderId) {
		return onlineActivityService.isHasSuitcaseGift(userId, orderId);
	}

	// ///////////////////////////////////////
	// 抽奖相关
	// ///////////////////////////////////////
	private static final int LOTTERY_CODE_NEED_LOGIN = 200;

	private static final int LOTTERY_CODE_UNHIT = 400;

	private static final int LOTTERY_CODE_HIT_FIRST = 101;

	private static final int LOTTERY_CODE_HIT_SECOND = 102;

	private static final int LOTTERY_CODE_HIT_THIRD = 103;

	private static final int LOTTERY_CODE_HIT_FOURTH = 104;

	private static final int LOTTERY_CODE_HIT_SPECIAL = 105;

	private static final int LOTTERY_SPECIAL_THRESHOLD = 2000;

	private static final int LOTTERY_FIRST_THRESHOLD = 500;

	private static final String LOTTERY_SEPCIAL_MSG = "哇，你一定是集万千宠爱于一身的幸运儿。恭喜你获得“巴厘岛双飞双人游”一次。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	private static final String LOTTERY_FIRST_MSG = "哇，你一定是集万千宠爱于一身的幸运儿。恭喜你获得“美图KISS自拍神器”一台。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	private static final String LOTTERY_SECOND_MSG = "哇，你一定是集万千宠爱于一身的幸运儿。恭喜你获得“美国clarisonic洗脸神器”一台。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	private static final String LOTTERY_THIRD_MSG = "哇，你一定是集万千宠爱于一身的幸运儿。恭喜你获得“韩国soc超声波补水神器”一台。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	private static final String LOTTERY_FOURTH_MSG = "哇，你一定是集万千宠爱于一身的幸运儿。恭喜你获得“100元mmall优惠券”一张。不要怀疑，这不是做梦！mmall会在5个工作日内与你确认奖品寄送【023.baiwandian.cn】";

	@Override
	public BaseJsonVO userLottery(long userId, String userName) {
		String userMobile = getUserMobile(userId);

		JSONObject data = new JSONObject();
		String phoneNum = mixPhoneNum(userMobile);;
		data.put("phoneNum", phoneNum);
		
		BaseJsonVO result = new BaseJsonVO();
		result.setCode(ErrorCode.SUCCESS);
		result.setResult(data);

		if (userId < 0) {
			result.setMessage("请先登录才可以戳mmall！");
			data.put("type", LOTTERY_CODE_NEED_LOGIN);
			return result;
		}

		// 用户抽奖机会-1
		int ret = removeOneUserLottery(userId);
		if (ret == -1) {
			result.setMessage("后台系统异常！");
			data.put("type", LOTTERY_CODE_UNHIT);
			return result;
		}else{
			data.put("lotteryNum", ret);
		}

		// 当天抽奖用户数量+1
		int curlotteryUserCnt = addOneLotteryCnt();
		if (curlotteryUserCnt == 0) { // +1 failure
			result.setMessage("后台系统异常！");
			data.put("type", LOTTERY_CODE_UNHIT);
			return result;
		}

		// 判断是否有资格中特等奖
		if (isSpecialPrizeDay()) {
			int left = getCurrDateGiftLeft(GiftType.SPECIAL.getIntValue());
			long now = System.currentTimeMillis();
			if (left > 0 && (curlotteryUserCnt == LOTTERY_SPECIAL_THRESHOLD || now > curDate23())) {
				result.setMessage("恭喜你中了特等奖！");
				data.put("type", LOTTERY_CODE_HIT_SPECIAL);
				saveLotteryUserInfo(userId, GiftType.SPECIAL, userMobile, userName);
				boolean decRet = onlineActivityService.decCurrDateGiftCnt(true, GiftType.SPECIAL.getIntValue());
				if (!decRet) { // 更新失败，可能是并发问题，出现概率不大
					result.setMessage("抽奖后台发生异常！！");
					data.put("type", LOTTERY_CODE_UNHIT);
				}
				return result;
			}
		}

		// 判断是否有资格中一等奖
		if (isFirstClassPrizeDay()) {
			int left = getCurrDateGiftLeft(GiftType.FIRST.getIntValue());
			long now = System.currentTimeMillis();
			if (left > 0 && (curlotteryUserCnt == LOTTERY_FIRST_THRESHOLD || now > curDate23())) {
				result.setMessage("恭喜你中了一等奖！");
				data.put("type", LOTTERY_CODE_HIT_FIRST);
				saveLotteryUserInfo(userId, GiftType.FIRST, userMobile, userName);
				boolean decRet = onlineActivityService.decCurrDateGiftCnt(true, GiftType.FIRST.getIntValue());
				if (!decRet) { // 更新失败，可能是并发问题，出现概率不大
					result.setMessage("抽奖后台发生异常！！");
					data.put("type", LOTTERY_CODE_UNHIT);
				}
				return result;
			}
		}

		// 抽取二、三、四等奖
		int lotteryResult = lottery();
		if (lotteryResult == -1) {
			result.setMessage("没有中奖！");
			data.put("type", LOTTERY_CODE_UNHIT);
			return result;
		} else if (lotteryResult == 2) {
			result.setMessage("恭喜你中了二等奖！");
			data.put("type", LOTTERY_CODE_HIT_SECOND);
			saveLotteryUserInfo(userId, GiftType.SECOND, userMobile, userName);

			return result;
		} else if (lotteryResult == 3) {
			result.setMessage("恭喜你中了三等奖！");
			data.put("type", LOTTERY_CODE_HIT_THIRD);
			saveLotteryUserInfo(userId, GiftType.THIRD, userMobile, userName);
			return result;
		} else if (lotteryResult == 4) {
			result.setMessage("恭喜你中了四等奖！");
			data.put("type", LOTTERY_CODE_HIT_FOURTH);
			
			boolean bindSuccess = userCouponService.bindUserCouponWithFixDay(userId, lotteryCouponCode, 0, true);
			if (!bindSuccess) {
				result.setMessage("后台绑定优惠券失败！");
				data.put("type", LOTTERY_CODE_HIT_THIRD);
				return result;
			}
			
			saveLotteryUserInfo(userId, GiftType.FOURTH, userMobile, userName);
			
			return result;
		}

		// 其它情况表示没有中奖
		result.setMessage("没有中奖！");
		data.put("type", LOTTERY_CODE_UNHIT);
		return result;
	}

	/**
	 * 抽奖核心逻辑
	 * 
	 * @return 2-二等奖 3-三等奖 4-四等奖 -1-未中奖
	 */
	private int lottery() {
		// 获取每天预估订单数量
		List<LotteryOrderCnt> orderCntList = onlineActivityService.getLotteryOrderCntList();
		if (orderCntList == null || orderCntList.size() == 0) {
			// 数据库可能没有初始化
			return -1;
		}

		int ret = MobileLotteryUtil.getInstance().doLottery(orderCntList);
		if (ret == -1) {
			return ret;
		}

		if (ret == 2) {
			int left = getCurrDateGiftLeft(2); // 二等奖当天奖品剩余数量
			if (left <= 0) {
				left = getYesterdayGiftLeft(2); // 二等奖昨天奖品是否剩余
				if (left <= 0) { // 当天二等奖没有了
					return -1; // 未中奖
				} else {
					boolean decRet = onlineActivityService.decCurrDateGiftCnt(false, GiftType.SECOND.getIntValue());
					if (!decRet) { // 更新失败，可能是并发问题，出现概率不大
						return -1;
					}
				}
			} else {
				boolean decRet = onlineActivityService.decCurrDateGiftCnt(true, GiftType.SECOND.getIntValue());
				if (!decRet) {
					return -1;
				}
			}

			return 2;
		} else if (ret == 3) {
			int left = getCurrDateGiftLeft(3); // 三等奖当天奖品剩余数量
			if (left <= 0) {
				left = getYesterdayGiftLeft(3); // 三等奖昨天奖品是否剩余
				if (left <= 0) { // 当天三等奖没有了
					return -1; // 未中奖
				} else {
					boolean decRet = onlineActivityService.decCurrDateGiftCnt(false, GiftType.THIRD.getIntValue());
					if (!decRet) {
						return -1;
					}
				}
			} else {
				boolean decRet = onlineActivityService.decCurrDateGiftCnt(true, GiftType.THIRD.getIntValue());
				if (!decRet) {
					return -1;
				}
			}

			return 3;
		} else if (ret == 4) {
			int left = getCurrDateGiftLeft(4); // 四等奖当天奖品剩余数量
			if (left <= 0) {
				left = getYesterdayGiftLeft(4); // 四等奖昨天奖品是否剩余
				if (left <= 0) { // 当天四等奖没有了
					return -1; // 未中奖
				} else {
					boolean decRet = onlineActivityService.decCurrDateGiftCnt(false, GiftType.FOURTH.getIntValue());
					if (!decRet) {
						return -1;
					}
				}
			} else {
				boolean decRet = onlineActivityService.decCurrDateGiftCnt(true, GiftType.FOURTH.getIntValue());
				if (!decRet) {
					return -1;
				}
			}

			return 4;
		}

		return -1;
	}

	private boolean saveLotteryUserInfo(long userId, GiftType type, String userMobile, String userName) {
		
		if (type != GiftType.FOURTH) {
			sendMsg(type, userMobile);
		}

		long now = System.currentTimeMillis();

		LotteryGiftDTO gift = new LotteryGiftDTO();
		gift.setHitTime(now);
		gift.setType(type);
		gift.setUserId(userId);
		gift.setUserMobile(userMobile);
		gift.setUserName(userName);
		gift.setUpdateTime(now);
		gift.setName(type.getDesc());
		gift.setImage("");
		gift.setUnit("");

		return onlineActivityService.saveGiftHit(gift);
	}

	
	private String mixPhoneNum(String originPhoneNum) {
		if (originPhoneNum == null || "".equals(originPhoneNum.trim())
				|| originPhoneNum.trim().length() < 11) {
			return "";
		}
		
		String prefix = originPhoneNum.substring(0, 3);
		String postfix = originPhoneNum.substring(originPhoneNum.length()-4);
//		StringBuilder mid = new StringBuilder();
//		for (int i = 1; i <= (originPhoneNum.length()-7); i++) {
//			mid.append("*");
//		}
		
		return prefix + "****" + postfix;
	}
	
	private boolean sendMsg(GiftType type, String userMobile) {
		//userMobile = mixPhoneNum(userMobile);
		if (userMobile == null || "".equals(userMobile.trim())) {
			return false;
		}

		if (type == GiftType.SPECIAL) {
			messagePushFacade.sendSms(userMobile, LOTTERY_SEPCIAL_MSG);
		} else if (type == GiftType.FIRST) {
			messagePushFacade.sendSms(userMobile, LOTTERY_FIRST_MSG);
		} else if (type == GiftType.SECOND) {
			messagePushFacade.sendSms(userMobile, LOTTERY_SECOND_MSG);
		} else if (type == GiftType.THIRD) {
			messagePushFacade.sendSms(userMobile, LOTTERY_THIRD_MSG);
		} else if (type == GiftType.FOURTH) {
			messagePushFacade.sendSms(userMobile, LOTTERY_FOURTH_MSG);
		} else {
			return false;
		}

		return true;
	}

	private String getUserMobile(long userId) {
		String userMobile = "";
		try {
			UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(userId);
			userMobile = userProfileDTO.getMobile();
		} catch (Exception e) {
			// ignore
		}

		return userMobile;
	}

	private boolean isSpecialPrizeDay() {
		long now = getCurDateTime();
		return now == 1421978400000L; // 2014-01-23
	}

	private long curDate23() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}

	private boolean isFirstClassPrizeDay() {
		long now = getCurDateTime();
		return (now == 1421805600000L) || (now == 1422237600000L); // 2014-01-21
																	// and 01-26
	}

	private long getCurDateTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}

	@Override
	public int removeOneUserLottery(long userId) {
		return onlineActivityService.removeOneUserLottery(userId);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserId(long userId) {
		return onlineActivityService.getGiftHitByUserId(userId);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserIdAndTime(long userId, long hitTime) {
		return onlineActivityService.getGiftHitByUserIdAndTime(userId, hitTime);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserIdAndTimeRange(long userId, long hitTimeStart, long hitTimeEnd) {
		return onlineActivityService.getGiftHitByUserIdAndTimeRange(userId, hitTimeStart, hitTimeEnd);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitList() {
		return onlineActivityService.getGiftHitList();
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitListByTime(long hitTime) {
		return onlineActivityService.getGiftHitListByTime(hitTime);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitListByTimeRange(long hitTimeStart, long hitTimeEnd) {
		return onlineActivityService.getGiftHitListByTimeRange(hitTimeStart, hitTimeEnd);
	}

	@Override
	public int addOneLotteryCnt() {
		return onlineActivityService.addOneLotteryCnt();
	}

	@Override
	public Integer getYesterdayGiftLeft(int giftType) {
		return onlineActivityService.getYesterdayGiftLeft(giftType);
	}

	@Override
	public Integer getCurrDateGiftLeft(int giftType) {
		return onlineActivityService.getCurrDateGiftLeft(giftType);
	}

	@Override
	public boolean decCurrDateGiftCnt(boolean curDateFlag, int giftType) {
		return onlineActivityService.decCurrDateGiftCnt(curDateFlag, giftType);
	}

}
