package com.xyl.mmall.saleschedule.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 品牌的推荐概率分类
 * @author chengximing
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = BrandProbabilityJsonDeserializer.class)
public enum BrandProbability implements AbstractEnumInterface<BrandProbability> {

	/**
	 * A类品牌，推荐的概率最高
	 */
	CATEGORY_TYPE_A(0, "A类品牌"),
	/**
	 * B类品牌，推荐的概率中等
	 */
	CATEGORY_TYPE_B(1, "B类品牌"),
	/**
	 * C类品牌，推荐的概率最低
	 */
	CATEGORY_TYPE_C(2, "C类品牌"),
	;
	
	private final int intValue;
	
	private final String desc;
	
	private BrandProbability(int v, String d) {
		intValue = v;
		desc = d;
	}
	
	public String getDesc() {
		return desc;
	}
	
	@Override
	public int getIntValue() {
		return intValue;
	}
	
	@Override
	public BrandProbability genEnumByIntValue(int intValue) {
		for (BrandProbability item : BrandProbability.values()) {
			if (item.intValue == intValue)
				return item;
		}
		return null;
	}
}
