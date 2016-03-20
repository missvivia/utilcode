package com.xyl.mmall.member.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author lihui
 */
public enum PermissionType implements AbstractEnumInterface<PermissionType> {

	/**
	 * 
	 */
	NULL(0, "NULL"),
	/**
	 * inbox
	 */
	INBOX(1, "inbox"),
	/**
	 * record
	 */
	RECORD(2, "record"),
	/**
	 * briefcase
	 */
	BRIEFCASE(3, "briefcase"),
	/**
	 * barcode
	 */
	BARCODE(4, "barcode"),
	/**
	 * folder-close
	 */
	FOLDER_CLOSE(5, "folder-close"),
	/**
	 * picture
	 */
	PICTURE(6, "picture"),
	/**
	 * wrench
	 */
	WRENCH(7, "wrench"),
	/**
	 * user
	 */
	USER(8, "user"),

	/**
	 * home
	 */
	HOME(9, "home"),

	/**
	 * dashboard
	 */
	DASHBOARD(10, "dashboard"),

	/**
	 * heart
	 */
	HEART(11, "heart"),

	/**
	 * gift
	 */
	GIFT(12, "gift"),

	/**
	 * bell
	 */
	BELL(13, "bell"),

	/**
	 * calendar
	 */
	CALENDAR(14, "calendar"),

	/**
	 * tags
	 */
	TAGS(15, "tags"),

	/**
	 * list-alt
	 */
	LIST_ALT(16, "list-alt"),

	/**
	 * credit-card
	 */
	CREDIT_CARD(17, "credit-card"),

	/**
	 * magnet
	 */
	MAGNET(18, "magnet"),

	/**
	 * asterisk
	 */
	ASTERISK(19, "asterisk"),

	/**
	 * saved
	 */
	SAVED(20, "saved"),

	/**
	 * phone
	 */
	PHONE(21, "phone"),

	/**
	 * time
	 */
	TIME(22, "time"),
	
	/**
	 * category
	 */
	LIST(23, "list"),
	
	/**
	 * item
	 */
	ITEM(24, "gift"),

	/**
	 * item
	 */
	PRODUCT(25, "gift"),
	
	/**
	 * userInfo
	 */
	USERINFO(26, "user"),
	
	/**
	 * order
	 */
	ORDER(27, "list"),

	/**
	 * site
	 */
	SITE(28, "globe");
	
	/**
	 * 值
	 */
	private final int value;

	/**
	 * 描述
	 */
	private final String desc;

	private PermissionType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public PermissionType genEnumByIntValue(int intValue) {
		for (PermissionType item : PermissionType.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
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

	public String getDesc() {
		return desc;
	}

}
