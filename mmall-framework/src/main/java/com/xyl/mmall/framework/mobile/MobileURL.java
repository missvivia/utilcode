package com.xyl.mmall.framework.mobile;

import com.alibaba.dubbo.common.utils.StringUtils;

public class MobileURL {
	private final static String app_url_1 = "mmall-link://pagestr=";
	private final static String app_url_2 = "?itemId=";
	//1.专场
	public final static int TYPE_PO = 1;
	//1.单品
	public final static int TYPE_PRODUCT = 2;
	//单品
	public final static int TYPE_BRAND = 3;
	//类目
	public final static int TYPE_CATEGORY = 4;
	//订单详情
	public final static int TYPE_ORDER = 5;
	//订单详情
	public final static int TYPE_ORDER_LIST = 6;
	
	//购物车
	public final static int TYPE_CART = 7;
	//红包
	public final static int TYPE_GIFT = 8;
	//红包
	public final static int TYPE_COUPON = 9;

	public static String genMobilePageLink(int type, String id) {
		StringBuilder sb = new StringBuilder(app_url_1);
		sb.append(type);
		if (!StringUtils.isBlank(id)) {
			sb.append(app_url_2);
			sb.append(id);
		}
		return sb.toString();
	}

}
