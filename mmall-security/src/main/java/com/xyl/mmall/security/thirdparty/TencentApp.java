/**
 * 
 */
package com.xyl.mmall.security.thirdparty;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.security.token.MobileAccessToken;
import com.xyl.mmall.security.utils.HttpUtils;

/**
 * QQ互联认证
 * 
 * @author lihui
 *
 */
public class TencentApp implements ThirdPartyApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(TencentApp.class);

	private String clientId = null;

	private String userInfoUrl = null;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.thirdparty.ThirdPartyApp#retrieveNickName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public MobileAccessToken retrieveNickName(String uid, String accessToken) throws Exception {
		if (StringUtils.isBlank(uid) || StringUtils.isBlank(accessToken)) {
			return null;
		}
		// 1.请求链接
		StringBuilder getUserInfoURL = new StringBuilder(256);
		getUserInfoURL.append(userInfoUrl);
		getUserInfoURL.append("?access_token=").append(accessToken);
		getUserInfoURL.append("&oauth_consumer_key=").append(clientId);
		getUserInfoURL.append("&openid=").append(uid);
		// 2.根据用户ID获取用户信息
		String responseContent = HttpUtils.getContent(getUserInfoURL.toString());
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Retrieved Tencent user {} info : {}", uid, responseContent);
		}
		// 3.解析返回数据
		if (StringUtils.isNotBlank(responseContent)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultMap = JsonUtils.fromJson(responseContent, HashMap.class);
			if (CollectionUtil.isNotEmptyOfMap(resultMap)) {
				MobileAccessToken mobileAccessToken = new MobileAccessToken();
				mobileAccessToken.setNickName((String) resultMap.get("nickname"));
				Object image = resultMap.get("figureurl_qq_2");
				if (image == null || StringUtils.isBlank((String) image)) {
					image = resultMap.get("figureurl_qq_1");
				}
				mobileAccessToken.setProfileImage((String) image);
				return mobileAccessToken;
			} else {
				LOGGER.error("Failed to retrieve nick name from Tencent, " + responseContent);
			}
		}
		return null;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @param userInfoUrl
	 *            the userInfoUrl to set
	 */
	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

}
