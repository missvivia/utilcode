/**
 * 
 */
package com.xyl.mmall.content.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.content.dto.BubbleActivityDTO;
import com.xyl.mmall.content.dto.LotteryGiftDTO;
import com.xyl.mmall.content.dto.PresentProductDTO;
import com.xyl.mmall.content.enums.GiftType;
import com.xyl.mmall.content.meta.LotteryGift;
import com.xyl.mmall.content.meta.LotteryGiftStat;
import com.xyl.mmall.content.meta.LotteryOrderCnt;
import com.xyl.mmall.content.meta.PresentProduct;
import com.xyl.mmall.content.meta.SuitcaseGift;
import com.xyl.mmall.content.service.OnlineActivityService;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.content.dao.BubbleDao;
import com.xyl.mmall.content.dao.LotteryDao;
import com.xyl.mmall.content.dao.LotteryGiftDao;
import com.xyl.mmall.content.dao.LotteryGiftStatDao;
import com.xyl.mmall.content.dao.LotteryOrderCntDao;
import com.xyl.mmall.content.dao.PresentProductDao;
import com.xyl.mmall.content.dao.SuitcaseGiftDao;

/**
 * @author hzlihui2014
 * 
 */
public class OnlineActivityServiceImpl implements OnlineActivityService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LotteryGiftDao lotteryGiftDao;

	@Autowired
	private SuitcaseGiftDao suitcaseGiftDao;

	@Autowired
	private PresentProductDao presentProductDao;

	@Autowired
	private BubbleDao bubbleDao;

	@Autowired
	private LotteryDao lotteryDao;
	
	@Autowired
	private LotteryGiftStatDao lotteryGiftStatDao;

	@Autowired
	private LotteryOrderCntDao lotteryOrderCntDao;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#getPresentProductList(long)
	 */
	@Override
	@Cacheable(value = "presentProductList")
	public List<PresentProductDTO> getPresentProductList(long saleAreaId) {
		if (saleAreaId <= 0) {
			return null;
		}
		DDBParam param = new DDBParam();
		param.setOrderColumn("orderBy");
		param.setAsc(true);
		List<PresentProduct> productList = presentProductDao.getPresentProductListByAreaId(saleAreaId, param);
		List<PresentProductDTO> productDTOList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(productList)) {
			for (PresentProduct product : productList) {
				productDTOList.add(new PresentProductDTO(product));
			}
		}
		return productDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#genBubble(long,
	 *      java.lang.String,  java.lang.String)
	 */
	@Override
	public BubbleActivityDTO genBubble(long userId, String userName, String url) {
		if (luckyForOrdBubble(userName)) {
			BubbleActivityDTO bubble = new BubbleActivityDTO();
			UUID uuid = UUID.randomUUID();
			bubble.setId(uuid.toString());
			bubble.setType(luckyForColBubble() && noColBubbleYet(userId) ? 1 : 0);
			// 检查用户是否在该页面戳过mmall了
			if (userId > 0) {
				Set<byte[]> urlSet = bubbleDao.findUserBubbleUrlSet(userId, bubble.getType());
				if (CollectionUtils.isNotEmpty(urlSet)) {
					for (byte[] urlByte : urlSet) {
						if (url.equalsIgnoreCase(new String(urlByte))) {
							// 已戳，返回空
							return null;
						}
					}
				}
			}
			bubbleDao.createBubble(bubble.getId(), bubble.getType(), url);
			return bubble;
		}
		return null;
	}

	/**
	 * @return
	 */
	private boolean luckyForColBubble() {
		// 判断当前是否在整点的活动分钟之内
		Calendar calNow = Calendar.getInstance();
		Calendar calNextStart = Calendar.getInstance();
		calNextStart.set(Calendar.MINUTE, 0);
		calNextStart.set(Calendar.SECOND, 0);
		calNextStart.set(Calendar.MILLISECOND, 0);
		Calendar calNextEnd = Calendar.getInstance();
		calNextEnd.set(Calendar.SECOND, 0);
		calNextEnd.set(Calendar.MILLISECOND, 0);
		// 遍历活动时间段
		for (int activityTIme : OnlineActivityConstants.COL_BUBBLE_ACTIVITY_TIME) {
			calNextStart.set(Calendar.HOUR_OF_DAY, activityTIme);
			calNextEnd.set(Calendar.MINUTE, OnlineActivityConstants.FIND_BUBBLE_MIN_TIME);
			calNextEnd.set(Calendar.SECOND, OnlineActivityConstants.FIND_BUBBLE_SECOND_TIME);
			calNextEnd.set(Calendar.HOUR_OF_DAY, activityTIme);
			if (calNow.after(calNextStart) && calNow.before(calNextEnd)) {
				int probability = bubbleDao.getBubbleProbability("COL");
				if (probability <= 0) {
					probability = OnlineActivityConstants.COL_BUBBLE_PROBABILITY;
				}
				return RandomUtils.nextInt(9999) % 9 < probability;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	private boolean luckyForOrdBubble(String userName) {
		int probability = bubbleDao.getBubbleProbability("ORD");
		if (probability <= 0) {
			probability = OnlineActivityConstants.ORD_BUBBLE_PROBABILITY;
		}
		if(StringUtils.isNotBlank(userName)){
			long lastBubbleTime = bubbleDao.findUserLastBubbleTime(userName);
			if (lastBubbleTime > 0) {
				Calendar startOfTody = Calendar.getInstance();
				startOfTody.set(Calendar.MINUTE, 0);
				startOfTody.set(Calendar.SECOND, 0);
				startOfTody.set(Calendar.MILLISECOND, 0);
				startOfTody.set(Calendar.HOUR_OF_DAY, 0);
				if(startOfTody.getTimeInMillis() < lastBubbleTime){
					probability = bubbleDao.getBubbleProbability("SEC_ORD");
					if (probability <= 0) {
						probability = OnlineActivityConstants.SECOND_ORD_BUBBLE_PROBABILITY;
					}
				}
			}
		}
		return RandomUtils.nextInt(9999) % 9 < probability;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#getUserActivitySummary(long)
	 */
	@Override
	public Map<String, Object> getUserActivitySummary(long userId) {
		Map<String, Object> resultMap = new HashMap<>();
		if (userId > 0) {
			resultMap.put("bubbleNum", bubbleDao.findUserBubble(userId, 0));
			resultMap.put("lotteryNum", lotteryDao.findUserLottery(userId));
		} else {
			resultMap.put("bubbleNum", 0);
			resultMap.put("lotteryNum", 0);
		}
		// 为页面返回一个中奖纪录的对象。
		resultMap.put("lotteryRecords", getLatestLotteryGiftList());
		return resultMap;
	}

	/**
	 * 获取最新的mmall优惠券记录
	 * 
	 * @return
	 */
	private List<LotteryGiftDTO> getLatestBubbleCouponList() {
		List<LotteryGiftDTO> bubbleCouponList = new ArrayList<>();
		List<String> userNameList = bubbleDao
				.getLatestBubbleUserList(OnlineActivityConstants.MAX_BUBBLE_COUPON_USER_DISPLAY_COUNT);
		if (CollectionUtils.isNotEmpty(userNameList)) {
			for (String userName : userNameList) {
				LotteryGiftDTO gift = new LotteryGiftDTO();
				gift.setType(GiftType.BUBBLE);
				gift.setName(GiftType.BUBBLE.getDesc());
				gift.setUserName(userName);
				bubbleCouponList.add(gift);
			}
		}
		return bubbleCouponList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#knockBubble(long,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> knockBubble(long userId, String userName, String id) {
		Map<String, Object> resultMap = new HashMap<>();
		String bubbleInfo = bubbleDao.findBubble(id);
		String[] infoArr = StringUtils.split(bubbleInfo, "-");
		int type = Integer.parseInt(infoArr[0]);
		resultMap.put("type", type);
		if (type == 1 && bubbleDao.removeBubble(id) && noColBubbleYet(userId)) {
			// 删除mmall成功, 尝试抢占彩虹mmall。
			int result = bubbleDao.seizeOneColBubble();
			if (result < 0) {
				// 抢彩虹mmall失败
				resultMap.put("success", Boolean.FALSE);
			} else {
				// 抢彩虹mmall成功
				resultMap.put("success", Boolean.TRUE);
				// 记录中奖纪录
				lotteryGiftDao.addObject(buildFreeGift(userId, userName));
			}
			// 取目前用户的普通mmall数
			resultMap.put("bubbleNum", bubbleDao.findUserBubble(userId, 0));
		} else if (type == 0 && bubbleDao.removeBubble(id)) {
			// 戳普通mmall成功，尝试为用户增加一个普通类型的mmall
			int bubbleNum = bubbleDao.bindOneBubbleToUser(userId, type, infoArr[1]);
			if (bubbleNum < 0) {
				// 添加mmall数目失败
				resultMap.put("success", Boolean.FALSE);
				// 取目前用户的普通mmall数
				resultMap.put("bubbleNum", bubbleDao.findUserBubble(userId, 0));
			} else {
				// 添加mmall数据成功
				resultMap.put("success", Boolean.TRUE);
				if (bubbleNum >= 10) {
					// mmall数达到10个时，尝试抢占优惠券
					boolean seizeCouponSuccess = bubbleDao.seizeOneBubbleCoupon() > 0;
					if (seizeCouponSuccess) {
						// 抢占优惠券成功， 减掉用户10个mmall数目
						bubbleDao.deleteBubbleFromUser(userId, type, 10);
						bubbleDao.addUserToBubbleCouponList(userName);
						resultMap.put("bubbleCoupon", Boolean.TRUE);
						bubbleNum = bubbleNum - 10;
					} else {
						// 抢占失败，继续保持现有用户mmall数目
						resultMap.put("bubbleCoupon", Boolean.FALSE);
					}
				}
				// 返回最新的普通mmall数目
				resultMap.put("bubbleNum", bubbleNum);
			}
		} else {
			// 戳mmall失败
			resultMap.put("success", Boolean.FALSE);
			// 取目前用户的普通mmall数
			resultMap.put("bubbleNum", bubbleDao.findUserBubble(userId, 0));
		}
		return resultMap;
	}

	/**
	 * 检查是否抢到过彩虹mmall
	 * 
	 * @param userId
	 * @return
	 */
	private boolean noColBubbleYet(long userId) {
		List<LotteryGift> giftList = lotteryGiftDao.getListByUserId(userId);
		if (CollectionUtils.isNotEmpty(giftList)) {
			for (LotteryGift gift : giftList) {
				if (GiftType.FREE == gift.getType()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 组装一个免单奖
	 * 
	 * @param userId
	 * @param userName
	 * @return
	 */
	private LotteryGift buildFreeGift(long userId, String userName) {
		LotteryGift freeGift = new LotteryGift();
		freeGift.setImage("");
		freeGift.setName(GiftType.FREE.getDesc());
		freeGift.setType(GiftType.FREE);
		freeGift.setUnit("");
		freeGift.setUpdateTime(System.currentTimeMillis());
		freeGift.setUserId(userId);
		freeGift.setUserName(userName);
		return freeGift;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#getLatestLotteryGiftList()
	 */
	@Override
	public List<LotteryGiftDTO> getLatestLotteryGiftList() {
		List<LotteryGiftDTO> dtoList = new ArrayList<>();
		// 查询最新的大奖纪录
		dtoList.addAll(getImportentAward());
		// 查询最新的普通奖记录
		dtoList.addAll(getOrdinaryAward());
		dtoList.addAll(getLatestBubbleCouponList());
		return dtoList;
	}

	/**
	 * @return
	 */
	private List<LotteryGiftDTO> getImportentAward() {
		List<LotteryGiftDTO> dtoList = new ArrayList<>();
		DDBParam param = new DDBParam();
		// 每次查找全部大奖
		// 按照中奖时间降序排列
		param.setOrderColumn("updateTime");
		param.setAsc(false);
		// 查询
		List<GiftType> typeList = new ArrayList<>();
		typeList.add(GiftType.SPECIAL);
		typeList.add(GiftType.FIRST);
		typeList.add(GiftType.SECOND);
		typeList.add(GiftType.THIRD);
		List<LotteryGift> lotteryGiftList = lotteryGiftDao.getListByGiftTypeList(typeList, param);
		if (CollectionUtils.isNotEmpty(lotteryGiftList)) {
			// 检查查询结果
			for (LotteryGift gift : lotteryGiftList) {
				if (gift.getUserId() > 0) {
					// 只取有中奖的纪录
					dtoList.add(new LotteryGiftDTO(gift));
				} else {
					// 到达非中奖纪录，退出遍历
					break;
				}
			}
		}
		return dtoList;
	}

	/**
	 * @return
	 */
	private List<LotteryGiftDTO> getOrdinaryAward() {
		List<LotteryGiftDTO> dtoList = new ArrayList<>();
		DDBParam param = new DDBParam();
		// 每次只查找最新的80个
		param.setLimit(80);
		param.setOffset(0);
		// 按照中奖时间降序排列
		param.setOrderColumn("updateTime");
		param.setAsc(false);
		// 查询
		List<GiftType> typeList = new ArrayList<>();
		typeList.add(GiftType.FOURTH);
		typeList.add(GiftType.SUITCASE);
		typeList.add(GiftType.FREE);
		List<LotteryGift> lotteryGiftList = lotteryGiftDao.getListByGiftTypeList(typeList, param);
		if (CollectionUtils.isNotEmpty(lotteryGiftList)) {
			// 检查查询结果
			for (LotteryGift gift : lotteryGiftList) {
				if (gift.getUserId() > 0) {
					// 只取有中奖的纪录
					dtoList.add(new LotteryGiftDTO(gift));
				} else {
					// 到达非中奖纪录，退出遍历
					break;
				}
			}
		}
		return dtoList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#giveUserOneLettory(long)
	 */
	@Override
	public int giveUserOneLettory(long userId) {
		return lotteryDao.addOneUserLottery(userId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#giveUserOneSuitcase(long,
	 *      java.lang.String, long)
	 */
	@Override
	public boolean giveUserOneSuitcase(long userId, String userName, long orderId) {
		// 尝试抢占一个旅行箱
		if (lotteryDao.seizeOneSuitcase() > 0) {
			// 抢占成功，记录订单信息
			SuitcaseGift suitcaseGift = new SuitcaseGift();
			suitcaseGift.setCreateTime(System.currentTimeMillis());
			suitcaseGift.setOrderId(orderId);
			suitcaseGift.setUserId(userId);
			suitcaseGiftDao.addObject(suitcaseGift);
			lotteryGiftDao.addObject(buildSuitcaseGift(userId, userName));
			return true;
		}
		return false;
	}

	/**
	 * @param userId
	 * @param userName
	 * @return
	 */
	private LotteryGift buildSuitcaseGift(long userId, String userName) {
		LotteryGift freeGift = new LotteryGift();
		freeGift.setImage("");
		freeGift.setName(GiftType.SUITCASE.getDesc());
		freeGift.setType(GiftType.SUITCASE);
		freeGift.setUnit("");
		freeGift.setUpdateTime(System.currentTimeMillis());
		freeGift.setUserId(userId);
		freeGift.setUserName(userName);
		return freeGift;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#createSloganMessageKey(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public int createSloganMessageKey(String userIp, String phoneNum) {
		if (StringUtils.isBlank(userIp) || StringUtils.isBlank(phoneNum)) {
			return -1;
		}
		// 生成一个短信发送记录
		return lotteryDao.createActivityMsgKey(userIp, phoneNum);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#findActivityMsgPhoneSet(int)
	 */
	@Override
	public Set<byte[]> findActivityMsgPhoneSet(int index) {
		return lotteryDao.findActivityMsgPhoneSet(index);
	}
	
		
	@Override
	public int removeOneUserLottery(long userId) {
		return lotteryDao.removeOneUserLottery(userId);
	}
	
	@Override
	public boolean saveGiftHit(LotteryGiftDTO gift) {
		logger.info("saveGiftHit: " + gift);
		gift.setHitTime(getHitTime(gift.getHitTime()));
		return lotteryGiftDao.saveGiftHit((LotteryGift) gift);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserId(long userId) {
		logger.info("getGiftHitByUserId(" + userId + ")");
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitByUserId(userId);
		return convert2DTO(giftList);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserIdAndTime(long userId, long hitTime) {
		logger.info("getGiftHitByUserIdAndTime(" + userId + ", " + hitTime + ")");
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitByUserIdAndTime(userId, hitTime);
		return convert2DTO(giftList);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitByUserIdAndTimeRange(long userId, long hitTimeStart, long hitTimeEnd) {
		logger.info("getGiftHitByUserIdAndTimeRange(" + userId + ", " + hitTimeStart + ", " + hitTimeEnd + ")");
		hitTimeStart = getHitTimeQueryStart(hitTimeStart);
		hitTimeEnd = getHitTimeQueryEnd(hitTimeEnd);
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitByUserIdAndTimeRange(userId, hitTimeStart, hitTimeEnd);
		return convert2DTO(giftList);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitList() {
		logger.info("getGiftHitList() called...");
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitList();
		return convert2DTO(giftList);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitListByTime(long hitTime) {
		logger.info("getGiftHitListByTime(" + hitTime + ")");
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitListByTime(hitTime);
		return convert2DTO(giftList);
	}

	@Override
	public List<LotteryGiftDTO> getGiftHitListByTimeRange(long hitTimeStart, long hitTimeEnd) {
		logger.info("getGiftHitListByTimeRange(" + hitTimeStart + ", " + hitTimeEnd + ")");
		hitTimeStart = getHitTimeQueryStart(hitTimeStart);
		hitTimeEnd = getHitTimeQueryEnd(hitTimeEnd);
		List<LotteryGift> giftList = lotteryGiftDao.getGiftHitListByTimeRange(hitTimeStart, hitTimeEnd);
		return convert2DTO(giftList);
	}

	private long getHitTime(long hitTime) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(hitTime);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}

	private long getHitTimeQueryStart(long hitTimeStart) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(hitTimeStart);
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}

	private long getHitTimeQueryEnd(long hitTimeEnd) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(hitTimeEnd);
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTimeInMillis();
	}

	private List<LotteryGiftDTO> convert2DTO(List<LotteryGift> giftList) {
		List<LotteryGiftDTO> giftDTOList = new ArrayList<LotteryGiftDTO>();

		if (giftList == null) {
			giftList = new ArrayList<LotteryGift>();
		}

		for (LotteryGift gift : giftList) {
			giftDTOList.add((LotteryGiftDTO) gift);
		}

		logger.info("Successfully get gift list: " + giftDTOList);
		return giftDTOList;
	}

	@Override
	public int addOneLotteryCnt() {
		return lotteryDao.addOneLotteryCnt();
	}

	@Override
	public Integer getYesterdayGiftLeft(int giftType) {
		long date = getYesterdayDateTime();
		LotteryGiftStat stat = lotteryGiftStatDao.getGiftStat(giftType, date);
		
		if (stat == null) {
			return 0;
		}
		
		return stat.getLotteryCnt();
	}
	
	@Override
	public Integer getCurrDateGiftLeft(int giftType) {
		long date = getCurrDateTime();
		LotteryGiftStat stat = lotteryGiftStatDao.getGiftStat(giftType, date);
		if (stat == null) {
			return 0;
		}
		
		return stat.getLotteryCnt();
	}

	@Transaction
	@Override
	public boolean decCurrDateGiftCnt(boolean curDateFlag, int giftType) {
		long date = getCurrDateTime();
		if (!curDateFlag) {
			date = getYesterdayDateTime();
		}
		
		LotteryGiftStat stat = lotteryGiftStatDao.getGiftStat(giftType, date);
		if (stat == null) {
			logger.warn("Cannot find any gift stat info for type=" + giftType + ", date=" + date);
			return false;
		}
		
		int cnt = stat.getLotteryCnt()-1;
		return lotteryGiftStatDao.updateGiftStat(giftType, date, cnt);
	}
	
	@Override
	public List<LotteryOrderCnt> getLotteryOrderCntList() {
		List<LotteryOrderCnt> list = lotteryOrderCntDao.getGiftOrderCntList();
		
		logger.info("Successfully get order cnt list: " + list);
		return list;
	}

	private long getYesterdayDateTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	private long getCurrDateTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#isHasSuitcaseGift(long,
	 *      long)
	 */
	@Override
	public boolean isHasSuitcaseGift(long userId, long orderId) {
		if (!(userId > 0 && orderId > 0)) {
			return false;
		}
		List<SuitcaseGift> suitcaseGiftList = suitcaseGiftDao.findByUserIdAndOrderId(userId, orderId);
		return CollectionUtils.isNotEmpty(suitcaseGiftList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#getBubbleInfo(java.lang.String)
	 */
	@Override
	public String getBubbleInfo(String id) {
		return StringUtils.isBlank(id) ? null : this.bubbleDao.findBubble(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.service.OnlineActivityService#findUserBubble(long)
	 */
	@Override
	public int findUserBubble(long userId) {
		return bubbleDao.findUserBubble(userId, 0);
	}
	
}
