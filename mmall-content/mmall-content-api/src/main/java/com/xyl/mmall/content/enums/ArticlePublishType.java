/**
 * 
 */
package com.xyl.mmall.content.enums;

import java.util.HashMap;
import java.util.Map;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum ArticlePublishType implements AbstractEnumInterface<ArticlePublishType> {

	/**
	 * 
	 */
	NULL(-1, "未知"),
	/**
	 * 不发布
	 */
	NONE(0, ""),
	/**
	 * 发布web
	 */
	WEB(1, "Web"),
	/**
	 * 发布App
	 */
	APP(2, "App"),
	/**
	 * 发布web和App
	 */
	BOTH(3, "Web,App");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private static Map<Integer, ArticlePublishType> map = new HashMap<>();

	static {
		for (ArticlePublishType item : ArticlePublishType.values()) {
			map.put(item.getIntValue(), item);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private ArticlePublishType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		return value;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public ArticlePublishType genEnumByIntValue(int intValue) {
		for (ArticlePublishType item : ArticlePublishType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	public static ArticlePublishType getArticlePublishTypeByIntValue(int value) {
		if (map.containsKey(value)) {
			return map.get(value);
		}
		return NULL;
	}
}
