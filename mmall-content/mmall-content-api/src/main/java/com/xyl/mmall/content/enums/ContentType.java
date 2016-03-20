package com.xyl.mmall.content.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public enum ContentType implements AbstractEnumInterface<ContentType> {
	NULL(0, "null"), 
	
	ARTICLE(1, "内容管理的文章信息"), 
	
	ARTICLE_PUBLISHED(2, "内容管理的已发布的文章信息");

	private int intValue;
	private String desc;
	
	private ContentType(int intValue, String desc) {
		this.intValue = intValue;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return intValue;
	}

	@Override
	public ContentType genEnumByIntValue(int intValue) {
		for(ContentType ct : ContentType.values()) {
			if(intValue == ct.intValue) {
				return ct;
			}
		}
		return ContentType.NULL;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
