package com.xyl.mmall.framework.enums;

/**
 * 
 * ComposeOrderErrorCodeEnum.java created by lhp at 2016年1月14日 下午4:48:56
 * 这里对类或者接口作简要描述
 *
 * @author lhp
 */
public enum ComposeOrderErrorCodeEnum {

	ORDER_FAIL(0, "未知错误失败"), ORDER_STOCK_LACK(1, "库存不足"), ORDER_ADDRESS_NULL(2, "收货地址为空"), ORDER_BTACH_DISSTISFY(3,
			"购买明细为空或者不满足起批条件"), ORDER_PARAM_ERROR(4, "输入参数不对 "), ORDER_CASH_EXCEED(5, "订单总价金额超过1亿 "), ORDER_USER_NOTALLOW(
			6, "特许经营商家不允许非指定用户下单"), ORDER_SKUPRICE_CHANGED(7, "下单是商品价格发生变动"), ORDER_AREA_NOTSUPPORT(8,
			"您选的商品包含不支持当前地区配送"), ORDER_PRODUCTLIMIT_ERROR(9, "您选的商品包含超过限购数量或者时间"), ORDER_COUPON_INVALID(10, "优惠券无效"), ORDER_SUCCESS(
			200, "成功");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private ComposeOrderErrorCodeEnum(int v, String d) {
		value = v;
		desc = d;
	}

	public static ComposeOrderErrorCodeEnum genEnumByIntValue(int intValue) {
		for (ComposeOrderErrorCodeEnum item : ComposeOrderErrorCodeEnum.values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
