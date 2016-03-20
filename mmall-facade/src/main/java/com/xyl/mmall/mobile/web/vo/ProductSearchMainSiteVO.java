/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * ProductSearchMainSiteVO.java created by yydx811 at 2015年5月26日 下午3:12:15
 * 商品搜索vo
 *
 * @author yydx811
 */
public class ProductSearchMainSiteVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 7575229322517270771L;

	/** 内容分类id. */
	private long categoryContentId;

	/** 商品分类id. */
	private long categoryNormalId;

	/** 品牌id. */
	private String brandIds;

	/** 属性-选项map. */
	private Map<Long, Set<Long>> paramMap;

	/** 规格-选项map. */
	private Map<Long, Set<Long>> speciMap;

	/** 搜索值. */
	private String searchValue;
	
	/** 区域id. */
	private long areaCode;
	
	public long getCategoryContentId() {
		return categoryContentId;
	}

	public void setCategoryContentId(long categoryContentId) {
		this.categoryContentId = categoryContentId;
	}

	public long getCategoryNormalId() {
		return categoryNormalId;
	}

	public void setCategoryNormalId(long categoryNormalId) {
		this.categoryNormalId = categoryNormalId;
	}

	public String getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
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

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(long areaCode) {
		this.areaCode = areaCode;
	}
}
