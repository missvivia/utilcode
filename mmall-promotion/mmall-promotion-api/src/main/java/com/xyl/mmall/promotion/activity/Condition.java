/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.activity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.promotion.enums.ConditionType;




/**
 * ConditionDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public class Condition implements Comparable<Condition>, Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1564651045233674355L;

    private int type;
	
	private int op = 1;
	
	private String value;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(Condition o) {
		if (o.getType() != this.getType()) {
			return 0;
		}
		
		if (this.getType() == ConditionType.BASIC_BRANDSCHEDULE_SATISFY.getIntValue()) {
			return 0;
		}
		
		if (StringUtils.isBlank(o.getValue()) || StringUtils.isBlank(this.getValue())) {
			return 0;
		}
		
		String[] o1Array = o.getValue().split("-");
		String[] thisArray = this.getValue().split("-");
		if (o1Array == null || o1Array.length == 0
				|| thisArray == null || thisArray.length == 0
				|| StringUtils.isBlank(o1Array[0]) || StringUtils.isBlank(thisArray[0])) {
			return 0;
		}
		
		int o1int = Integer.valueOf(o1Array[0]);
		int thisInt = Integer.valueOf(thisArray[0]);
		
		if (o1int > thisInt) {
			return -1;
		} else if (o1int < thisInt) {
			return 1;
		}
		return 0;
	}
}
