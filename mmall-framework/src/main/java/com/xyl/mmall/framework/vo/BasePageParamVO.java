package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.List;

/**
 * BasePageParamVO.java created by yydx811 at 2015年4月27日 上午9:57:16
 * 分页对象
 *
 * @author yydx811
 * @param <T>
 */
public class BasePageParamVO<T> implements Serializable {
	
	/** 序列化id. */
	private static final long serialVersionUID = -2322057707981348481L;

	/** 每页记录数. */
	private int pageSize = 10;

	/** 当前页码. */
	private int currentPage = 1;

	/** 总记录数. */
	private int totalResults = 0;

	/** 总页数. */
	private int totalPages;
	
	/** 是否还有下一页。 */
	private boolean hasNextPage;
	
	/** 数据. */
	private List<T> list;
	

	/** 是否分页,1分页,其余不分页. */
	private short isPage = 1;
	
	/**
	 * 构造函数.
	 */
	public BasePageParamVO() {
	}
	
	/**
	 * 构造函数.
	 * @param totalResults 总记录数
	 */
	public BasePageParamVO(int totalResults) {
		this.totalResults = (totalResults > 0) ? totalResults : 0;
		this.totalPages = (int) Math.ceil(this.totalResults / (double) this.pageSize);
	}
	
	/**
	 * 构造函数.
	 * @param totalResults 总记录数
	 * @param pageSize 每页大小
	 */
	public BasePageParamVO(int totalResults, int pageSize) {
		this.totalResults = (totalResults > 0) ? totalResults : 0;
		if (pageSize > 0){
			this.pageSize = pageSize;
		}
		this.totalPages = (int) Math.ceil(this.totalResults / (double) this.pageSize);
	}

	
	/**
	 * 获取 每页记录数.
	 * @return the 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	
	/**
	 * 设置 每页记录数.
	 * @param pageSize the new 每页记录数
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
	/**
	 * 获取 当前页码.
	 * @return the 当前页码
	 */
	public int getCurrentPage() {
		if(currentPage > getTotalPages()){
			currentPage = getTotalPages();
		}
		return currentPage > 0 ? currentPage : 1;
	}

	
	/**
	 * 设置 当前页码.
	 * @param currentPage the new 当前页码
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage >= 1){
			this.currentPage = currentPage;
		}
	}

	
	/**
	 * 获取 总记录数.
	 * @return the 总记录数
	 */
	public int getTotal() {
		this.totalResults = (totalResults > 0) ? totalResults : 0;
		return totalResults;
	}

	public boolean isHasNextPage() {
		hasNextPage = currentPage == totalPages ? false : true;
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
	/**
	 * 设置 总记录数.
	 * @param totalResults the new 总记录数
	 */
	public void setTotal(int totalResults) {
		this.totalResults = totalResults;
	}

	
	/**
	 * 获取 总页数.
	 * @return the 总页数
	 */
	public int getTotalPages() {
		this.totalPages = (int) Math.ceil(this.totalResults / (double) this.pageSize);
		return totalPages = totalPages > 0 ? totalPages : 1;
	}
	
	/**
	 * 设置 总页数.
	 * @param totalPages the new 总页数
	 */
	public void setTotalPages(int totalPages) {		
		this.totalPages = totalPages;
	}

	/**
	 * 是否有下一页.
	 * @return true, if successful
	 */
	public boolean hasNextPage() {
		if (this.currentPage < this.getTotalPages()){
			return true;
		}
		return false;
	}

	/**
	 * 是否有上一页.
	 * 
	 * @return true, if successful
	 * @return
	 */
	public boolean hasPreviousPage() {
		if (this.currentPage > 1){
			return true;
		}
		return false;
	}

	/**
	 * 获取当前显示记录页.
	 * 
	 * @return the start rownum
	 * @return
	 */
	public int getStartRownum() {
		return (getCurrentPage() - 1) * getPageSize() + 1;
	}

	/**
	 * 获取 数据.
	 * 
	 * @return the 数据
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置 数据.
	 * @param data the new 数据
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	public short getIsPage() {
		return isPage;
	}

	public void setIsPage(short isPage) {
		this.isPage = isPage;
	}
	
	public <V> BasePageParamVO<V> copy(BasePageParamVO<V> pageParamVO) {
		pageParamVO.setCurrentPage(this.currentPage);
		pageParamVO.setIsPage(this.getIsPage());
		pageParamVO.setPageSize(this.getPageSize());
		pageParamVO.setTotal(this.getTotal());
		pageParamVO.setTotalPages(this.getTotalPages());
		pageParamVO.setHasNextPage(this.isHasNextPage());
		return pageParamVO;
	}
}
