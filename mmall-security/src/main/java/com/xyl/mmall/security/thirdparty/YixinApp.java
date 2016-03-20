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
 * 易信认证
 * 
 * @author lihui
 *
 */
public class YixinApp implements ThirdPartyApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboApp.class);

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
		StringBuilder usersShowURL = new StringBuilder(256);
		usersShowURL.append(userInfoUrl);
		usersShowURL.append("?access_token=" + accessToken);
		// 2.根据用户ID获取用户信息
		String responseContent = HttpUtils.getContent(usersShowURL.toString());
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Retrieved Yixin user {} info : {}", uid, responseContent);
		}
		// 3.解析返回数据
		if (StringUtils.isNotBlank(responseContent)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultMap = JsonUtils.fromJson(responseContent, HashMap.class);
			if (CollectionUtil.isNotEmptyOfMap(resultMap) && resultMap.containsKey("userinfo")) {
				@SuppressWarnings("unchecked")
				Map<String, String> userInfoMap = (Map<String, String>) resultMap.get("userinfo");
				if (CollectionUtil.isNotEmptyOfMap(userInfoMap) && userInfoMap.containsKey("nick")) {
					MobileAccessToken mobileAccessToken = new MobileAccessToken();
					mobileAccessToken.setNickName((String) userInfoMap.get("nick"));
					mobileAccessToken.setProfileImage((String) userInfoMap.get("icon"));
					return mobileAccessToken;
				}
				LOGGER.error("Failed to retrieve nick name from Yixin, " + responseContent);
			}
			LOGGER.error("Failed to retrieve nick name from Yixin, " + responseContent);
		}
		return null;
	}

	/**
	 * @param userInfoUrl
	 *            the userInfoUrl to set
	 */
	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

}
