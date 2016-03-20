/**
 * 
 */
package com.xyl.mmall.content.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum ArticleStatus implements AbstractEnumInterface<ArticleStatus> {

	/**
	 * 
	 */
	NULL(-1, "未知"),
	/**
	 * 未发布
	 */
	SAVED(0, "未发布"),
	/**
	 * 已发布
	 */
	PUBLISHED(1, "已发布"),
	/**
	 * 已删除
	 */
	DELETED(2, "已删除");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private ArticleStatus(int value, String desc) {
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
	public ArticleStatus genEnumByIntValue(int intValue) {
		for (ArticleStatus item : ArticleStatus.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}
}
