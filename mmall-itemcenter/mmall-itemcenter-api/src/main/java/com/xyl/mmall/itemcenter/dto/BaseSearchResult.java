package com.xyl.mmall.itemcenter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索结果基类，包含结果总数，是否有下一页，以及结果列表
 * 
 * @author hzhuangluqian
 *
 * @param <T>
 */
public class BaseSearchResult<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5051786269045951374L;

	/** 总记录数 */
	private int total;

	/** 是否有下一页 */
	private boolean hasNext;

	/** 结果集 */
	private List<T> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
