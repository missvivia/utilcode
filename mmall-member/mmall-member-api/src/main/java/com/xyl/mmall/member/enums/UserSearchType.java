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
public enum UserSearchType implements AbstractEnumInterface<UserSearchType> {

	/**
	 * 
	 */
	NULL(-1, "NULL"),
	/**
	 * 用户名
	 */
	USER_NAME(1, "userName"),
	/**
	 * 昵称
	 */
	NICK_NAME(2, "nickName"),
	/**
	 * 用户ID
	 */
	USER_ID(3, "userId"),
	/**
	 * 用户手机号码
	 */
	MOBILE(4, "mobile"),
	/**
	 * 用户邮箱
	 */
	EMAIL(5, "email"),
	/**
	 * 收货人手机号码
	 */
	CONSIGNEE_MOBILE(6, "consigneeMobile");

	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private static Map<Integer, UserSearchType> map = new HashMap<>();

	static {
		for (UserSearchType item : UserSearchType.values()) {
			map.put(item.getIntValue(), item);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 */
	private UserSearchType(int value, String desc) {
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
	public UserSearchType genEnumByIntValue(int intValue) {
		for (UserSearchType item : UserSearchType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

	/**
	 * find UserSearchType by int value. Will return null if int value is
	 * invalid.
	 * 
	 * @param value
	 *            int value
	 * @return UserSearchType
	 */
	public static UserSearchType getUserSearchTypeByIntValue(int value) {
		if (map.containsKey(value)) {
			return map.get(value);
		}
		return NULL;
	}
}
