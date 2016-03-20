package com.xyl.mmall.backend.vo;

import java.util.List;

public class ItemReviewPassVO {
	/**
	 * 档期id
	 */
	private List<Long> poList;

	/** 通过审核的id列表 */
	private List<Long> list;

	public List<Long> getPoList() {
		return poList;
	}

	public void setPoList(List<Long> poList) {
		this.poList = poList;
	}

	public List<Long> getList() {
		return list;
	}

	public void setList(List<Long> list) {
		this.list = list;
	}
}
