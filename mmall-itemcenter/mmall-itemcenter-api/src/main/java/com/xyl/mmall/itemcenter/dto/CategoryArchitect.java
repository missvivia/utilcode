package com.xyl.mmall.itemcenter.dto;

import java.util.List;

/**
 * 类目结构类
 * 
 * @author hzhuangluqian
 *
 */
public class CategoryArchitect {
	/** 类目id */
	private String id;

	/** 类目名 */
	private String name;

	/** 下一级类目列表 */
	private List<CategoryArchitect> list;

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

	public List<CategoryArchitect> getList() {
		return list;
	}

	public void setList(List<CategoryArchitect> list) {
		this.list = list;
	}

}
