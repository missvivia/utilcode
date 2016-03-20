/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.xyl.mmall.content.constants.OnlineActivityConstants;
import com.xyl.mmall.framework.config.NkvConfiguration;

/**
 * @author hzlihui2014
 *
 */
@Repository
public class BubbleDaoNkvImpl implements BubbleDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(BubbleDaoNkvImpl.class);

	private static final String NKV_BUBBLE_KEY = "NKV_BUBBLE_KEY:";

	private static final String NKV_USER_BUBBLE_KEY = "NKV_USER_BUBBLE_KEY:";

	private static final String NKV_USER_BUBBLE_URL_KEY = "NKV_USER_BUBBLE_URL_KEY:";

	private static final String NKV_COLOURFUL_BUBBLE_KEY = "NKV_COLOURFUL_BUBBLE_KEY:";

	private static final String NKV_COLOURFUL_BUBBLE_COUPON_KEY = "NKV_COLOURFUL_BUBBLE_COUPON_KEY:";

	private static final String NKV_COLOURFUL_BUBBLE_USER_KEY = "NKV_COLOURFUL_BUBBLE_USER_KEY:";

	private static final String NKV_BUBBLE_PROBABILITY_KEY = "NKV_BUBBLE_PROBABILITY_KEY";
	
	private static final String NKV_COL_BUBBLE_NUM_OF_TODAY_KEY = "NKV_COL_BUBBLE_NUM_OF_TODAY_KEY";
	
	private static final String NKV_BUBBLE_COUPON_NUM_OF_TODAY_KEY = "NKV_BUBBLE_COUPON_NUM_OF_TODAY_KEY";
	
	private static final long DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT = 5000L;

	// 生成的mmall三分钟后过期
	private final static int DEFAULT_NKV_BUBBLE_OPTION_EXPIRE = 3 * 60;

	// 活动期间彩虹mmall计数不过期
	private final static int DEFAULT_NKV_COL_BUBBLE_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	// 活动期间用户mmall计数不过期
	private final static int DEFAULT_NKV_USER_BUBBLE_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	// 活动期间用户mmall页面限制5分钟过期
	private final static int DEFAULT_NKV_USER_BUBBLE_URL_OPTION_EXPIRE = 5 * 60;
	
	// 活动期间用户mmall优惠券计数不过期
	private final static int DEFAULT_NKV_BUBBLE_COUPON_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	// 活动期间mmall优惠券领取人列表不过期
	private final static int DEFAULT_NKV_BUBBLE_USER_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	private static final long ONE_DAT_TIME_MILLIS = 24 * 60 * 60 * 1000L;

	@Autowired
	private DefaultExtendNkvClient defaultExtendNkvClient;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#createBubble(java.lang.String,
	 *      int, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean createBubble(String id, int type, String url) {
		if (StringUtils.isBlank(id)) {
			return false;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.put(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_BUBBLE_KEY + id).getBytes(), (type + "-" + url).getBytes(), new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create bubble, bubble id = {}, and result code = {} in {}", id, null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to put key {} due to {}", id, e.getMessage());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#findBubble(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String findBubble(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_BUBBLE_KEY + id).getBytes(), new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read bubble, bubble id = {}, and result code = {} in {}", id, null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? new String(result.getResult()) : null;
			}
			return null;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to put key {} due to {}", id, e.getMessage());
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#removeBubble(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean removeBubble(String id) {
		if (StringUtils.isBlank(id)) {
			return false;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.remove(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_BUBBLE_KEY + id).getBytes(), new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0,
							DEFAULT_NKV_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Delete bubble, bubble id = {}, and result code = {} in {}", id, null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to remove value by key {} exist due to {}", id, e.getMessage());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#bindOneBubbleToUser(long,
	 *      int, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int bindOneBubbleToUser(long userId, int type, String url) {
		if (userId < 0 || type < 0) {
			return -1;
		}
		try {
			defaultExtendNkvClient.sadd(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_USER_BUBBLE_URL_KEY + "-" + userId).getBytes(), url.getBytes(), new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_USER_BUBBLE_URL_OPTION_EXPIRE));
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_USER_BUBBLE_KEY + "-" + userId + "-" + type).getBytes(), 1, 0, new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_USER_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Bind bubble, user id = {}, type = {} and result code = {} in {}", userId, type,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				Integer num = result.getResult();
				if (num > 10) {
					deleteBubbleFromUser(userId, type, 1);
					return -1;
				}
				return num;
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to bind bubble type {} to userId {} exist due to {}", type, userId, e.getMessage());
		}
		return -1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#findUserBubbleUrlSet(long,
	 *      int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Set<byte[]> findUserBubbleUrlSet(long userId, int type) {
		try {
			Result<Set<byte[]>> result = defaultExtendNkvClient.smembers(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_USER_BUBBLE_URL_KEY + "-" +userId).getBytes(), new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT,
							(short) 0, DEFAULT_NKV_USER_BUBBLE_URL_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Find all user bubble url and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to find all user bubble url due to {}", e.getMessage());
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#findUserBubble(long, int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int findUserBubble(long userId, int type) {
		if (userId < 0 || type < 0) {
			return 0;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_USER_BUBBLE_KEY + "-" + userId + "-" + type).getBytes(), new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read user bubble, userId id = {}, type = {}, and result code = {} in {}", userId, type,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? Integer.parseInt(new String(result.getResult())) : 0;
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to read user bubble {} type {} due to {}", userId, type, e.getMessage());
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#deleteBubbleFromUser(long,
	 *      int, int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int deleteBubbleFromUser(long userId, int type, int num) {
		if (userId < 0 || type < 0) {
			return -1;
		}
		try {
			Result<Integer> result = defaultExtendNkvClient.decr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_USER_BUBBLE_KEY + "-" + userId + "-" + type).getBytes(), num, 0, new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_USER_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Bind bubble, user id = {}, type = {} and result code = {} in {}", userId, type,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to bind bubble type {} to userId {} exist due to {}", type, userId, e.getMessage());
		}
		return -1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#seizeOneColBubble()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int seizeOneColBubble() {
		try {
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_KEY).getBytes(), 1, 0, new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT,
							(short) 0, DEFAULT_NKV_COL_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Seize a colorful bubble and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				Integer num = result.getResult();
				if (num > getColBubbleNumOfToday()) {
					releaseOneColBubble();
					return -1;
				}
				return num;
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to seize a colorful bubble due to {}", e.getMessage());
		}
		return -1;
	}

	/**
	 * 本日当前时段的最大彩色mmall数
	 * 
	 * @return
	 */
	private Integer maxColorfuleBubbleOfTodayTime() {
		Integer currentDay = NumberUtils.convertNumberToTargetClass(
				(System.currentTimeMillis() - OnlineActivityConstants.ACTIVITY_START_TIME) / ONE_DAT_TIME_MILLIS,
				Integer.class);
		Integer numOfToday = OnlineActivityConstants.COL_BUBBLE_GIFT_DISTRIBUTION[currentDay > 10 ? 10 : currentDay];
		Integer numOfYestoday = currentDay == 0 ? 0
				: OnlineActivityConstants.COL_BUBBLE_GIFT_DISTRIBUTION[(currentDay > 10 ? 10 : currentDay) - 1];
		// 初始化晚八点活动时间
		Calendar calNext = Calendar.getInstance();
		calNext.set(Calendar.MINUTE, 0);
		calNext.set(Calendar.HOUR_OF_DAY, OnlineActivityConstants.COL_BUBBLE_ACTIVITY_TIME[1]);
		calNext.set(Calendar.SECOND, 0);
		calNext.set(Calendar.MILLISECOND, 0);
		// 初始化当前时间
		Calendar calNow = Calendar.getInstance();
		if (calNow.after(calNext)) {
			// 第二次活动时，最大数更新为当日总数
			return numOfToday;
		} else {
			// 第一次活动时，最大数为当日的总数的一半
			return numOfYestoday + (numOfToday - numOfYestoday) / 2;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#releaseOneColBubble()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int releaseOneColBubble() {
		try {
			Result<Integer> result = defaultExtendNkvClient.decr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_KEY).getBytes(), 1, 0, new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT,
							(short) 0, DEFAULT_NKV_COL_BUBBLE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Release a colorful bubble and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to release a colorful bubble due to {}", e.getMessage());
		}
		return -1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#seizeOneBubbleCoupon()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int seizeOneBubbleCoupon() {
		try {
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_COUPON_KEY).getBytes(), 1, 0, new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_COUPON_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Seize a bubble coupon and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				Integer num = result.getResult();
				if (num > maxBubbleCouponOfToday()) {
					releaseOneBubbleCoupon();
					return -1;
				}
				return num;
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to seize a bubble coupon due to {}", e.getMessage());
		}
		return -1;
	}

	/**
	 * @return
	 */
	private Integer maxBubbleCouponOfToday() {
		int bubbleCouponNum = getBubbleCouponOfToday();
		if (bubbleCouponNum > 0) {
			return bubbleCouponNum;
		}
		Integer currentDay = NumberUtils.convertNumberToTargetClass(
				(System.currentTimeMillis() - OnlineActivityConstants.ACTIVITY_START_TIME) / ONE_DAT_TIME_MILLIS,
				Integer.class);
		return OnlineActivityConstants.BUBBLE_COUPON_COUNT_PER_TIME * ((currentDay > 10 ? 10 : currentDay) + 1);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#releaseOneBubbleCoupon()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int releaseOneBubbleCoupon() {
		try {
			Result<Integer> result = defaultExtendNkvClient.decr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_COUPON_KEY).getBytes(), 1, 0, new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_COUPON_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Release a bubble coupon and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to release a bubble coupon due to {}", e.getMessage());
		}
		return -1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#getLatestBubbleUserList(int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<String> getLatestBubbleUserList(int limit) {
		List<String> userNameList = new ArrayList<>();
		try {
			Result<Map<byte[], Double>> result = defaultExtendNkvClient.zrevrangebyScore(
					NkvConfiguration.NKV_RDB_COMMON_NAMESPACE, (NKV_COLOURFUL_BUBBLE_USER_KEY).getBytes(),
					System.currentTimeMillis(), System.currentTimeMillis() - ONE_DAT_TIME_MILLIS, limit, false,
					new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_USER_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Retrieve bubble user records and result code = {} in {}", null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())
					&& CollectionUtils.isNotEmpty(result.getResult().keySet())) {
				for (byte[] nameByte : result.getResult().keySet()) {
					userNameList.add(new String(nameByte));
				}
			}
			return userNameList;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to retrieve bubble user list due to {}", e.getMessage());
		}
		return userNameList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#addUserToBubbleCouponList(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String addUserToBubbleCouponList(String userName) {
		if (StringUtils.isBlank(userName)) {
			return null;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.zadd(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_USER_KEY).getBytes(), userName.getBytes(), System.currentTimeMillis(),
					new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_USER_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add a bubble user {} record and result code = {} in {}", userName,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return userName;
			}
			return null;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to add a bubble user {} due to {}", userName, e.getMessage());
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#findUserLastBubbleTime(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public long findUserLastBubbleTime(String userName) {
		if (StringUtils.isBlank(userName)) {
			return 0L;
		}
		try {
			Result<Double> result = defaultExtendNkvClient.zscore(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COLOURFUL_BUBBLE_USER_KEY).getBytes(), userName.getBytes(), new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_BUBBLE_USER_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Find a bubble user {} record and result code = {} in {}", userName,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return NumberUtils.convertNumberToTargetClass(result.getResult(), Long.class);
			}
			return 0L;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to find a bubble user {} due to {}", userName, e.getMessage());
		}
		return 0L;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.BubbleDao#getBubbleProbability(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int getBubbleProbability(String type) {
		if (StringUtils.isBlank(type)) {
			return 0;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_BUBBLE_PROBABILITY_KEY + "-" + type).getBytes(), new NkvOption(
							DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT, (short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read bubble probability, type = {}, and result code = {} in {}", type,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? Integer.parseInt(new String(result.getResult())) : 0;
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to read bubble probability type {} due to {}", type, e.getMessage());
		}
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getColBubbleNumOfToday() {
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_COL_BUBBLE_NUM_OF_TODAY_KEY).getBytes(), new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT,
							(short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read col bubble num and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? Integer.parseInt(new String(result.getResult())) : 0;
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to read col bubble num due to {}", e.getMessage());
		}
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getBubbleCouponOfToday() {
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_BUBBLE_COUPON_NUM_OF_TODAY_KEY).getBytes(), new NkvOption(DEFAULT_NKV_BUBBLE_OPTION_TIMEOUT,
							(short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read bubble coupon num and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? Integer.parseInt(new String(result.getResult())) : 0;
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to read bubble coupon num due to {}", e.getMessage());
		}
		return 0;
	}

}
