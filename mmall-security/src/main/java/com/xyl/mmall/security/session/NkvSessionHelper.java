/**
 * 
 */
package com.xyl.mmall.security.session;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.Result.ResultCode;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.client.impl.DefaultNkvClient;
import com.xyl.mmall.framework.config.NkvConfiguration;

/**
 * Session转存NKV工具类
 * 
 * @author lihui
 *
 */
public final class NkvSessionHelper {

	/**
	 * 
	 */
	private static final long DEFAULT_NKV_SESSION_OPTION_TIMEOUT = 5000L;

	private static final Logger LOGGER = LoggerFactory.getLogger(NkvSessionHelper.class);

	private static final String NKV_MMALL_SESSION_KEY = "_NKV_MMALL_SESSION_KEY";

	private final static int DEFAULT_NKV_SESSION_OPTION_EXPIRE = 30 * 60;

	private DefaultNkvClient defaultExtendNkvClient;

	private String webSessionKeyPrefix = null;

	/**
	 * 根据指定的key获取之前保存的value。
	 * 
	 * @param key
	 *            存储的key
	 * @return 成功返回之前保存的值，否则返回null
	 */
	public Session getFromSession(String key) {
		return getFromSession(key, DEFAULT_NKV_SESSION_OPTION_EXPIRE);
	}

	/**
	 * 根据指定的key获取之前保存的value。
	 * 
	 * @param key
	 *            存储的key
	 * @param expire
	 *            过期时间，单位为毫秒
	 * @return 成功返回之前保存的值，否则返回null
	 */
	@SuppressWarnings("deprecation")
	public Session getFromSession(String key, int expire) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		try {
			Result<byte[]> result = defaultExtendNkvClient.get(NkvConfiguration.NKV_MDB_SESSION_NAMESPACE,
					(webSessionKeyPrefix + NKV_MMALL_SESSION_KEY + key).getBytes(), new NkvOption(
							DEFAULT_NKV_SESSION_OPTION_TIMEOUT, (short) 0, DEFAULT_NKV_SESSION_OPTION_EXPIRE));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Read session, session id = {}, and result code = {} in {}", key, null != result ? result
						.getCode().errno() : 999, System.currentTimeMillis());
			}
			if (null != result && ResultCode.OK.equals(result.getCode())) {
				return result.getResult() != null ? (Session) SerializationUtils.deserialize(result.getResult()) : null;
			}
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to get value by key {} exist due to {}", key, e.getMessage());
		}
		return null;
	}

	/**
	 * 将一个指定key的session数据删除。
	 * 
	 * @param key
	 *            存储的key
	 * @return 成功返回true，否则返回false
	 */
	public boolean removeFromSession(String key) {
		return removeFromSession(key, DEFAULT_NKV_SESSION_OPTION_EXPIRE);
	}

	/**
	 * 将一个指定key的session数据删除。
	 * 
	 * @param key
	 *            存储的key
	 * @param expire
	 *            过期时间，单位为毫秒
	 * @return 成功返回true，否则返回false
	 */
	@SuppressWarnings("deprecation")
	public boolean removeFromSession(String key, int expire) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.remove(NkvConfiguration.NKV_MDB_SESSION_NAMESPACE,
					(webSessionKeyPrefix + NKV_MMALL_SESSION_KEY + key).getBytes(), new NkvOption(
							DEFAULT_NKV_SESSION_OPTION_TIMEOUT, (short) 0, expire));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Delete session, session id = {}, and result code = {} in {}", key,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to remove value by key {} exist due to {}", key, e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * 将一对key/value对存储在nkv中。
	 * 
	 * @param key
	 *            存储的key
	 * @param value
	 *            存储的value
	 * 
	 * @return 成功返回true，否则返回false
	 */
	public boolean putToSession(String key, Session value) {
		return putToSession(key, value, DEFAULT_NKV_SESSION_OPTION_EXPIRE);
	}

	/**
	 * 
	 * 将一对key/value对存储在nkv中。
	 * 
	 * @param key
	 *            存储的key
	 * @param value
	 *            存储的value
	 * @param expire
	 *            过期时间，单位为毫秒
	 * @return 成功返回true，否则返回false
	 */
	@SuppressWarnings("deprecation")
	public boolean putToSession(String key, Session value, int expire) {
		if (StringUtils.isBlank(key) || null == value) {
			return false;
		}
		try {
			Result<Void> result = defaultExtendNkvClient.put(NkvConfiguration.NKV_MDB_SESSION_NAMESPACE,
					(webSessionKeyPrefix + NKV_MMALL_SESSION_KEY + key).getBytes(), SerializationUtils
							.serialize(value), new NkvOption(DEFAULT_NKV_SESSION_OPTION_TIMEOUT, (short) 0, expire));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create/Update session, session id = {}, and result code = {} in {}", key,
						null != result ? result.getCode().errno() : 999, System.currentTimeMillis());
			}
			return null != result && ResultCode.OK.equals(result.getCode());
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			LOGGER.error("Failed to put key {} and value {} due to {}", key, value.toString(), e.getMessage());
		}
		return false;
	}

	/**
	 * 获取所有session的key和value值
	 * 
	 * @return
	 */
	public Collection<Session> getAllFromSessions() {
		return getAllFromSessions(DEFAULT_NKV_SESSION_OPTION_EXPIRE);
	}

	/**
	 * 获取所有session的key和value值
	 * 
	 * @param expire
	 *            过期时间，单位为毫秒
	 * @return
	 */
	public Collection<Session> getAllFromSessions(int expire) {
		// always return empty set of session to satisfied shiro.
		Set<Session> sessonSet = new HashSet<>();
		return sessonSet;
	}

	/**
	 * @return the defaultExtendNkvClient
	 */
	public DefaultNkvClient getDefaultExtendNkvClient() {
		return defaultExtendNkvClient;
	}

	/**
	 * @param defaultExtendNkvClient
	 *            the defaultExtendNkvClient to set
	 */
	public void setDefaultExtendNkvClient(DefaultNkvClient defaultExtendNkvClient) {
		this.defaultExtendNkvClient = defaultExtendNkvClient;
	}

	/**
	 * @return the webSessionKeyPrefix
	 */
	public String getWebSessionKeyPrefix() {
		return webSessionKeyPrefix;
	}

	/**
	 * @param webSessionKeyPrefix
	 *            the webSessionKeyPrefix to set
	 */
	public void setWebSessionKeyPrefix(String webSessionKeyPrefix) {
		this.webSessionKeyPrefix = webSessionKeyPrefix;
	}

}
