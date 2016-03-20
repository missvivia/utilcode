package com.xyl.mmall.backend.vo;

import java.util.List;

import com.xyl.mmall.itemcenter.dto.CategoryArchitect;

public class SizeTemplateEditVO {
	private List<CategoryArchitect> categoryList;

	private List<String> categories;

	private String templateName;

	private String remindText;

	private SizeTmplTableVO sizeTable;

	public SizeTemplateEditVO(){
		templateName="";
		remindText="";
	}
	
	public List<CategoryArchitect> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryArchitect> categoryList) {
		this.categoryList = categoryList;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getRemindText() {
		return remindText;
	}

	public void setRemindText(String remindText) {
		this.remindText = remindText;
	}

	public SizeTmplTableVO getSizeTable() {
		return sizeTable;
	}

	public void setSizeTable(SizeTmplTableVO sizeTable) {
		this.sizeTable = sizeTable;
	}
}
