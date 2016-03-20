/*
 * @(#) 2014-10-17
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.PromotionConfig;
import com.xyl.mmall.promotion.service.RebateService;

/**
 * RebateServiceTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-17
 * @since      1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PromotionConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class RebateServiceTest {
	
	@Resource
	private RebateService rebateService;
	
	static {
		System.setProperty("spring.profiles.active", "dev");
	}
	
	@Test
	public void testRebate() {
		rebateService.rebate(475, 12);
	}
}
