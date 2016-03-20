package com.xyl.mmall.backend.vo;

import java.util.List;

public class SizeTemplateArchitect {
	/** 尺码模板id */
	private String id;

	/** 尺码模板名称 */
	private String name;

	/** 尺码模板下的尺码列表 */
	private List<SizeOption> list;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SizeOption> getList() {
		return list;
	}

	public void setList(List<SizeOption> list) {
		this.list = list;
	}
}
