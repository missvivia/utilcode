package com.xyl.mmall.bi.core.enums;

/**
 * app/web端.
 * 
 * @author wangfeng
 * 
 */
public enum ClientType {

	NULL("null", "未知类型"),
	
	APP("app", "app"),
	
	WEB("web", "web"),
	
	WAP("wap", "wap"),
	
	ORDER("order", "销售数据"),
	
	CMS("cms", "cms平台"),
	
	API_CONSUMER("api_consumer","对外api的调用者");

	/**
	 * 值
	 */
	private final String value;

	/**
	 * 描述
	 */
	private final String desc;

	private ClientType(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public ClientType genEnumByValue(String strValue) {
		for (ClientType item : ClientType.values()) {
			if (item.value.equalsIgnoreCase(strValue))
				return item;
		}
		return NULL;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
