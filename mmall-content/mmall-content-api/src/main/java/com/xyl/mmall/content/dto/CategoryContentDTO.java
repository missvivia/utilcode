package com.xyl.mmall.content.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.content.meta.CategoryContent;

public class CategoryContentDTO extends CategoryContent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5087362571989935342L;


	/**
	 * 子类
	 */
	private List<CategoryContentDTO>subCategoryContentDTOs;
	
	
	/**
	 * 是否可见
	 */
	private boolean isvisible;
	
	/**
	 * 是否验证区域
	 */
	private boolean isCheckArea;
	
	
	public CategoryContentDTO() {
	}
	
	public CategoryContentDTO(CategoryContent obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public List<CategoryContentDTO> getSubCategoryContentDTOs() {
		return subCategoryContentDTOs;
	}

	public void setSubCategoryContentDTOs(
			List<CategoryContentDTO> subCategoryContentDTOs) {
		this.subCategoryContentDTOs = subCategoryContentDTOs;
	}

	public boolean isIsvisible() {
		return isvisible;
	}

	public void setIsvisible(boolean isvisible) {
		this.isvisible = isvisible;
	}

	public boolean isCheckArea() {
		return isCheckArea;
	}

	public void setCheckArea(boolean isCheckArea) {
		this.isCheckArea = isCheckArea;
	}


	
	
	

}
