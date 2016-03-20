package com.xyl.mmall.cms.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.itemcenter.dto.CategoryNormalDTO;

/**
 * CategoryNormalVO.java created by yydx811 at 2015年4月27日 上午9:22:13
 * 商品分类vo
 *
 * @author yydx811
 */
public class CategoryNormalVO implements Serializable{

	/** 序列化id. */
	private static final long serialVersionUID = 7361312171260579278L;

	/** 主键id */
	private long categoryId;

	/** 目录等级 */
	private int categoryDepth;

	/** 目录名称 */
	private String categoryName;

	/** 显示次序 */
	private int categoryIndex;

	/** 父目录的id */
	private long parentId;
	
	/** 操作人. */
	private String operator;
	
	/** 子分类. */
	private List<CategoryNormalVO> subCategoryList;
	
	public CategoryNormalVO() {
	}
	
	public CategoryNormalVO(CategoryNormalDTO obj) {
		this.categoryId = obj.getId();
		this.categoryDepth = obj.getLevel();
		this.categoryName = obj.getName();
		this.categoryIndex = obj.getShowIndex();
		this.parentId = obj.getSuperCategoryId();
		if (!CollectionUtils.isEmpty(obj.getSameParentList())) {
			this.subCategoryList = new ArrayList<CategoryNormalVO>(obj.getSameParentList().size());
			for (CategoryNormalDTO categoryNormalDTO : obj.getSameParentList()) {
				this.subCategoryList.add(new CategoryNormalVO(categoryNormalDTO));
			}
		}
	}

	public CategoryNormalDTO convertToDTO() {
		CategoryNormalDTO ret = new CategoryNormalDTO();
		ret.setId(categoryId);
		ret.setLevel(categoryDepth);
		ret.setName(categoryName);
		ret.setShowIndex(categoryIndex);
		ret.setSuperCategoryId(parentId);
		return ret;
	}
	
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryDepth() {
		return categoryDepth;
	}

	public void setCategoryDepth(int categoryDepth) {
		this.categoryDepth = categoryDepth;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<CategoryNormalVO> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<CategoryNormalVO> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "[categoryId=" + categoryId + ", categoryDepth=" + categoryDepth + ", categoryName="
				+ categoryName + ", categoryIndex=" + categoryIndex + ", parentId=" + parentId + ", operator="
				+ operator + ", subCategoryList=" + subCategoryList + "]";
	}
}
