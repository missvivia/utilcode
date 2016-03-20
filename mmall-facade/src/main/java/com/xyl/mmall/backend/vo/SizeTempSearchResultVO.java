package com.xyl.mmall.backend.vo;

public class SizeTempSearchResultVO {
	private long id;
	private String templateName;
	private String lowCategoryName;
	private String modifyTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getLowCategoryName() {
		return lowCategoryName;
	}
	public void setLowCategoryName(String lowCategoryName) {
		this.lowCategoryName = lowCategoryName;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
