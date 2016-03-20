package com.xyl.mmall.order.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;

/**
 * 退款方式
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 上午11:33:40
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = RefundTypeJsonDeserializer.class)
public enum RefundType implements AbstractEnumInterface<RefundType> {

	ORIGINAL_PATH(0, "原路返回"),
	
	WANGYIBAO(1, "网易宝"), 
	
	BANKCARD(2, "银行卡");

	/** 值 */
	private final int value;

	/** 描述 */
	private final String desc;
	
	private RefundType(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public int getIntValue() {
		return value;
	}

	@Override
	public RefundType genEnumByIntValue(int intValue) {
		for(RefundType rt : RefundType.values()) {
			if(intValue == rt.getIntValue()) {
				return rt;
			}
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}
}
