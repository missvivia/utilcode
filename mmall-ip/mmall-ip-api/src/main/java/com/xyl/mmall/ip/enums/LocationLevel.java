package com.xyl.mmall.ip.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public enum LocationLevel implements AbstractEnumInterface<LocationLevel> {

	NULL(0, "NULL"),
	
	LEVEL_PROVICE(1, "省份或者直辖市"),
	
	LEVEL_CITY(2, "普通市"),
	
	LEVEL_DISTRICT(3, "区或者县"),
	
	LEVEL_STREET(4, "街道"),
	;
	/**
	 * 值
	 */
	private final int value;
	
	/**
	 * 描述
	 */
	private final String desc;
	
	private LocationLevel(int v, String d) {
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
	public LocationLevel genEnumByIntValue(int intValue) {
		for (LocationLevel item : LocationLevel.values()) {
			if (item.value == intValue)
	    		return item;
		}
		return NULL;
	}
}
