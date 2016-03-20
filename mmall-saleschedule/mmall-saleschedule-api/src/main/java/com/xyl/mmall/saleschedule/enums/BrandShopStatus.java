package com.xyl.mmall.saleschedule.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 品牌门店表状态
 * @author chengximing
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = BrandShopStatusJsonDeserializer.class)
public enum BrandShopStatus implements AbstractEnumInterface<BrandShopStatus> {

	/**
	 * NULL
	 */
	NULL(-1, "NULL"),
	/**
	 * 已停用
	 */
	SHOP_STOPED(0, "已停用"),
	/**
	 * 新建
	 */
	SHOP_NEW(1, "新建"),
	/**
	 * 已启用
	 */
	SHOP_USING(2, "已启用"),
	;
	/**
	 * 值
	 */
	private final int intValue;
	
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
	private BrandShopStatus(int v, String d) {
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
	public BrandShopStatus genEnumByIntValue(int intValue) {
		for (BrandShopStatus item : BrandShopStatus.values()) {
			if (item.intValue == intValue)
				return item;
		}
		return null;
	}
	

}
