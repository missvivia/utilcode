package com.xyl.mmall.cms.vo;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class PageVO implements java.io.Serializable {

	private static final long serialVersionUID = 4301236122002695213L;

	private int offset;

	private int limit;

	private boolean total;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "PageVO [offset=" + offset + ", limit=" + limit + ", total=" + total + "]";
	}

}
