/*
 * @(#) 2014-10-22
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.MainsiteConfig;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;

/**
 * CalCenterFacadeTest.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-10-22
 * @since      1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainsiteConfig.class, FacadeConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class CalCenterFacadeTest {
	
	@Resource
	private CalCenterFacade calCenterFacade;
	
	static {
		System.setProperty("spring.profiles.active", "dev");
	}
	
	@Test
	public void testRefund() {
		
		Map<Long, List<PromotionSkuItemDTO>> refundItems = new HashMap<>();
		
		List<PromotionSkuItemDTO> dtos = new ArrayList<>();
		PromotionSkuItemDTO dto = new PromotionSkuItemDTO();
		dto.setSkuId(1895);
		dto.setUserId(475);
		dto.setCartPrice(new BigDecimal(275));
		dto.setCount(1);
		dto.setOriRetailPrice(new BigDecimal(300));
		dto.setRetailPrice(new BigDecimal(275));
		
		dtos.add(dto);
		
		refundItems.put(877L, dtos);
		
		Map<Long, List<PromotionSkuItemDTO>> skuItems = new HashMap<>();
		
		List<PromotionSkuItemDTO> skuItemDTOs = new ArrayList<>();
		PromotionSkuItemDTO skuItemDto = new PromotionSkuItemDTO();
		skuItemDto.setSkuId(1895);
		skuItemDto.setUserId(475);
		skuItemDto.setCartPrice(new BigDecimal(275));
		skuItemDto.setCount(1);
		skuItemDto.setOriRetailPrice(new BigDecimal(300));
		skuItemDto.setRetailPrice(new BigDecimal(275));
		
		skuItemDTOs.add(skuItemDto);
		
		skuItems.put(877L, skuItemDTOs);
		
		
		FavorCaculateParamDTO paramDTO = new FavorCaculateParamDTO();
		paramDTO.setProvince(1);
		paramDTO.setUserId(475);
		
		Map<Long, PromotionDTO> map = new HashMap<>();
		PromotionDTO promotionDTO = new PromotionDTO();
		promotionDTO.setId(1733);
		promotionDTO.setIndex(2);
		map.put(877L, promotionDTO);
		
		paramDTO.setPoPromotionMap(map);
		
		calCenterFacade.refundCaculation(refundItems, skuItems, paramDTO);
	}
	
}
