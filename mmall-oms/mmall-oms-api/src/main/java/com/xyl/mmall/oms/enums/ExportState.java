/*
 * 2014-9-23
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.oms.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * ExportState.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-23
 * @since 1.0
 */
public enum ExportState implements AbstractEnumInterface<ExportState> {
	NULL(-1, "NULL"), NO_JIT(0, "未导出"), IS_JIT(1, "已导出");

	private final int value;

	private final String desc;

	private ExportState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#getIntValue()
	 */
	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	@Override
	public ExportState genEnumByIntValue(int intValue) {
		for (ExportState item : ExportState.values()) {
			if (item.value == intValue)
				return item;
		}
		return NULL;
	}

}
