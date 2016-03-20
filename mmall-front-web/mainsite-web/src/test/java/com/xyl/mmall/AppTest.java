/*
 * @(#) 2014-10-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.MainsiteConfig;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * CalCenterFacadeTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-22
 * @since      1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainsiteConfig.class, FacadeConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("performance")
//@Profile("performance")
public class AppTest {
	
	@Resource
	private UserProfileService userProfileService;
	
	@Resource
	private UserCouponService userCouponService;
	
	static {
		System.setProperty("spring.profiles.active", "performance");
	}
	
	@Test
	public void testCoupon() {
		
		List<UserProfileDTO> dtos = userProfileService.searchUserByParams(new HashMap<Integer, String>(), Integer.MAX_VALUE, 0);
		if (CollectionUtils.isEmpty(dtos)) {
			return;
		}
		
		long current = System.currentTimeMillis();
		long end = current + 1000 * 60 * 60 * 24 * 30L;
		
		for (UserProfileDTO dto : dtos) {
			UserCoupon coupon = new UserCoupon();
			coupon.setCouponCode("youhui1zy");
			coupon.setState(ActivationConstants.STATE_CAN_USE);
			coupon.setUserId(dto.getUserId());
			coupon.setValidStartTime(current);
			coupon.setValidEndTime(end);
			
			userCouponService.addUserCoupon(coupon);
			System.out.println("bind to user:" + dto.getUserName() + "; userid:" + dto.getUserId());
		}
	}
	
}
