package com.xyl.mmall.content.enums;

import com.netease.ndir.common.schema.FieldType;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public enum ArticlePublishedNCSIndexField {

	ID("id", "主键", FieldType.LONG), 
	
	CATEGORY_ID("categoryId", "文章类别ID", FieldType.LONG), 
	
	PARENT_CATEGORY_ID("parentCategoryId", "文章父类别ID", FieldType.LONG), 
	
	KEYWORDS("keywords", "文章关键字", FieldType.STRING), 
	
	TITLE("title", "文章标题", FieldType.STRING), 
	
	CONTENT("content", "文章内容", FieldType.STRING), 
	
	STATUS("status", "文章状态(0:未发布,1:已发布,2:已删除)", FieldType.INT), 
	
	PUBLISH_TYPE("publishType", "文章发布类型(0:未发布,1:Web,2:App,3:Web和App)", FieldType.INT), 
	
	ORG_ARTICLE_ID("orgArticleId", "原文章ID", FieldType.LONG);
	
	
	private String fieldName;
	private String desc;
	private FieldType fieldType;
	
	private ArticlePublishedNCSIndexField(String fieldName, String desc, FieldType fieldType) {
		this.fieldName = fieldName;
		this.desc = desc;
		this.fieldType = fieldType;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public FieldType getFieldType() {
		return fieldType;
	}
	
}
