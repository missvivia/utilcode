package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.Category;

public class CategoryVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -7885756907193298146L;

	/** 主键id. */
	private long id;

	/** 父目录的id. */
	private long superCategoryId;

	/** 显示次序. */
	private int showIndex;

	/** 目录等级. */
	private int level;

	/** 目录名称. */
	private String name;
	
	/** 子分类. */
	private List<CategoryVO> subList;
	
	public CategoryVO(Category obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSuperCategoryId() {
		return superCategoryId;
	}

	public void setSuperCategoryId(long superCategoryId) {
		this.superCategoryId = superCategoryId;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

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

	public List<CategoryVO> getSubList() {
		return subList;
	}

	public void setSubList(List<CategoryVO> subList) {
		this.subList = subList;
	}
	
}
