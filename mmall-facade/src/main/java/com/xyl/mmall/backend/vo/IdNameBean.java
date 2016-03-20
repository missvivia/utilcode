package com.xyl.mmall.backend.vo;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class IdNameBean implements java.io.Serializable {
	private static final long serialVersionUID = -160800241253117117L;

	private String id;

	private String name;

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

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
