package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.Category;

/**
 * 类目dto
 * 
 * @author hzhuangluqian
 *
 */
public class CategoryDTO extends Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7869733347237669642L;

	/** 所属同一父级类目下的素有类目列表 */
	private List<Category> sameParentList;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public CategoryDTO(Category obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public List<Category> getSameParentList() {
		return sameParentList;
	}

	public void setSameParentList(List<Category> sameParentList) {
		this.sameParentList = sameParentList;
	}
}
