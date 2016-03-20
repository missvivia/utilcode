package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退货时定时器向JIT发送消息的状态
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:08:11
 * 
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JITPushState implements AbstractEnumInterface<JITPushState> {

	INIT(0, "初始化"), 
	
	WAITING_PUSH(1, "等待推送"), 
	
	PUSH_SUCCESSFUL(2, "推送成功");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;

	/**
	 * 构造函数
	 * 
	 * @param v
	 * @param d
	 * @param name
	 */
	private JITPushState(int v, String d) {
		value = v;
		desc = d;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.daojar.meta.base.AbstractEnumInterface#genEnumByIntValue(int)
	 */
	public JITPushState genEnumByIntValue(int intValue) {
		for (JITPushState item : JITPushState.values()) {
			if (item.value == intValue)
				return item;
		}
		return null;
	}

	public int getIntValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
