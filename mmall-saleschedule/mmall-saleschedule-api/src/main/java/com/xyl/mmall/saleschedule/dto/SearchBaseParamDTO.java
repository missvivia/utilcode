package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

public class SearchBaseParamDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer limit;
	
	private Integer offset;
	
	private String orderColumn;
	
	private boolean isAsc;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

}
