/**
 * 
 */
package com.xyl.mmall.content.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzlihui2014
 *
 */
public enum GiftType implements AbstractEnumInterface<GiftType> {

	/**
	 * 
	 */
	NULL(-1, "未知"),
	/**
	 * 特等奖
	 */
	SPECIAL(0, "获得特等奖：巴厘岛双人游"),
	/**
	 * 一等奖
	 */
	FIRST(1, "获得一等奖：自拍神器"),
	/**
	 * 二等奖
	 */
	SECOND(2, "获得二等奖：洗脸神器"),
	/**
	 * 三等奖
	 */
	THIRD(3, "获得三等奖：补水神器"),
	/**
	 * 四等奖
	 */
	FOURTH(4, "获得四等奖：100元抵扣券"),
	/**
	 * 旅行箱奖
	 */
	SUITCASE(5, "下单满1288元获赠网易旅行箱"),
	/**
	 * 免单奖
	 */
	FREE(6, "在找mmall中抽到了免单大奖"),
	/**
	 * mmall奖
	 */
		BUBBLE(7, "找齐了mmall成功兑换20元优惠券"),
	
	FIVE(50, "5元"),
	TEN(51, "10元"),
	TWENTY(52, "20元"),
	THIRTY(53, "30元");

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
	private GiftType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		return value;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public GiftType genEnumByIntValue(int intValue) {
		for (GiftType item : GiftType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
