package com.xyl.mmall.backend.vo;

public class CategoryVO {
	/** 类目id */
	private String id;

	/** 类目名 */
	private String name;

	/** 类目下商品数量 */
	private int count;

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
