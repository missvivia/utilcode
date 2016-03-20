package com.xyl.mmall.task.enums;

import java.io.Serializable;


public enum PlatformType implements Serializable{
	ANDROID("android"),IOS("ios"),WEB("web"),PC("pc"),ALL_PLATFORM("");
	
	PlatformType(String value){
		this.value=value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
