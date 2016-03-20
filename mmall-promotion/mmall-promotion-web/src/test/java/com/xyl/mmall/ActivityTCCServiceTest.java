/*
 * @(#) 2014-10-17
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.PromotionConfig;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.TCCParamDTO;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;
import com.xyl.mmall.promotion.service.tcc.ActivityTCCService;

/**
 * ActivityTCCServiceTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-17
 * @since      1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PromotionConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class ActivityTCCServiceTest {
	
	@Resource
	private ActivityTCCService activityTCCService;
	
	static {
		System.setProperty("spring.profiles.active", "dev");
	}
	
	@Test
	public void testAdd() {
		//构建参数
		TCCParamDTO tccParamDTO = new TCCParamDTO();
		tccParamDTO.setOrderId(12);
		tccParamDTO.setUserId(475);
		
		FavorCaculateResultDTO resultDTO = new FavorCaculateResultDTO();
		
		//优惠券订单
		List<CouponOrderTCC> couponOrderTCCs = new ArrayList<>();
		CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
		couponOrderTCC.setCouponCode("RN37B49Q");
		couponOrderTCC.setCtimeOfTCC(System.currentTimeMillis());
		couponOrderTCC.setUserId(tccParamDTO.getUserId());
		couponOrderTCC.setCouponOrderType(CouponOrderType.USE_COUPON);
		
		CouponOrderTCC couponOrderTCC2 = new CouponOrderTCC();
		couponOrderTCC2.setCouponCode("R5ST6CGN");
		couponOrderTCC2.setCtimeOfTCC(System.currentTimeMillis());
		couponOrderTCC2.setUserId(tccParamDTO.getUserId());
		couponOrderTCC2.setCouponOrderType(CouponOrderType.RETURN_COUPON);
		
		couponOrderTCCs.add(couponOrderTCC);
		couponOrderTCCs.add(couponOrderTCC2);
		resultDTO.setCouponOrderTCCList(couponOrderTCCs);
		
		//红包订单
		List<RedPacketOrderTCC> redPacketOrderTCCs = new ArrayList<>();
		RedPacketOrderTCC redPacketOrderTCC = new RedPacketOrderTCC();
		redPacketOrderTCC.setCash(new BigDecimal(1));
		redPacketOrderTCC.setCtimeOfTCC(System.currentTimeMillis());
		redPacketOrderTCC.setUserRedPacketId(1);
		redPacketOrderTCC.setUsedTime(System.currentTimeMillis());
		redPacketOrderTCC.setRedPacketId(1);
		redPacketOrderTCCs.add(redPacketOrderTCC);
		resultDTO.setRedPacketOrderTCCList(redPacketOrderTCCs);
		
		
		activityTCCService.tryAddActivityTCC(1, tccParamDTO, resultDTO);
	}
	
	@Test
	public void testConfirm() {
		activityTCCService.confirmAddActivityTCC(1);
	}
	
	@Test
	public void testCancel() {
		activityTCCService.cancelAddActivityTCC(1);
	}
}
