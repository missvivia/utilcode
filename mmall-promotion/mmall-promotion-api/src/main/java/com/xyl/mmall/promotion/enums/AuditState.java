/**
 * 
 */
package com.xyl.mmall.promotion.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * @author jmy
 *
 */
public enum AuditState implements AbstractEnumInterface<AuditState> {
	INIT(0, "新建"),
	SUBMITTED(1, "审核中"),
	PASSED(2, "审核通过"),
	REJECTED(3, "审核拒绝"),
//	DELETED(4, "已删除")
	;
	
	private final int type;
	private final String desc;
	
	private AuditState(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIntValue() {
		return this.type;
	}

	@Override
	public AuditState genEnumByIntValue(int intValue) {
		switch (intValue) {
			case 0:
				return INIT;
			case 1:
				return SUBMITTED;
			case 2:
				return PASSED;
			case 3:
				return REJECTED;
			default :
				return null;
		}
	}

}
