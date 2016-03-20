package com.xyl.mmall.framework.enums;

/**
 * 快递相关常量枚举
 * @author hzzhaozhenzuo
 *
 */
public enum ExpressConstant {
	
	SF("SF","顺风"),EMS("EMS","EMS");
	
	private String code;
	
	private String name;
	
	ExpressConstant(String code,String name){
		this.code=code;
		this.name=name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getNameByCode(String code){
		if(SF.getCode().equals(code)){
			return SF.getName();
		}else if(EMS.getCode().equals(code)){
			return EMS.getName();
		}
		return null;
	}
	

}
