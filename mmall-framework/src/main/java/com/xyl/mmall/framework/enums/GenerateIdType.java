/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * GenerateIdType.java created by yydx811 at 2015年6月25日 下午7:55:40
 * generateid 类型
 *
 * @author yydx811
 */
public enum GenerateIdType implements AbstractEnumInterface<GenerateIdType> {
	
	ORDER(1, "订单id"),
	
	COUPON(2, "红包/优惠券id"),
	
	MODEL(3, "模型id"),
	
	MODEL_PARAM(4, "模型属性id"),

	MODEL_SPECI(5, "模型规格id"),
	
	SKU(6, "商品id"),
	
	BUSINESS(7, "商家id"),
	
	USER(8, "用户id"),
	
	OTHER(9, "其他id");

	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private GenerateIdType(int v, String d) {
		value = v;
		desc = d;
	}

	public String getDesc() {
		return desc;
	}
	
	@Override
	public int getIntValue() {
		return value;
	}

	@Override
	public GenerateIdType genEnumByIntValue(int intValue) {
		for (GenerateIdType item : GenerateIdType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

}
