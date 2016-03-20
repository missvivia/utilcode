package com.xyl.mmall.content.dto;

import com.netease.print.daojar.meta.base.DDBParam;

/**
 * 内容分类搜索条件
 * @author lihongpeng
 *
 */
public class SearchCategoryContentDTO extends DDBParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2410358196467303740L;
	
	/**
	 * 分类等级
	 */
	private int level;
	
	/**
	 * 分类名称
	 */
	private String name;
	
	/**
	 * 类目树Id
	 */
	private long rootId = 0l;
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRootId() {
		return rootId;
	}

	public void setRootId(long rootId) {
		this.rootId = rootId;
	}
	
	
	

}
