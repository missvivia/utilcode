/*
 * @(#)JudgementEngine.java 2014-4-10
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.judgement.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.enums.Operator;
import com.xyl.mmall.promotion.service.judgement.Judgement;

/**
 * JudgementEngine.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-4-10
 * @since 1.0
 */
@Component("judgementFacade")
public class JudgementFacade {

	@Autowired
	private Judgement judgement;

	public boolean judge(Condition condition, Object base) {
		int opType = condition.getOp();
		String desc = condition.getValue();
		if (StringUtils.isBlank(desc)) {
			return true;
		}

		if (opType == Operator.EQUAL.getValue()) {
			// 处理区间
			if (desc.indexOf("-") > 0) {
				String[] arrays = desc.split("-");
				if (arrays.length == 2) {
					if (StringUtils.isBlank(arrays[0]) && StringUtils.isBlank(arrays[1])) {
						return true;
					} else if (StringUtils.isNotBlank(arrays[0]) && StringUtils.isBlank(arrays[1])) {
						return judgement.greaterEqualThan(base, arrays[0]);
					} else if (StringUtils.isBlank(arrays[0]) && StringUtils.isNotBlank(arrays[1])) {
						return judgement.lessThan(base, arrays[1]);
					} else {
						return judgement.between(base, arrays[0], arrays[1]);
					}
				} else {
					return judgement.greaterEqualThan(base, arrays[0]);
				}
			}
			return judgement.equal(base, desc);
		} else if (opType == Operator.GREATER_EQUAL_THAN.getValue()) {
			return judgement.greaterEqualThan(base, desc);
		} else if (opType == Operator.GREATER_THAN.getValue()) {
			return judgement.greaterThan(base, desc);
		} else if (opType == Operator.LESS_EQUAL_THAN.getValue()) {
			return judgement.lessEqualThan(base, desc);
		} else if (opType == Operator.LESS_THAN.getValue()) {
			return judgement.lessThan(base, desc);
		} else if (opType == Operator.UNEQUAL.getValue()) {
			return judgement.nonEqual(base, desc);
		} else if (opType == Operator.CONTAINS.getValue()) {
			return judgement.contains(base, desc);
		} else if (opType == Operator.IN.getValue()) {
			return judgement.indexOf(base, desc);
		}
		return false;
	}

}
