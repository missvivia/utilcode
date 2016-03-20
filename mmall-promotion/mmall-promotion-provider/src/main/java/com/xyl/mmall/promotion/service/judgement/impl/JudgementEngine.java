/*
 * @(#)JudgementEngine.java 2014-4-10
 *
 * Copyright 2013 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.judgement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.promotion.activity.Condition;

/**
 * JudgementEngine.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-4-10
 * @since 1.0
 */
@Component("judgementEngine")
public class JudgementEngine {

	@Autowired
	private JudgementFacade judgementFacade;

	public boolean judge(Condition condition, Object base) {
		return judgementFacade.judge(condition, base);
	}

}
