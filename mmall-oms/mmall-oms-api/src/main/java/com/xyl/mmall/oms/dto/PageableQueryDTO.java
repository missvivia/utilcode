/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.io.Serializable;

/**
 * 支持分页查询接口的参数封装
 * 
 * @author hzzengchengyuan
 *
 */
public class PageableQueryDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final long DEFAULT_LIMIT = 10;
	
	public static final long PAGE_UNABLE = -1;
	/**
	 * 分页-每页数据大小
	 */
	private long limit = PAGE_UNABLE;

	/**
	 * 分页-从第几行开始查询
	 */
	private long offset;

	/**
	 * @return the limit
	 */
	public long getLimit() {
		return limit;
	}
	
	/**
	 * 取消分页查询
	 */
	public void unPageable(){
		this.limit = PAGE_UNABLE;
		this.offset = 0;
	}

	/**
	 * 设置分页大小，如果设置值<=0 则设置为默认值{@value #DEFAULT_LIMIT}
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(long limit) {
		this.limit = limit <=0 ? DEFAULT_LIMIT : limit;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * 设置当前页码，页面数从0开始，如果设置值小于0则设置为0
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(long offset) {
		this.offset = offset < 0 ? 0 : offset;
	}
	
	/**
	 * 判断是否开启了分页查询，依据：limit > 0
	 * @return
	 */
	public boolean isPageable(){
		return limit > 0;
	}

}
