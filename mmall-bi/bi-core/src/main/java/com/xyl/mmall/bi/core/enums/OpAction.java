package com.xyl.mmall.bi.core.enums;

/**
 * 用户操作类型.
 * 
 * @author wangfeng
 * 
 */
public enum OpAction {

	UNKNOWN("unknown", "未知类型"),
	
	PAGE("page", "页面访问"),
	
	CLICK("click", "点击操作"),
	
	API_ACC("api_acc","api调用"),
	
	ORDER("order", "销售数据"),
	
	CMS("cms", "cms平台");

	/**
	 * 值
	 */
	private final String value;

	/**
	 * 描述
	 */
	private final String desc;

	private OpAction(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public OpAction genEnumByValue(String strValue) {
		for (OpAction item : OpAction.values()) {
			if (item.value.equalsIgnoreCase(strValue))
				return item;
		}
		return UNKNOWN;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
