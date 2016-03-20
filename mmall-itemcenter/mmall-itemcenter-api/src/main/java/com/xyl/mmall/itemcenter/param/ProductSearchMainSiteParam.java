/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ProductSearchMainSiteParam.java created by yydx811 at 2015年5月26日 下午3:12:15
 * 商品搜索vo
 *
 * @author yydx811
 */
public class ProductSearchMainSiteParam implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 7575229322517270771L;

	/** 单品ids. */
	private List<Long> spuIds;
	
	/** 商家ids. */
	private Set<Long> businessIds;

	/** 属性-选项map. */
	private Map<Long, Set<Long>> paramMap;

	/** 规格-选项map. */
	private Map<Long, Set<Long>> speciMap;
	
	/** 商品分类id. */
	private List<Long> categoryIds;
	
	/** 品牌id. */
	private Set<Long> brandIds;
	
	/** 搜索值. */
	private String searchValue;
	
	/** 排序字段. */
	private String sortColumn;
	
	/** 升降序. */
	private boolean isAsc = false;

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public Set<Long> getBusinessIds() {
		return businessIds;
	}

	public void setBusinessIds(Set<Long> businessIds) {
		this.businessIds = businessIds;
	}

	public Map<Long, Set<Long>> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<Long, Set<Long>> paramMap) {
		this.paramMap = paramMap;
	}

	public Map<Long, Set<Long>> getSpeciMap() {
		return speciMap;
	}

	public void setSpeciMap(Map<Long, Set<Long>> speciMap) {
		this.speciMap = speciMap;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Set<Long> getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(Set<Long> brandIds) {
		this.brandIds = brandIds;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public boolean getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
}
