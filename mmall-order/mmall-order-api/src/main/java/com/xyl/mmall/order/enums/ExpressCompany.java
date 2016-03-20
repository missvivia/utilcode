package com.xyl.mmall.order.enums;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 快递公司
 * 
 * @author dingmingliang
 * 
 */
@Deprecated
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExpressCompany implements AbstractEnumInterface<ExpressCompany> {
	
	/**
	 * 普通快递
	 */
	NORMAL_1(1, "普通快递", "HZEMS", new BigDecimal("100")),	
	/**
	 * 普通快递
	 */
	NORMAL_2(2, "普通快递", "EJJ", new BigDecimal("100")),
	/**
	 * 普通快递
	 */
	NORMAL_3(3, "普通快递", "SF", new BigDecimal("100")),
	/**
	 * 未知
	 */
	UNKNOWN(100, "", "UNKnown", new BigDecimal("101"));		

	/**
	 * 默认的快递方式(用户没有选择||用户无法选择情况下的默认值)
	 */
	public static ExpressCompany DEFAULT_EXPRESS = NORMAL_1;

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 快递公司英文短名
	 */
	private final String shortName;

	/**
	 * 快递价格
	 */
	private final BigDecimal price;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param sn
	 * @param price
	 */
	private ExpressCompany(int v, String d, String sn, BigDecimal price) {
		value = v;
		desc = d;
		shortName = sn;
		this.price = price;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public ExpressCompany genEnumByIntValue(int intValue) {
		for (ExpressCompany item : ExpressCompany.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.meta.base.AbstractEnumInterface#genEnumByShortName(int)
	 */
	public static ExpressCompany genEnumByShortName(String shortName) {
		for (ExpressCompany item : ExpressCompany.values()) {
			if (item.shortName.equalsIgnoreCase(shortName))
				return item;
		}
		return null;
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

	public String getShortName() {
		return shortName;
	}

	@Deprecated
	public BigDecimal getPrice() {
		return price;
	}
}
