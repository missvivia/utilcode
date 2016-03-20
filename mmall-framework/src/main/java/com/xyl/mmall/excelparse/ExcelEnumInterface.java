package com.xyl.mmall.excelparse;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

public interface ExcelEnumInterface<T> extends AbstractEnumInterface<T> {
	public T getEnumByDesc(String descValue);

	public String getDesc();
	
	public String toString();
}
