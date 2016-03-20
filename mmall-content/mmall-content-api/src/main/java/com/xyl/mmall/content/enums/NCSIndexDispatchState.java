package com.xyl.mmall.content.enums;

import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public enum NCSIndexDispatchState implements AbstractEnumInterface<NCSIndexDispatchState> {
	NULL(0, "null"), 
	
	WAITING_TO_DISPATCH(1, "等待索引"), 
	
	DISPATCHED(2, "索引完成"), 
	
	WAITING_TO_DELETE(3, "等待索引"), 
	
	DELETED(4, "索引完成");

	private int intValue;
	private String desc;
	
	private NCSIndexDispatchState(int intValue, String desc) {
		this.intValue = intValue;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return intValue;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public NCSIndexDispatchState genEnumByIntValue(int intValue) {
		for(NCSIndexDispatchState s : NCSIndexDispatchState.values()) {
			if(intValue == s.intValue) {
				return s;
			}
		}
		return NCSIndexDispatchState.NULL;
	}
	
}
