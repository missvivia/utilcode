/**
 * 
 */
package com.xyl.mmall.security.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.xyl.mmall.security.thirdparty.ThirdPartyApp;
import com.xyl.mmall.security.token.MobileAccessToken;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * @author lihui
 *
 */
public class MainSiteAuthcServiceImpl implements MainSiteAuthcService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainSiteAuthcServiceImpl.class);

	private String retrieveOauthAccessTokenUrl = null;

	private ApplicationContext context;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MainSiteAuthcService#getNickNameFromExt(java.lang.String)
	 */
	@Override
	public MobileAccessToken getNickNameFromExt(String userName) {
		// 根据urs oauth的token获取第三方的accessToke和UID
		StringBuilder url = new StringBuilder(256);
		url.append(retrieveOauthAccessTokenUrl);
		url.append("?username=").append(userName);
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when Validate URS OAuth user name!", e);
			return null;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Retrieve Oauth Access Token result: {} for user {}", content, userName);
		}
		// Sample:"201\nOK\naccess_token=D9823hjbhge7s6&uid=123456789";
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 2) {
			// 内容第一行为返回的代码。
			if (!"201".equals(contentArr[0])) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Validate URS OAuth user name request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				// 验证失败。
				return null;
			}
			// 验证成功，内容第二行为"access_token=&uid="。
			String[] ursResults = StringUtils.split(contentArr[2], "&");
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Going to validate URS OAuth token: {} for user {}", contentArr[2], userName);
			}
			if (null != ursResults && ursResults.length > 0) {
				// 根据accessToke和UID获取第三方的昵称
				String accessToken = null;
				String uid = null;
				for (String result : ursResults) {
					String[] tempArray = StringUtils.split(result, "=");
					if (null != tempArray && tempArray.length > 1) {
						if (result.contains("access_token")) {
							accessToken = tempArray[1];
						}
						if (result.contains("uid")) {
							String[] uidArr = StringUtils.split(tempArray[1], "@");
							if (null != uidArr && uidArr.length > 0) {
								uid = uidArr[0];
							} else {
								uid = tempArray[1];
							}
						}
					}
				}
				return getNickNameFromExt(accessToken, uid, userName);
			}
		}
		return null;
	}

	/**
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
	 * @return the retrieveOauthAccessTokenUrl
	 */
	public String getRetrieveOauthAccessTokenUrl() {
		return retrieveOauthAccessTokenUrl;
	}

	/**
	 * @param retrieveOauthAccessTokenUrl
	 *            the retrieveOauthAccessTokenUrl to set
	 */
	public void setRetrieveOauthAccessTokenUrl(String retrieveOauthAccessTokenUrl) {
		this.retrieveOauthAccessTokenUrl = retrieveOauthAccessTokenUrl;
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

}
