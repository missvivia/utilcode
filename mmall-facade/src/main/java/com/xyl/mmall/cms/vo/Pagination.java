package com.xyl.mmall.cms.vo;

/**
 * 
 * @author hzchaizhf
 * 
 */
public class Pagination {

	private int currentPage = 1;

	private int pageSize = 10;

	private int totalRecord;

	private int beginIndex = 0;

	private int totalPage;

	private boolean hasPrePage;

	private boolean hasNextPage;

	public Pagination(int currentPage, int pageSize) {
		setCurrentPage(currentPage);
		this.pageSize = pageSize;
		calculate();
	}

	public Pagination() {
	}

	private void calculate() {
		if (totalRecord <= 0) {
			totalPage = 1;
		} else {
			totalPage = (totalRecord + pageSize - 1) / pageSize;
		}
		this.beginIndex = (currentPage - 1) * pageSize;
		this.hasPrePage = this.currentPage > 1;
		this.hasNextPage = this.totalPage > this.currentPage;
	}

	public boolean isHasPrePage() {
		return hasPrePage;
	}

	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calculate();
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		calculate();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage < 1 ? 1 : currentPage;
		calculate();
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

}
