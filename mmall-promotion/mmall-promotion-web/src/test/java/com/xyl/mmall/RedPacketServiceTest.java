/*
 * @(#) 2014-10-16
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.PromotionConfig;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.enums.DistributeRule;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.service.RedPacketService;

/**
 * RedPacketServiceTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-16
 * @since      1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PromotionConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class RedPacketServiceTest {
	
	@Resource
	private RedPacketService redPacketService;
	
	static {
		System.setProperty("spring.profiles.active", "dev");
	}
	
	@Test
	public void testAddRedPacket() {
		long current = System.currentTimeMillis();
		RedPacket redPacket = new RedPacket();
		redPacket.setApplyUserId(475);
		redPacket.setCash(new BigDecimal(10));
		redPacket.setCopies(5);
		redPacket.setCount(5);
		redPacket.setDescription("redpacket unit test add");
		redPacket.setDistributeRule(DistributeRule.RANDOM);
		redPacket.setEndTime(current + 1000 * 60 * 60 * 24 * 10L);
		redPacket.setName("红包添加");
		redPacket.setShare(true);
		redPacket.setStartTime(current);
		
		redPacketService.addRedPacket(redPacket);
	}
	
	@Test
	public void testCommitRedPacket() {
		RedPacket redPacket = redPacketService.getRedPacketById(2447);
		Assert.assertNotNull(redPacket);
		redPacket.setAuditState(StateConstants.COMMIT);
		
		redPacketService.updateRedPacket(new RedPacketDTO(redPacket));
	}
	
	@Test
	public void testRefusedRedPacket() {
		RedPacket redPacket = redPacketService.getRedPacketById(2447);
		Assert.assertNotNull(redPacket);
		redPacket.setAuditState(StateConstants.REFUSED);
		
		redPacketService.updateRedPacket(new RedPacketDTO(redPacket));
	}
	
	@Test
	public void testCancelRedPacket() {
		RedPacket redPacket = redPacketService.getRedPacketById(2447);
		Assert.assertNotNull(redPacket);
		redPacket.setAuditState(StateConstants.CANCEL);
		
		redPacketService.updateRedPacket(new RedPacketDTO(redPacket));
	}
	
	@Test
	public void testPassRedPacket() {
		RedPacket redPacket = redPacketService.getRedPacketById(2447);
		Assert.assertNotNull(redPacket);
		redPacket.setAuditState(StateConstants.PASS);
		redPacket.setAuditTime(System.currentTimeMillis());
		redPacket.setAuditUserId(475);
		
		redPacketService.updateRedPacket(new RedPacketDTO(redPacket));
	}
}
