/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzengchengyuan
 *
 */
public class PageableList<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 分页-每页数据大小
	 */
	private long limit;

	/**
	 * 分页-当前查询页
	 */
	private long offset;

	private long total;

	private List<E> list = new ArrayList<E>();

	public PageableList() {

	}

	public PageableList(List<E> list) {
		this(list, 0, 0, list == null ? 0 : list.size());
	}

	public PageableList(List<E> list, long limit, long offset) {
		this(list, limit, offset, list == null ? 0 : list.size());
	}

	public PageableList(List<E> list, long limit, long offset, long total) {
		this.list = list;
		this.limit = limit;
		this.offset = offset;
		this.total = total;
	}

	/**
	 * @return the limit
	 */
	public long getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(long limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = offset;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}
	
	public int getSize(){
		return list == null ? 0 : list.size();
	}

	/**
	 * @return the list
	 */
	public List<E> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<E> list) {
		this.list = list;
	}

}
