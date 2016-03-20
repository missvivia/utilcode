package com.xyl.mmall.mobile.facade.converter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.framework.util.JsonUtils;

public class MobileConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 一定要配的部分 */
	public static String version = "1.1.0";
	public static String version_desc = "有新版本1.2\r\n \r\n *可直接使用支付宝付款\r\n*支持自定义特卖时间提醒\r\n*增加更多商品筛选维度";
	public static String mobile_web_url = "http://m.023.baiwandian.cn";

	public static String mobile_pay_url = "http://023.baiwandian.cn";

	public static String mobile_init_image_max = "";//"http://paopao.nos.netease.com/6e0468f5-4543-41a8-bfb6-aea35320c491";

	public static HashMap<String, String> mobile_init_image_map = new HashMap<String, String>();
	public static String mobile_init_image_linkto = "";
	public static HashMap<String, String> android_channl_download = new HashMap<String, String>();
	public static synchronized String getMobileInitImageMap(String size){
		if (mobile_init_image_map.isEmpty()) {
			return null;
		}
		String url = mobile_init_image_map.get(size);
		if(StringUtils.isBlank(url)){
			String near_size = Converter.findSmailSize(size, mobile_init_image_map.keySet());
			url = mobile_init_image_map.get(near_size);
		}
		return url;
	}
	public static synchronized String getAndroidChannlDownload(String channl){
		if (android_channl_download.isEmpty()) {
			return "";
			/*android_channl_download.put("normal", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("google", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("qihu360", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("baidu", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("xiaomi", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("wandoujia", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("tencent", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("netease", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("gphone", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("zhushou91", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("hiapk", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("anzhi", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("yingyonghui", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");
			android_channl_download.put("others", "http://023.baiwandian.cn/res/apps/vstore_v1.0.0_normal.apk");*/
		}
		String url = android_channl_download.get(channl);
		if(StringUtils.isBlank(url))
			url = android_channl_download.get("normal");
		return url;
	}
	
	public static int islock = -1;
	public static String lock_version = "";
	public static String lock_area = "";
	public static String share_po_template = "最新活动：";
	public static String share_po_template_title = "抢专柜新款折扣，尽在新云联百万店";
	public static String share_po_template_body = "我喜欢的#brand正在新云联百万店上#off折抢购，超多专柜同步新款，够给力！也有你爱的品牌哦~";
	public static String share_po_template_image = "http://paopao.nos.netease.com/6688527b-79ff-4f11-9bbf-cd655a97054a";
	
	public static String share_prdt_template = "最新商品：";
	public static String share_prdt_template_title = "抢专柜新款折扣，尽在新云联百万店";
	public static String share_prdt_template_body = "#price元！抢\"#brand\"#product，任性购专柜同步新款！抢专属新品折扣，从上新云联百万店开始！赶紧猛戳";
	public static String share_gift_templage = "";
	public static String share_gift_template_title = "人品大爆发";
	public static String share_gift_template_body = "送您mmall红包，抢专柜折扣！那件幸福的小事，是每天都有新衣服！";
	public static String share_gift_template_image = "http://paopao.nos.netease.com/21c1d05b-2c34-445c-a968-fc3fe142a96c";

	public static String share_logo_templage = "http://paopao.nos.netease.com/6688527b-79ff-4f11-9bbf-cd655a97054a";
	/** 首页不在分类里显示 */
	public static int new_po_type = 1;

	/** 首页不在分类里显示 */
	public static String active_platform_info = "全场满288包邮";

	/** 购物车最大数 */
	public static int cart_full_count = 10;

	/** 状态信息 1:正常 2:已过期 3:还未开始 4:抢光了 */
	public static String prdt_status_1 = "";

	public static String prdt_status_2 = "已过期";

	public static String prdt_status_3 = "还未开始";

	public static String prdt_status_4 = "抢光了";

	public static String cart_item_status_1 = "删除";

	public static String cart_item_status_2 = "已过期";

	public static String cart_item_status_3 = "抢光了";

	// 正常
	public static String sku_status_1 = "";

	public static String sku_status_2 = "超时";

	public static String sku_status_3 = "售罄";

	public static String sku_status_4 = "过期";

	public static String sku_status_5 = "已删除";

	public static String no_discount_title = "";

	public static String discount_tail = "折起";

	public static String discount_tail2 = "折";

	public static int future_day = 4;

	public static int future_number = 500;

	public static String po_new_titil = "最新抢购 每天<font color=\"#d951b5\">10点</font>开始";

	public static String po_new_titil_before10 = "今天<font color=\"#d951b5\">10点</font>开始";
	
	public static String brand_po_today_titil = "<font color=\"#d951b5\">正在抢购</font>";

	public static String po_today_titil = "最新抢购 每天<font color=\"#d951b5\">10点</font>开始";

	public static String po_group_titil = "10点开始";

	public static String po_group_titil_color = "<font color=\"#d951b5\">10点</font>开始";

	public static String same_as_shop_tag = "专柜同步";
	public static String same_as_shop_icon = "http://paopao.nos.netease.com/c56ca873-4c24-41f8-83c1-674299deec85";
	
	
	static {
		ConfigLoader c = new ConfigLoader();
		c.start();
	}
	
	
	public static int getNew_po_type() {
		return new_po_type;
	}

	public static void setNew_po_type(int new_po_type) {
		MobileConfig.new_po_type = new_po_type;
	}

	public static int getCart_full_count() {
		return cart_full_count;
	}

	public static void setCart_full_count(int cart_full_count) {
		MobileConfig.cart_full_count = cart_full_count;
	}

	public static String getPrdt_status_1() {
		return prdt_status_1;
	}

	public static void setPrdt_status_1(String prdt_status_1) {
		MobileConfig.prdt_status_1 = prdt_status_1;
	}

	public static String getPrdt_status_2() {
		return prdt_status_2;
	}

	public static void setPrdt_status_2(String prdt_status_2) {
		MobileConfig.prdt_status_2 = prdt_status_2;
	}

	public static String getPrdt_status_3() {
		return prdt_status_3;
	}

	public static void setPrdt_status_3(String prdt_status_3) {
		MobileConfig.prdt_status_3 = prdt_status_3;
	}

	public static String getPrdt_status_4() {
		return prdt_status_4;
	}

	public static void setPrdt_status_4(String prdt_status_4) {
		MobileConfig.prdt_status_4 = prdt_status_4;
	}

	public static String getCart_item_status_1() {
		return cart_item_status_1;
	}

	public static void setCart_item_status_1(String cart_item_status_1) {
		MobileConfig.cart_item_status_1 = cart_item_status_1;
	}

	public static String getCart_item_status_2() {
		return cart_item_status_2;
	}

	public static void setCart_item_status_2(String cart_item_status_2) {
		MobileConfig.cart_item_status_2 = cart_item_status_2;
	}

	public static String getCart_item_status_3() {
		return cart_item_status_3;
	}

	public static void setCart_item_status_3(String cart_item_status_3) {
		MobileConfig.cart_item_status_3 = cart_item_status_3;
	}

	public static String getSku_status_1() {
		return sku_status_1;
	}

	public static void setSku_status_1(String sku_status_1) {
		MobileConfig.sku_status_1 = sku_status_1;
	}

	public static String getSku_status_2() {
		return sku_status_2;
	}

	public static void setSku_status_2(String sku_status_2) {
		MobileConfig.sku_status_2 = sku_status_2;
	}

	public static String getSku_status_3() {
		return sku_status_3;
	}

	public static void setSku_status_3(String sku_status_3) {
		MobileConfig.sku_status_3 = sku_status_3;
	}

	public static String getSku_status_4() {
		return sku_status_4;
	}

	public static void setSku_status_4(String sku_status_4) {
		MobileConfig.sku_status_4 = sku_status_4;
	}

	public static String getSku_status_5() {
		return sku_status_5;
	}

	public static void setSku_status_5(String sku_status_5) {
		MobileConfig.sku_status_5 = sku_status_5;
	}

	public static String getNo_discount_title() {
		return no_discount_title;
	}

	public static void setNo_discount_title(String no_discount_title) {
		MobileConfig.no_discount_title = no_discount_title;
	}

	public static String getDiscount_tail() {
		return discount_tail;
	}

	public static void setDiscount_tail(String discount_tail) {
		MobileConfig.discount_tail = discount_tail;
	}

	public static String getDiscount_tail2() {
		return discount_tail2;
	}

	public static void setDiscount_tail2(String discount_tail2) {
		MobileConfig.discount_tail2 = discount_tail2;
	}

	public static int getFuture_day() {
		return future_day;
	}

	public static void setFuture_day(int future_day) {
		MobileConfig.future_day = future_day;
	}

	public static int getFuture_number() {
		return future_number;
	}

	public static void setFuture_number(int future_number) {
		MobileConfig.future_number = future_number;
	}

	public static String getPo_today_titil() {
		return po_today_titil;
	}

	public static void setPo_today_titil(String po_today_titil) {
		MobileConfig.po_today_titil = po_today_titil;
	}

	public static String getPo_group_titil() {
		return po_group_titil;
	}

	public static void setPo_group_titil(String po_group_titil) {
		MobileConfig.po_group_titil = po_group_titil;
	}

	public static String getMobile_web_url() {
		return mobile_web_url;
	}

	public static void setMobile_web_url(String mobile_web_url) {
		MobileConfig.mobile_web_url = mobile_web_url;
	}

	public static String getMobile_pay_url() {
		return mobile_pay_url;
	}

	public static void setMobile_pay_url(String mobile_pay_url) {
		MobileConfig.mobile_pay_url = mobile_pay_url;
	}

	public static String getActive_platform_info() {
		return active_platform_info;
	}

	public static void setActive_platform_info(String active_platform_info) {
		MobileConfig.active_platform_info = active_platform_info;
	}

	public static String getMobile_init_image_max() {
		return mobile_init_image_max;
	}

	public static void setMobile_init_image_max(String mobile_init_image_max) {
		MobileConfig.mobile_init_image_max = mobile_init_image_max;
	}

	public static void setMobile_init_image_map(HashMap<String, String> mobile_init_image_map) {
		MobileConfig.mobile_init_image_map = mobile_init_image_map;
	}

	public static String getShare_po_template() {
		return share_po_template;
	}

	public static void setShare_po_template(String share_po_template) {
		MobileConfig.share_po_template = share_po_template;
	}

	public static String getShare_prdt_template() {
		return share_prdt_template;
	}

	public static void setShare_prdt_template(String share_prdt_template) {
		MobileConfig.share_prdt_template = share_prdt_template;
	}

	public static String getShare_gift_templage() {
		return share_gift_templage;
	}

	public static void setShare_gift_templage(String share_gift_templage) {
		MobileConfig.share_gift_templage = share_gift_templage;
	}

	public static String getPo_new_titil() {
		return po_new_titil;
	}

	public static void setPo_new_titil(String po_new_titil) {
		MobileConfig.po_new_titil = po_new_titil;
	}

}
