package com.xyl.mmall.bi.core.enums;

/**
 * 具体页面/操作.
 * 
 * @author wangfeng
 * 
 */
public enum BIType {

	UNKNOWN("unknown", "未知页面/操作"),

	NEWPAGE("newPage", "最新页面"), FINDPAGE("findPage", "发现页面"),

	MYPAGE("myPage", "个人页面"), HOMEPAGE("homePage", "首页"),

	CARTPAGE("cartPage", "购物车页面"), WOMENSWEARPAGE("womenswearPage", "女装分类页面"),

	MENSWEARPAGE("menswearPage", "男装分类页面"), SHOEPAGE("shoePage", "鞋包分类页面"),

	CHILDSWEARPAGE("childswearPage", "童装分类页面"), TEXTILEPAGE("textilePage", "家纺分类页面"),

	BRANDDETAILPAGE("brandDetailPage", "品牌mini页"), BRANDLISTPAGE("brandListPage", "入驻品牌列表页"),

	POPAGE("poPage", "档期页面"), GOODSPAGE("goodsPage", "商品详情页"),

	CHANGEPAYMENT("changePayment", "修改付款方式"),

	ADDTOCART("addToCart", "加入购物车"),

	TOPAY("toPay", "结算/去付款"), CANCELORDER("cancelOrder", "取消订单"),

	ORDERDETAILPAGE("orderDetailPage", "订单详情页"), ORDERCONFIRMPAGE("orderConfirmPage", "填写订单页面"),

	ORDERSUBMITPAGE("orderSubmitPage", "提交订单页面"), MYFAVORITEPAGE("myFavoritePage", "我的关注页面"),

	RETURN_GOODS_RULE_PAGE("returnGoodsRulePage", "退货说明页面"),

	RETURN_GOODS_APPLY_PAGE("returnGoodsApplyPage", "退货申请页面"),

	RETURN_GOODS_INFO_PAGE("returnGoodsInfoPage", "填写退货信息页面"),

	RETURN_GOODS_BACK_PAGE("returnGoodsBackPage", "退回商品页面"),

	RETURN_GOODS_SERVICE_PAGE("returnGoodsServicePage", "客服退款页面"),

	PERSONAL_DATA_PAGE("personalDataPage", "个人资料页面"),

	ADDRESS_PAGE("addressPage", "收货地址页面"),

	COUPON_PAGE("couponPage", "优惠券红包页面"),

	/**
	 * 下面的枚举是处理点击行为;
	 */
	FOLLOWBRAND("followBrand", "关注品牌"), 
	DEFOLLOWBRAND("defollowBrand", "取消关注品牌"), 

	/**
	 * clientType Order
	 */
	SKU_SELL_STATISTICS_ERP("skuSellStatisticsERP", "销售统计(erp)"),
	SKU_SELL_STATISTICS_CMS("skuSellStatisticsCMS", "销售统计(cms)"),
	SKU_SELL_STATISTICS_MAINSITE("skuSellStatisticsMainsite", "销售统计(mainsite)"),
	
	/**
	 * clientType CMS
	 */
	CMS_USER_REGIST("cmsUserRegist", "cms添加用户");
	
	/**
	 * 值
	 */
	private final String value;

	/**
	 * 描述
	 */
	private final String desc;

	private BIType(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public BIType genEnumByValue(String strValue) {
		for (BIType item : BIType.values()) {
			if (item.value.equalsIgnoreCase(strValue))
				return item;
		}
		return UNKNOWN;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
