package com.xyl.mmall.mobile.ios.facade.pageView.common;

import java.util.List;

import com.xyl.mmall.content.dto.CategoryContentDTO;

public class MobileCategoryVO {

	private long id;
	private String name;
	private int showIndex;
	private int level;
	private List<MobileCategoryVO>subCategoryContentDTOs;
	
	public MobileCategoryVO() {
	}
	
	public MobileCategoryVO(CategoryContentDTO categoryContentDTO) {
		this.id = categoryContentDTO.getId();
		this.name = categoryContentDTO.getName();
		this.showIndex = categoryContentDTO.getShowIndex();
		this.level = categoryContentDTO.getLevel();
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
	public int getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<MobileCategoryVO> getSubCategoryContentDTOs() {
		return subCategoryContentDTOs;
	}
	public void setSubCategoryContentDTOs(List<MobileCategoryVO> subCategoryContentDTOs) {
		this.subCategoryContentDTOs = subCategoryContentDTOs;
	}
	
	
}
