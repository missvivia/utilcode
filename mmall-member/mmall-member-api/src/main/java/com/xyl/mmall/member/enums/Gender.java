/**
 * 
 */
package com.xyl.mmall.member.enums;

import java.util.HashMap;
import java.util.Map;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author lihui
 *
 */
public enum Gender implements AbstractEnumInterface<Gender> {

	/**
	 * 
	 */
	NULL(-1, "未知"),
	/**
	 * 男
	 */
	MALE(0, "男"),
	/**
	 * 女
	 */
	FEMALE(1, "女");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private static Map<Integer, Gender> map = new HashMap<>();

	static {
		for (Gender item : Gender.values()) {
			map.put(item.getIntValue(), item);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private Gender(int value, String desc) {
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
	public Gender genEnumByIntValue(int intValue) {
		for (Gender item : Gender.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	public static Gender getGenderByIntValue(int value) {
		if (map.containsKey(value)) {
			return map.get(value);
		}
		return NULL;
	}
}
