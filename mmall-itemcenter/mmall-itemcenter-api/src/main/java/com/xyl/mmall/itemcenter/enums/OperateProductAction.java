package com.xyl.mmall.itemcenter.enums;


public enum OperateProductAction {
	
	/**
	 * 上架
	 */
	SHELVE("shelve", "上架"),

	/**
	 * 下架
	 */
	UNSHELVE("unshelve", "下架"),
	
	
	AUDIT("audit","提交审核");
	
	/**
	 * 值
	 */
	private final String name;

	/**
	 * 描述
	 */
	private final String desc;
	

	private OperateProductAction(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}


	public String getName() {
		return name;
	}


	public String getDesc() {
		return desc;
	}

	
	

}
