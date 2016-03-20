/**
 * 
 */
package com.xyl.mmall.security.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.security.utils.HttpUtils;

/**
 * @author lihui
 *
 */
public class URSAuthUserInfoServiceImpl implements URSAuthUserInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(URSAuthUserInfoServiceImpl.class);

	private String getURSOtherInfoUrl = null;

	private String product = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.URSAuthUserInfoService#getNicknameFromURS(java.lang.String)
	 */
	@Override
	public String getNicknameFromURS(String userName) {
		// 根据urs userName获取用户昵称
		StringBuilder url = new StringBuilder(256);
		url.append(getURSOtherInfoUrl);
		url.append("?username=").append(userName).append("&product=").append(product).append("&neednickname=1");
		String content = null;
		try {
			content = HttpUtils.getContent(url.toString());
		} catch (Exception e) {
			LOGGER.error("Exception occurs when retrieve URS user nick name!", e);
			return null;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Retrieve URS user {} nick name response: {}", userName, content);
		}
		// Sample:"201\nfullname=&gender=&email=&education=&occupation=&province=&income=&phone=&mobile=&mobileisactivated=&industry=&address=&postcode=&birthday=&nickname=";
		String[] contentArr = StringUtils.split(content, System.getProperty("line.separator"));
		if (null != contentArr && contentArr.length > 1) {
			// 内容第一行为返回的代码。
			if (!"201".equals(contentArr[0])) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Retrieve URS user nick name request failed: {} : {}", contentArr[0], contentArr[1]);
				}
				// 获取失败。
				return null;
			}
			// 获取成功。
			String[] ursResults = StringUtils.split(contentArr[2], "&");
			if (null != ursResults && ursResults.length > 0) {
				// 取出用户的昵称
				for (String result : ursResults) {
					String[] tempArray = StringUtils.split(result, "=");
					if (null != tempArray && tempArray.length > 1) {
						if (result.contains("nickname")) {
							try {
								return URLDecoder.decode(tempArray[1], "UTF-8");
							} catch (UnsupportedEncodingException e) {
								return null;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return the getURSOtherInfoUrl
	 */
	public String getGetURSOtherInfoUrl() {
		return getURSOtherInfoUrl;
	}

	/**
	 * @param getURSOtherInfoUrl
	 *            the getURSOtherInfoUrl to set
	 */
	public void setGetURSOtherInfoUrl(String getURSOtherInfoUrl) {
		this.getURSOtherInfoUrl = getURSOtherInfoUrl;
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

}
