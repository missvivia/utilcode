/**
 * 
 */
package com.xyl.mmall.content.dao;

import java.util.Set;
import java.util.Calendar;
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
public class LotteryDaoNkvImpl implements LotteryDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryDaoNkvImpl.class);

	private static final String NKV_LOTTERY_KEY = "NKV_LOTTERY_KEY:";

	private static final String NKV_SUIT_CASE_KEY = "NKV_SUIT_CASE_KEY:";

	private static final String NKV_ACTIVITY_MESSAGE_KEY = "NKV_ACTIVITY_MESSAGE_KEY:";

	private static final String NKV_PHONE_SET_KEY = "NKV_PHONE_SET_KEY:";

	private static final long DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT = 5000L;

	// 用户的抽奖计数活动期间不过期
	private final static int DEFAULT_NKV_LOTTERY_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	// 旅行箱的计数活动期间不过期
	private final static int DEFAULT_NKV_SUIT_CASE_OPTION_EXPIRE = 14 * 24 * 60 * 60;

	// 短信计数过期时间为1个小时
	private final static int DEFAULT_NKV_ACTIVITY_MESSAGE_OPTION_EXPIRE = 60 * 60;

	// 用户电话的计数预热期间不过期
	private final static int DEFAULT_NKV_USER_PHONE_OPTION_EXPIRE = 7 * 24 * 60 * 60;

	private static final long ONE_DAT_TIME_MILLIS = 24 * 60 * 60 * 1000L;

	@Autowired
	private DefaultExtendNkvClient defaultExtendNkvClient;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#findUserLottery(long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int findUserLottery(long userId) {
		if (userId < 0) {
			return 0;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_LOTTERY_KEY + "-" + userId).getBytes(), new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT,
							(short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read user lottery, userId id = {} and result code = {} in {}", userId,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? Integer.parseInt(new String(result.getResult())) : 0;
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to read user lottery {} due to {}", userId, e.getMessage());
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#addOneUserLottery(long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int addOneUserLottery(long userId) {
		if (userId < 0) {
			return 0;
		}
		try {
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_LOTTERY_KEY + "-" + userId).getBytes(), 1, 0, new NkvOption(
							DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_LOTTERY_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add one user lottery, user id = {} and result code = {} in {}", userId,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to add one lottery to userId {} exist due to {}", userId, e.getMessage());
		}
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#removeOneUserLottery(long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int removeOneUserLottery(long userId) {
		if (userId < 0) {
			return -1;
		}
		try {
			Result<Integer> result = defaultExtendNkvClient.decr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_LOTTERY_KEY + "-" + userId).getBytes(), 1, 0, new NkvOption(
							DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_LOTTERY_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Remove one user lottery, user id = {} and result code = {} in {}", userId,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				Integer num = result.getResult();
				if (num < 0) {
					addOneUserLottery(userId);
					return -1;
				}
				return num;
			}
			return -1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to remove one lottery to userId {} exist due to {}", userId, e.getMessage());
		}
		return -1;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#seizeOneSuitcase()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int seizeOneSuitcase() {
		try {
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_SUIT_CASE_KEY).getBytes(), 1, 0, new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0,
							DEFAULT_NKV_SUIT_CASE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Seize a colorful bubble and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				Integer num = result.getResult();
				if (num > maxSuitcaseOfToday()) {
					releaseOneSuitcase();
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
	 * 当前时段的最大可以得拉杆箱数目。
	 * 
	 * @return
	 */
	private Integer maxSuitcaseOfToday() {
		Integer currentDay = NumberUtils.convertNumberToTargetClass(
				(System.currentTimeMillis() - OnlineActivityConstants.ACTIVITY_START_TIME) / ONE_DAT_TIME_MILLIS,
				Integer.class);
		return OnlineActivityConstants.SUITCASE_GIFT_DISTRIBUTION[currentDay > 10 ? 10 : currentDay + 1];
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#releaseOneSuitcase()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int releaseOneSuitcase() {
		try {
			Result<Integer> result = defaultExtendNkvClient.decr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_SUIT_CASE_KEY).getBytes(), 1, 0, new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0,
							DEFAULT_NKV_SUIT_CASE_OPTION_EXPIRE));
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
	 * @see com.xyl.mmall.content.dao.LotteryDao#createActivityMsgKey(java.lang.String,
	 *      java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int createActivityMsgKey(String userIp, String phoneNum) {
		if (StringUtils.isBlank(userIp)) {
			return -1;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_ACTIVITY_MESSAGE_KEY + userIp).getBytes(), new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT,
							(short) 0));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read activity msg key, userIp = {} and result code = {} in {}", userIp,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result
					&& (ResultCode.OK.equals(result.getCode()) || ResultCode.NOTEXISTS.equals(result.getCode()))
					&& (null == result.getResult() || Integer.parseInt(new String(result.getResult())) < 60)) {
				Result<Integer> addResult = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
						(NKV_ACTIVITY_MESSAGE_KEY + userIp).getBytes(), 1, 0, new NkvOption(
								DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0,
								DEFAULT_NKV_ACTIVITY_MESSAGE_OPTION_EXPIRE));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Create activity msg key, userIp = {}, and result code = {} in {}", userIp,
							null != addResult ? addResult.getCode().errno() : 999, System.currentTimeMillis());
				}
				if (null != addResult && ResultCode.OK.equals(addResult.getCode())) {
					addUserPhoneNum(phoneNum);
					return addResult.getResult();
				}
			}
			return null == result.getResult() ? -1 : Integer.parseInt(new String(result.getResult())) + 1;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to put activity msg key userIp {} due to {}", userIp, e.getMessage());
		}
		return -1;
	}

	/**
	 * @param phoneNum
	 */
	@SuppressWarnings("deprecation")
	private boolean addUserPhoneNum(String phoneNum) {
		try {
			Result<Void> result = defaultExtendNkvClient.sadd(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_PHONE_SET_KEY + Long.parseLong(phoneNum) % 100).getBytes(), phoneNum.getBytes(),
					new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_USER_PHONE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add a user phone num and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to add a user phone num due to {}", e.getMessage());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.content.dao.LotteryDao#findActivityMsgPhoneList(int)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Set<byte[]> findActivityMsgPhoneSet(int index) {
		try {
			Result<Set<byte[]>> result = defaultExtendNkvClient.smembers(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_PHONE_SET_KEY + index).getBytes(), new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT,
							(short) 0, DEFAULT_NKV_USER_PHONE_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Find all user phone num and result code = {} in {}", null != result ? result.getCode()
						.errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to find all user phone num due to {}", e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int addOneLotteryCnt() {
		long key = getCurrDateTime();

		try {
			Result<Integer> result = defaultExtendNkvClient.incr(NkvConfiguration.NKV_RDB_COMMON_NAMESPACE,
					(NKV_LOTTERY_KEY + "-" + key).getBytes(), 1, 0, new NkvOption(DEFAULT_NKV_LOTTERY_OPTION_TIMEOUT,
							(short) 0, DEFAULT_NKV_LOTTERY_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add one user lottery,  result code = {} in {}", null != result ? result.getCode().errno()
						: 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult();
			}
			return 0;
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to add one lottery due to {}", e.getMessage());
		}
		return 0;
	}

	private long getCurrDateTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
}
