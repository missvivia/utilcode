/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * PointHistoryType.java created by yydx811 at 2015年12月23日 上午10:33:34
 * 积分记录类型
 *
 * @author yydx811
 */
public enum PointHistoryType implements AbstractEnumInterface<PointHistoryType> {
	
	CMS(1, "管理员调整"), 
	
	ACHIEVE(2, "交易获取"), 
	
	EXCHANGE(3, "交易抵扣"),

	RETURN(2, "交易回退");

	private final int type;

	private final String desc;
	
	PointHistoryType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	@Override
	public int getIntValue() {
		return type;
	}

	@Override
	public PointHistoryType genEnumByIntValue(int intValue) {
		for (PointHistoryType historyType : PointHistoryType.values()) {
			if (historyType.getType() == intValue) {
				return historyType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
