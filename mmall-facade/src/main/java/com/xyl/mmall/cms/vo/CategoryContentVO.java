package com.xyl.mmall.cms.vo;

import java.util.List;

import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.content.dto.CategoryContentDTO;


public class CategoryContentVO {
	
	private String name;
	
	private long id;
	
	private long parentId;
	
	private int level;
	
	private int showIndex;
	
	private List<CategoryNormalVO> categoryNormalVOs;
	
	/** 子分类. */
	private List<CategoryContentVO> subCategoryContentList;
	
	/** 子分类DTO. */
	private List<CategoryContentDTO> subCategoryContentDTOList;
	
	/**
	 * 站点
	 */
	private List<SendDistrictDTO> sendDistrictDTOs;
	
	public CategoryContentVO() {
	}
	
	public CategoryContentVO(CategoryContentDTO obj) {
		this.id = obj.getId();
		this.name = obj.getName();
		this.parentId = obj.getSuperCategoryId();
		this.level = obj.getLevel();
		this.showIndex = obj.getShowIndex();
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CategoryContentVO> getSubCategoryContentList() {
		return subCategoryContentList;
	}
	
	public void setSubCategoryContentList(
			List<CategoryContentVO> subCategoryContentList) {
		this.subCategoryContentList = subCategoryContentList;
	}
	
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public List<CategoryNormalVO> getCategoryNormalVOs() {
		return categoryNormalVOs;
	}

	public void setCategoryNormalVOs(List<CategoryNormalVO> categoryNormalVOs) {
		this.categoryNormalVOs = categoryNormalVOs;
	}
	
	public List<SendDistrictDTO> getSendDistrictDTOs() {
		return sendDistrictDTOs;
	}

	public void setSendDistrictDTOs(List<SendDistrictDTO> sendDistrictDTOs) {
		this.sendDistrictDTOs = sendDistrictDTOs;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<CategoryContentDTO> getSubCategoryContentDTOList() {
		return subCategoryContentDTOList;
	}

	public void setSubCategoryContentDTOList(
			List<CategoryContentDTO> subCategoryContentDTOList) {
		this.subCategoryContentDTOList = subCategoryContentDTOList;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

}
