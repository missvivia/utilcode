/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CouponConfigType.java created by yydx811 at 2015年12月31日 上午10:15:57
 * 优惠券配置类型
 *
 * @author yydx811
 */
public enum CouponConfigType implements AbstractEnumInterface<CouponConfigType> {

	NULL(0, ""),
	
	NOOB(1, "新手券");
	
	private final int type;

	private final String desc;

	CouponConfigType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public CouponConfigType genEnumByIntValue(int intValue) {
		for (CouponConfigType couponConfigType : CouponConfigType.values()) {
			if (couponConfigType.getType() == intValue) {
				return couponConfigType;
			}
		}
		return NULL;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
