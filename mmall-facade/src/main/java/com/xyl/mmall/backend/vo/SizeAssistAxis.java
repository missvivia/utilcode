package com.xyl.mmall.backend.vo;

import java.util.List;

/**
 * 尺码助手表轴vo
 * 
 * @author hzhuangluqian
 *
 */
public class SizeAssistAxis {
	/** 表轴名 */
	private String name;

	/** 表轴值 */
	private List<?> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
