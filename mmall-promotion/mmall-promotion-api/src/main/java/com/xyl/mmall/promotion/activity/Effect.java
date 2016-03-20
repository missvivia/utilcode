/*
 * @(#)ActivationAct.java 2014-06-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * ActivationAct.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-06-10
 * @since      1.0
 */
public class Effect {
	
	private int type;
	
	private int op = 1;
	
	private String value;
	
	private String name;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Effect> sortByKey(List<Effect> actions) {
		if (CollectionUtils.isEmpty(actions)) {
			return null;
		}
		
		List<Effect> base = new ArrayList<>();
		List<Effect> other = new ArrayList<>();
		
		for (Effect action : actions) {
			other.add(action);
		}
		
		base.addAll(other);
		
		return base;
	}
}
