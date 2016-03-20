package com.xyl.mmall.framework.util;

import java.util.ResourceBundle;

/**
 * 
 * @author author:lhp
 * 
 * @version date:2015年6月30日下午8:22:48
 */
public class UrlBaseUtil {

	private static final ResourceBundle rs = ResourceBundle.getBundle("config.application");

	private static String MAINSITE_SERVER = null;

	static {
		String server = rs.getString("mainsite.server");
		String port = rs.getString("mainsite.server.port");
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		if ("80".equals(port)) {
			sb.append(server);
		} else {
			sb.append(server).append(":").append(port);
		}
		MAINSITE_SERVER = sb.toString();
	}

	/**
	 * 生成商品产品Detail页面的访问URL
	 * 
	 * @param productId 产品ID
	 * @return
	 */
	public static String buildProductUrl(long productId) {
		StringBuffer url = new StringBuffer();
		url.append(MAINSITE_SERVER).append("/product/detail?skuId=").append(productId);
		return url.toString();
	}

	/**
	 * 根据相对路径RUL生成绝对URL
	 * 
	 * @param productId 产品ID
	 * @return
	 */
	public static String buildFullPathUrl(String linkUrl) {
		StringBuffer url = new StringBuffer();
		url.append(MAINSITE_SERVER).append(linkUrl);
		return url.toString();
	}

	/**
	 * 店铺url
	 * 
	 * @param storeId 商铺ID即商家 businessId
	 * @return
	 */
	public static String buildStoreUrl(long storeId) {
		StringBuilder url = new StringBuilder();
		url.append(MAINSITE_SERVER).append("/store/").append(storeId).append("/");
		return url.toString();
	}
	
	/**
	 * 商品快照url
	 * @param skuId
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public static String buildSkuSnapShotUrl(long skuId, long orderId, long userId) {
		StringBuffer url = new StringBuffer(MAINSITE_SERVER);
		url.append("/product/snapShot?skuId=").append(skuId);
		url.append("&orderId=").append(orderId);
		url.append("&userId=").append(userId);
		return url.toString();
	}

}
