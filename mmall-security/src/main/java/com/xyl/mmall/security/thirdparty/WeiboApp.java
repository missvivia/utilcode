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
 * 新浪微博认证
 * 
 * @author lihui
 *
 */
public class WeiboApp implements ThirdPartyApp {

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
		usersShowURL.append("&uid=" + uid);
		// 2.根据用户ID获取用户信息
		String responseContent = HttpUtils.getContent(usersShowURL.toString());
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Retrieved Weibo user {} info : {}", uid, responseContent);
		}
		// 3.解析返回数据
		if (StringUtils.isNotBlank(responseContent)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultMap = JsonUtils.fromJson(responseContent, HashMap.class);
			if (CollectionUtil.isNotEmptyOfMap(resultMap)) {
				MobileAccessToken mobileAccessToken = new MobileAccessToken();
				mobileAccessToken.setNickName((String) resultMap.get("name"));
				Object image = resultMap.get("avatar_large");
				if (image == null || StringUtils.isBlank((String) image)) {
					image = resultMap.get("profile_image_url");
				}
				mobileAccessToken.setProfileImage((String) image);
				return mobileAccessToken;
			} else {
				LOGGER.error("Failed to retrieve nick name from Webibo, " + responseContent);
			}
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
