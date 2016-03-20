/**
 * 
 */
package com.xyl.mmall.content.enums;

import java.util.HashMap;
import java.util.Map;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author hzlihui2014
 *
 */
public enum PreheatSlogan implements AbstractEnumInterface<PreheatSlogan> {

	/**
	 * 
	 */
	NULL(-1, ""),
	/**
	 * Slogan1
	 */
	SLOGAN_ONE(1, "幸福的小事就是每天都有新衣服"),
	/**
	 * Slogan2
	 */
	SLOGAN_TWO(2, "谁说新品不打折"),
	/**
	 * Slogan3
	 */
	SLOGAN_THREE(3, "不做尾单承包户"),
	/**
	 * Slogan4
	 */
	SLOGAN_FOUR(4, "时尚新宠美到冒泡"),
	/**
	 * Slogan5
	 */
	SLOGAN_FIVE(5, "专柜新款不用等"),
	/**
	 * Slogan6
	 */
	SLOGAN_SIX(6, "美到冒泡");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private static Map<String, PreheatSlogan> map = new HashMap<>();

	static {
		for (PreheatSlogan item : PreheatSlogan.values()) {
			map.put(item.getDesc(), item);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private PreheatSlogan(int value, String desc) {
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
	public PreheatSlogan genEnumByIntValue(int intValue) {
		for (PreheatSlogan item : PreheatSlogan.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	/**
	 * find PreheatSlogan by desc value. Will return null if desc value is
	 * invalid.
	 * 
	 * @param value
	 *            desc value
	 * @return PreheatSlogan
	 */
	public static PreheatSlogan getPreheatSloganByDesc(String value) {
		if (map.containsKey(value)) {
			return map.get(value);
		}
		return NULL;
	}

}
