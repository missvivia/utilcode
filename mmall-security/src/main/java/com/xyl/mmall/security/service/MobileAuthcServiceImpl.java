/**
 * 
 */
package com.xyl.mmall.security.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.netease.print.security.util.FullUserNameUtils;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.util.HttpUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.security.thirdparty.ThirdPartyApp;
import com.xyl.mmall.security.token.MobileAccessToken;
import com.xyl.mmall.security.utils.DigestUtils;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * @author lihui
 *
 */
public class MobileAuthcServiceImpl implements MobileAuthcService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileAuthcServiceImpl.class);

	private ApplicationContext context = null;

	private String initAppUrl = null;

	private String loginForMobUrl = null;

	private String logoutForMobUrl = null;

	private String checkURSTokenUrl = null;

	private String checkOAuthTokenUrl = null;

	private String product = null;
	
	private String exchangeURSTokenUrl = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#getInitAppIdKey(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getInitAppIdKey(String pdtVersion, String mac, String deviceType, String systemName,
			String systemVersion, String resolution, String uniqueID) {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuilder url = new StringBuilder().append(initAppUrl);
		url.append("?product=").append(product).append("&pdtVersion=").append(pdtVersion).append("&mac=").append(mac)
				.append("&deviceType=").append(deviceType).append("&systemName=").append(systemName)
				.append("&systemVersion=").append(systemVersion).append("&resolution=").append(resolution)
				.append("&uniqueID=").append(uniqueID);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Send init app request: {}", url.toString());
		}
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when init app!", e);
			resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
			resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
			return resultMap;
		}
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 1) {
			// 内容第一行为返回的代码。
			if ("201".equals(contentArr[0])) {
				// 获取成功，内容第三行为"id=xxx&key=xxxx"。
				String[] ursResults = StringUtils.split(contentArr[2], "&");
				if (null != ursResults && ursResults.length > 0) {
					for (String result : ursResults) {
						String[] tempArray = StringUtils.split(result, "=");
						if (null != tempArray && tempArray.length > 0) {
							resultMap.put(tempArray[0], tempArray[1]);
						}
					}
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Init app request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				// 获取失败，内容第二行为提示信息。
				resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
				resultMap.put("message", contentArr[1]);
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#loginForMob(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> loginForMob(String id, String params, String initKey, String userIp) {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuilder url = new StringBuilder().append(loginForMobUrl);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User login from IP: {}", userIp);
		}
		params = rebuildParamsWithUserIp(params, initKey, userIp);
		url.append("?id=").append(id).append("&params=").append(params);
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when user login!", e);
			resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
			resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
			return resultMap;
		}
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 1) {
			// 内容第一行为返回的代码。
			if ("201".equals(contentArr[0])) {
				// 获取成功，内容第三行为"result=xxx"。
				String[] tempArray = StringUtils.split(contentArr[2], "=");
				if (null != tempArray && tempArray.length > 0) {
					resultMap.put(tempArray[0], tempArray[1]);
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("URS login request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				// 获取失败，内容第二行为提示信息。
				switch (contentArr[0]) {
				case "420":
					resultMap.put("code", String.valueOf(MobileErrorCode.USER_NAME_NOT_EXSIX.getIntValue()));
					resultMap.put("message", MobileErrorCode.USER_NAME_NOT_EXSIX.getDesc());
					break;
				case "422":
					resultMap.put("code", String.valueOf(MobileErrorCode.ACCOUNT_FREEZE.getIntValue()));
					resultMap.put("message", MobileErrorCode.ACCOUNT_FREEZE.getDesc());
					break;
				case "412":
					resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_ERROR_TOO_MANY.getIntValue()));
					resultMap.put("message", MobileErrorCode.LOGIN_ERROR_TOO_MANY.getDesc());
					break;
				case "460":
					resultMap.put("code", String.valueOf(MobileErrorCode.PASSWORD_ERROR.getIntValue()));
					resultMap.put("message", MobileErrorCode.PASSWORD_ERROR.getDesc());
					break;
				default:
					resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
					resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
				}
			}
		}
		return resultMap;
	}

	/**
	 * 对手机用户的请求参数中添加用户的IP内容。
	 * 
	 * @param params
	 * @param initKey
	 * @param remoteHost
	 * @return
	 */
	private String rebuildParamsWithUserIp(String params, String initKey, String remoteHost) {
		String orgPrarms = DigestUtils.decryptAES(DigestUtils.hex2Byte(params), DigestUtils.hex2Byte(initKey));
		if (StringUtils.isBlank(orgPrarms)) {
			// 解密失败。正常情况下不会出现这种情况，这里直接返回会导致登录请求验证失败。
			return params;
		}
		StringBuilder sb = new StringBuilder(orgPrarms);
		sb.append("&userip=").append(remoteHost);
		return DigestUtils.byte2Hex(DigestUtils.encryptAES(sb.toString(), DigestUtils.hex2Byte(initKey)));
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#logoutForMob(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Map<String, String> logoutForMob(String id, String params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuilder url = new StringBuilder().append(logoutForMobUrl);
		url.append("?id=").append(id).append("&params=");
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when user logout!", e);
			resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
			resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
			return resultMap;
		}
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 1) {
			// 内容第一行为返回的代码。
			if (!"200".equals(contentArr[0])) {
				// 登出失败，内容第二行为提示信息。
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("URS logout request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
				resultMap.put("message", contentArr[1]);
			}
		}
		return resultMap;
	}

	/**
	 * @param loginForMobUrl
	 *            the loginForMobUrl to set
	 */
	public void setLoginForMobUrl(String loginForMobUrl) {
		this.loginForMobUrl = loginForMobUrl;
	}

	/**
	 * @param initAppUrl
	 *            the initAppUrl to set
	 */
	public void setInitAppUrl(String initAppUrl) {
		this.initAppUrl = initAppUrl;
	}

	/**
	 * @return the logoutForMobUrl
	 */
	public String getLogoutForMobUrl() {
		return logoutForMobUrl;
	}

	/**
	 * @param logoutForMobUrl
	 *            the logoutForMobUrl to set
	 */
	public void setLogoutForMobUrl(String logoutForMobUrl) {
		this.logoutForMobUrl = logoutForMobUrl;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#decryptURSToken(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Map<String, String> decryptURSToken(String result, String initKey) {
		String decryptedResult = DigestUtils.decryptAES(DigestUtils.hex2Byte(result), DigestUtils.hex2Byte(initKey));
		// 从result中解密token，格式为token=&username=...
		String[] results = StringUtils.split(decryptedResult, "&");
		if (null != results && results.length > 0) {
			Map<String, String> resultMap = new HashMap<>();
			for (String item : results) {
				// 格式为username=xxx@163.com...
				String[] items = StringUtils.split(item, "=");
				if (items != null && items.length > 1) {
					resultMap.put(items[0], items[1]);
				}
			}
			return resultMap;
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#validateURSToken(java.lang.String,
	 *      java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public MobileAccessToken validateURSToken(String initId, String ursTokenStr, String userIp) {
		MobileAccessToken mobileToken = new MobileAccessToken();
		// 根据urs的token进行校验，获取用户名等信息
		StringBuilder url = new StringBuilder(256);
		url.append(this.checkURSTokenUrl);
		url.append("?id=").append(initId).append("&token=").append(ursTokenStr).append("&userip=").append(userIp);
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when Validate URS token!", e);
			mobileToken.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
			mobileToken.setMessage(MobileErrorCode.LOGIN_FAIL.getDesc());
			return mobileToken;
		}
		// Sample:"201\nusername=abc2003@163.com&loginTime=20140509&product=miss&aliasuser=abc";
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 1) {
			// 内容第一行为返回的代码。
			if (!"201".equals(contentArr[0])) {
				// 验证失败。
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Validate URS token request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				mobileToken.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
				mobileToken.setMessage(MobileErrorCode.LOGIN_FAIL.getDesc());
				return mobileToken;
			}
			// 验证成功，内容第三行为"username=&loginTime=&product=&aliasuser="。
			String[] ursResults = StringUtils.split(contentArr[2], "&");
			if (null != ursResults && ursResults.length > 0) {
				for (String result : ursResults) {
					String[] tempArray = StringUtils.split(result, "=");
					if (null != tempArray && tempArray.length > 1) {
						if (result.contains("username")) {
							mobileToken.setUserName(FullUserNameUtils.getFullUserName(tempArray[1]));
						}
					}
				}
			}
		}
		return mobileToken;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#validateOAuthToken(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MobileAccessToken validateOAuthToken(String id, String ursTokenStr, String userName, String initKey) {
		MobileAccessToken mobileToken = new MobileAccessToken();
		// 根据urs oauth的token获取第三方的accessToke和UID
		StringBuilder url = new StringBuilder(256);
		url.append(checkOAuthTokenUrl);
		url.append("?id=").append(id).append("&token=").append(ursTokenStr);
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when Validate URS OAuth token!", e);
			mobileToken.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
			mobileToken.setMessage(MobileErrorCode.LOGIN_FAIL.getDesc());
			return mobileToken;
		}
		// Sample:"201\nOK\nresult=CC56755139E572539889A...";
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 2) {
			// 内容第一行为返回的代码。
			if (!"201".equals(contentArr[0])) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Validate URS OAuth token request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				// 验证失败。
				mobileToken.setCode(MobileErrorCode.LOGIN_FAIL.getIntValue());
				mobileToken.setMessage(MobileErrorCode.LOGIN_FAIL.getDesc());
				return mobileToken;
			}
			mobileToken.setUserName(userName);
			// 验证成功，内容第三行为"result=CC56755139E572539889A..."，并且为加密字符，result解密后为"access_token=&uid="。
			String[] resultArr = StringUtils.split(contentArr[2], "=");
			if (null == resultArr || resultArr.length < 2) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Retrieved result from URS OAuth: {} for user {}", resultArr, userName);
				}
				// result的字符段错误，跳过昵称的获取
				return mobileToken;
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Going to validate URS OAuth token: {} for user {} with key {}", resultArr[1], userName,
						initKey);
			}
			// 解密token和uid信息
			Map<String, String> decryptResultMap = decryptURSToken(resultArr[1], initKey);
			if (null != decryptResultMap && decryptResultMap.size() > 0) {
				// 根据accessToke和UID获取第三方的昵称
				String accessToken = decryptResultMap.get("access_token");
				String ursUID = decryptResultMap.get("uid");
				String uid = null;
				if (null != ursUID) {
					// 获取到的为URS的UID，例如236714763@sina.163.com,格式为[uid]@[app].163.com
					String[] uidArr = StringUtils.split(ursUID, "@");
					if (null != uidArr && uidArr.length > 0) {
						uid = uidArr[0];
					} else {
						uid = ursUID;
					}
				}
				MobileAccessToken mobileAccessToken = getNickNameFromExt(accessToken, uid, userName);
				if (mobileAccessToken != null) {
					mobileToken.setNickName(mobileAccessToken.getNickName());
					mobileToken.setProfileImage(mobileAccessToken.getProfileImage());
				}
			}
		}
		return mobileToken;
	}

	/**
	 * 根据accessToke和UID获取第三方的昵称等信息
	 * 
	 * @param accessToken
	 * @param uid
	 * @param userName
	 * @return
	 */
	private MobileAccessToken getNickNameFromExt(String accessToken, String uid, String userName) {
		// 根据accessToke和UID获取第三方的昵称
		if (StringUtils.isNotBlank(userName)) {
			String[] userNameArr = StringUtils.split(userName, "@");
			if (null != userNameArr && userNameArr.length > 1) {
				String[] suffixArr = StringUtils.split(userNameArr[1], ".");
				if (null != suffixArr && StringUtils.isNotBlank(suffixArr[0])) {
					ThirdPartyApp thirdPartyApp = context.getBean(suffixArr[0] + "App", ThirdPartyApp.class);
					try {
						return null == thirdPartyApp ? null : thirdPartyApp.retrieveNickName(uid, accessToken);
					} catch (Exception e) {
						LOGGER.error("Exception occurs when retrieve nick name!", e);
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#genMobileToken(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String genMobileToken(String userName, String key) {
		// 生成手机应用访问的token
		StringBuilder content = new StringBuilder(256);
		// 随机Long + userName + currentTimeMills
		content.append(RandomUtils.nextLong()).append(userName).append(System.currentTimeMillis());
		return DigestUtils.byte2Hex(DigestUtils.encryptAES(content.toString(), DigestUtils.hex2Byte(key)));
	}

	/**
	 * @return the checkOAuthTokenUrl
	 */
	public String getCheckOAuthTokenUrl() {
		return checkOAuthTokenUrl;
	}

	/**
	 * @param checkOAuthTokenUrl
	 *            the checkOAuthTokenUrl to set
	 */
	public void setCheckOAuthTokenUrl(String checkOAuthTokenUrl) {
		this.checkOAuthTokenUrl = checkOAuthTokenUrl;
	}

	/**
	 * @return the checkURSTokenUrl
	 */
	public String getCheckURSTokenUrl() {
		return checkURSTokenUrl;
	}

	/**
	 * @param checkURSTokenUrl
	 *            the checkURSTokenUrl to set
	 */
	public void setCheckURSTokenUrl(String checkURSTokenUrl) {
		this.checkURSTokenUrl = checkURSTokenUrl;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#encryptMobileResult(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String encryptMobileResult(String result, String initKey) {
		return DigestUtils.byte2Hex(DigestUtils.encryptAES(result, DigestUtils.hex2Byte(initKey)));
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.security.service.MobileAuthcService#exchangeURSToken(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> exchangeURSToken(String id, String params, String initKey) {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuilder url = new StringBuilder().append(exchangeURSTokenUrl);
		url.append("?id=").append(id).append("&params=").append(params);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Send exchange URS Token request: {}", url.toString());
		}
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when exchange URS Token!", e);
			resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
			resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
			return resultMap;
		}
		if (StringUtils.isNotBlank(content)) {
			@SuppressWarnings("unchecked")
			Map<String, String> jsonResultMap = JsonUtils.fromJson(content, Map.class);
			// 内容第一行为返回的代码。
			if ("200".equals(jsonResultMap.get("retCode"))) {
				// 获取成功，内容第三行为加密的"username=&token="。
				String encryptedResult = jsonResultMap.get("result");
				if (StringUtils.isNotBlank(encryptedResult)) {
					resultMap = decryptExchangeToken(encryptedResult, initKey);
				}
				if (resultMap == null || resultMap.size() == 0) {
					resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
					resultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Exchange URS Token request failed: {} : {}", jsonResultMap.get("retCode"),
							jsonResultMap.get("retDesc"));
				}
				// 获取失败，内容第二行为提示信息。
				resultMap.put("code", String.valueOf(MobileErrorCode.LOGIN_FAIL.getIntValue()));
				resultMap.put("message", jsonResultMap.get("retDesc"));
			}
		}
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> decryptExchangeToken(String encryptedResult, String initKey) {
		String decryptedResult = DigestUtils.decryptAES(DigestUtils.hex2Byte(encryptedResult),
				DigestUtils.hex2Byte(initKey));
		// 解密后的格式：{"username":"123232123@sina.163.com","token":"1f9d9cf85123beb5731233c67a27578cf"}
		if (StringUtils.isNotBlank(decryptedResult)) {
			return JsonUtils.fromJson(decryptedResult, Map.class);
		}
		return null;
	}

	/**
	 * @return the exchangeURSTokenUrl
	 */
	public String getExchangeURSTokenUrl() {
		return exchangeURSTokenUrl;
	}

	/**
	 * @param exchangeURSTokenUrl
	 *            the exchangeURSTokenUrl to set
	 */
	public void setExchangeURSTokenUrl(String exchangeURSTokenUrl) {
		this.exchangeURSTokenUrl = exchangeURSTokenUrl;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileAuthcService#getUserInfoFromExt(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MobileAccessToken getUserInfoFromExt(String id, String params, String initKey, String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		Map<String, String> decryptResultMap = decryptURSToken(params, initKey);
		return getNickNameFromExt(decryptResultMap.get("access_token"), StringUtils.split(username, "@")[0], username);
	}

	@Override
	public Map<String, String> loginFromMobile(String userName, String password) {
		NameValuePair userNameParam = new BasicNameValuePair("username", userName);
		NameValuePair passWordParam = new BasicNameValuePair("password", password);
		NameValuePair redirectURLParam = new BasicNameValuePair("redirectURL", "http://mobile.baiwandian.cn/m/");
		NameValuePair typeParam = new BasicNameValuePair("type", "3");
		NameValuePair[] param = new NameValuePair[]{userNameParam, passWordParam, redirectURLParam, typeParam};
		HttpResponse response = HttpUtil.sendPost("http://denglu.baiwandian.cn/login", param);
		Header[] hearders = response.getHeaders("Set-Cookie");
		String xylun = null, xylsess = null, xylexp = null, xylerr = null;
		for (Header header : hearders) {
			String[] keys = header.getValue().trim().split(";");
			for (String key : keys) {
				if (key.indexOf("XYLUN") == 0) {
					xylun = key.substring("XYLUN".length() + 1);
					break;
				} else if (key.indexOf("XYLSESS") == 0) {
					xylsess = key.substring("XYLSESS".length() + 1);
					break;
				} else if (key.indexOf("XYLEXP") == 0) {
					xylexp = key.substring("XYLEXP".length() + 1);
					break;
				} else if (key.indexOf("XYLERR") == 0) {
					xylerr = key.substring("XYLERR".length() + 1);
					break;
				}
			}
		}
		Map<String, String> map = new HashMap<String, String>(3);
		if (StringUtils.isBlank(xylerr) || StringUtils.equals("\"\"", xylerr)) {
			if (StringUtils.isNotBlank(xylun) && !StringUtils.equals("\"\"", xylun) 
					&& StringUtils.isNotBlank(xylsess) && !StringUtils.equals("\"\"", xylsess) 
					&& StringUtils.isNotBlank(xylexp) && !StringUtils.equals("\"\"", xylexp)) {
				map.put(MmallConstant.XYL_MAINSITE_USERNAME, xylun);
				map.put(MmallConstant.XYL_MAINSITE_SESS, xylsess);
				map.put(MmallConstant.XYL_MAINSITE_EXPIRES, xylexp);
			} else {
				map.put(MmallConstant.XYL_MAINSITE_FAILED, "login failed!");
			}
		} else {
			try {
				map.put(MmallConstant.XYL_MAINSITE_FAILED, URLDecoder.decode(xylerr, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Wrong character!", e);
				map.put(MmallConstant.XYL_MAINSITE_FAILED, "登录失败！");
			}
		}
		return map;
	}
}
