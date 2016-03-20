/*
 * @(#) 2014-10-16
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.PromotionConfig;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.service.FavorCaculateService;

/**
 * FavorCalculateServiceTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-16
 * @since      1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PromotionConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class FavorCalculateServiceTest {
	
	@Resource
	private FavorCaculateService favorCaculateService;
	
	static {
		System.setProperty("spring.profiles.active", "dev");
	}
	
	@Test
	public void testBindCoupon() {
		Map<Long, List<PromotionSkuItemDTO>> skuPoMap = new HashMap<>();
		
		List<PromotionSkuItemDTO> dtos = new ArrayList<>();
		PromotionSkuItemDTO dto = new PromotionSkuItemDTO();
		dto.setSkuId(1895);
		dto.setUserId(475);
		dto.setCartPrice(new BigDecimal(200));
		dto.setCount(1);
		dto.setOriRetailPrice(new BigDecimal(200));
		dto.setRetailPrice(new BigDecimal(200));
		
		dtos.add(dto);
		
		List<PromotionSkuItemDTO> dtos2 = new ArrayList<>();
		PromotionSkuItemDTO dto2 = new PromotionSkuItemDTO();
		dto2.setSkuId(1946);
		dto2.setUserId(475);
		dto2.setCartPrice(new BigDecimal(200));
		dto2.setCount(1);
		dto2.setOriRetailPrice(new BigDecimal(200));
		dto2.setRetailPrice(new BigDecimal(200));
		
		dtos2.add(dto2);
		
		skuPoMap.put(877L, dtos);
		skuPoMap.put(1909L, dtos2);
		
		
		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setProvince(1);
		paramDTO.setUserId(475);
		paramDTO.setCouponCode("RN37B49Q");
		FavorCaculateResultDTO resultDTO = favorCaculateService.bindCoupon(skuPoMap, paramDTO);
		Assert.assertNotNull(resultDTO);
	}
	
	@Test
	public void pi() {
		Map<Long, List<PromotionSkuItemDTO>> skuPoMap = new HashMap<>();
		
		List<PromotionSkuItemDTO> dtos = new ArrayList<>();
		PromotionSkuItemDTO dto = new PromotionSkuItemDTO();
		dto.setSkuId(1895);
		dto.setUserId(475);
		dto.setCartPrice(new BigDecimal(200));
		dto.setCount(2);
		dto.setOriRetailPrice(new BigDecimal(200));
		dto.setRetailPrice(new BigDecimal(200));
		
		dtos.add(dto);
		
		skuPoMap.put(877L, dtos);
		
		
		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setProvince(1);
		paramDTO.setUserId(475);
		paramDTO.setCaculateCoupon(true);
		paramDTO.setCouponCode("test11090");
		FavorCaculateResultDTO resultDTO = favorCaculateService.caculateActivation(skuPoMap, paramDTO);
		Assert.assertNotNull(resultDTO);
	}
}
