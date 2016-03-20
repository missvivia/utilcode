package com.xyl.mmall.framework.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * CartItem类型
 * 
 * @author dingmingliang
 * 
 */
public enum CartItemType implements AbstractEnumInterface<CartItemType> {
	
	/**
	 * 普通产品
	 */
	CART_SKU(1, "普通产品"),	
	;

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
	private CartItemType(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public CartItemType genEnumByIntValue(int intValue) {
		for (CartItemType item : CartItemType.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public CartItemType genEnumByIntValueSt(int intValue) {
		return CartItemType.values()[0].genEnumByIntValue(intValue);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#getIntValue()
	 */
	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
