package com.xyl.mmall.framework.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzzengchengyuan
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExpressCompany implements AbstractEnumInterface<ExpressCompany> {
	NULL(0, "NULL", "NULL"),
	
	EMS(1, "EMS", "邮政速递"),
	
	SF(2, "SF", "顺丰速运"), 
	
	YUNDA(3, "YUNDA", "韵达快递"), 
	
	ZTO(4, "ZTO", "中通快递"), 
	
	BEST(5, "BEST", "百世汇通快递"), 
	
	YTO(6, "YTO", "圆通快递"), 
	
	STO(7, "STO", "申通快递"), 
	
	TTK(8, "TTK", "天天快递"), 
	
	QF(9, "QF", "全峰快递"), 
	
	ZJS(10, "ZJS", "宅急送"), 
	
	OTHER(11, "OTHER", "其他快递公司");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 快递公司code
	 */
	private final String code;

	/**
	 * 快递公司名称
	 */
	private final String name;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private ExpressCompany(int v, String c, String n) {
		value = v;
		code = c;
		name = n;
	}

	public int getValue() {
		return value;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static ExpressCompany[] validValues() {
		ExpressCompany[] validValuseArray = new ExpressCompany[] {
				ExpressCompany.EMS, ExpressCompany.SF, ExpressCompany.YUNDA, ExpressCompany.ZTO, 
				ExpressCompany.BEST, ExpressCompany.YTO, ExpressCompany.STO, ExpressCompany.TTK, 
				ExpressCompany.QF, ExpressCompany.ZJS, ExpressCompany.OTHER
		};
		return validValuseArray;
	}
	
	@Override
	public int getIntValue() {
		return this.value;
	}

	public static ExpressCompany genEnumNameIgnoreCase(String name) {
		for (ExpressCompany type : values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}

	@Override
	public ExpressCompany genEnumByIntValue(int intValue) {
		for (ExpressCompany type : values()) {
			if (type.getValue() == intValue) {
				return type;
			}
		}
		return null;
	}
}