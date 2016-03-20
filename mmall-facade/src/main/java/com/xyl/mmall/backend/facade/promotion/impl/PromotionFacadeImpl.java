/*
 * 2014-9-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.promotion.service.PromotionService;

/**
 * PromotionFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-19
 * @since      1.0
 */

@Facade
public class PromotionFacadeImpl implements PromotionFacade {
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private LocationService locationService;
	
	private static Map<Long, Integer> locationMap = new HashMap<>();
	
	@PostConstruct
	public void initMap() {
		if (!CollectionUtils.isEmpty(locationMap)) {
			 return;
		}
		List<LocationCode> codes = locationService.getAllProvince();
		if (CollectionUtils.isEmpty(codes)) {
			return;
		}
		
		for (int i = 0; i < codes.size(); i++) {
			locationMap.put(codes.get(i).getCode(), i);
		}
	}
	
	@Override
	public List<PromotionDTO> getPromotionList(long userId, int state, long province, int limit,
			int offset) {
		long areaPermission = getProvinceCodePermission(province);
		List<Promotion> promotions = promotionService.getPromotionList(userId, state, areaPermission, limit, offset);
		List<PromotionDTO> list = convertPromotionToDTO(promotions);
		
		return list;
	}

	@Override
	public int getPromotionCount(long userId, int state, long province) {
		long areaPermission = getProvinceCodePermission(province);
		return promotionService.getPromotionCount(userId, state, areaPermission);
	}

	public long getProvinceCodePermission(long province) {
		long areaPermission = 0;
		if (locationMap.containsKey(province)) {
			int index = locationMap.get(province);
			areaPermission = (long) Math.pow(2, index);
		}
		return areaPermission;
	}

	@Override
	public PromotionDTO addPromotion(PromotionDTO promotion) {
		if (promotion == null) {
			return null;
		}
		
		Promotion p = promotionService.addPromotion(promotion);
		if (p == null) {
			return null;
		}
		
		return new PromotionDTO(p);
	}

	@Override
	public boolean updatePromotion(PromotionDTO promotion) {
		if (promotion == null) {
			return false;
		}
		return promotionService.updatePromotion(promotion);
	}

	@Override
	public PromotionDTO getPromotionById(long id) {
		Promotion promotion = promotionService.getPromotionById(id);
		if (promotion == null) {
			return null;
		}
		PromotionDTO promotionDTO = new PromotionDTO(promotion);
		
		return promotionDTO;
	}

	@Override
	public PromotionDTO getPromotionByPO(long po, long id, long start, long end, boolean isValid) {
		Promotion promotion = promotionService.getPromotionByPO(po, id, start, end, isValid);
		if (promotion == null) {
			return null;
		}
		return new PromotionDTO(promotion);
	}

	@Override
	public Map<Long, PromotionDTO> getPromotionListByPos(List<Long> pos, boolean isValid) {
		return promotionService.getPromotionListByPos(pos, isValid);
	}

	@Override
	public PromotionDTO getPromotionByPO(long poId, long id, boolean isValid) {
		return getPromotionByPO(poId, id, 0, 0, isValid);
	}

	@Override
	public List<PromotionDTO> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		List<Promotion> promotions = promotionService.getCommitPromotions(promotionQueryBeanDTO);
		List<PromotionDTO> list = convertPromotionToDTO(promotions);
		
		return list;
	}

	private List<PromotionDTO> convertPromotionToDTO(List<Promotion> promotions) {
		if (CollectionUtils.isEmpty(promotions)) {
			return null;
		}
		
		List<PromotionDTO> list = new ArrayList<>(promotions.size());
		for (Promotion promotion : promotions) {
			PromotionDTO promotionDTO = new PromotionDTO(promotion);
			//根据id获取用户名称
			AgentDTO applyAgentDTO = agentService.findAgentById(promotion.getApplyUserId());
			if (applyAgentDTO != null) {
				promotionDTO.setApplyUserName(applyAgentDTO.getRealName());
			}
			
			AgentDTO auditAgentDTO = agentService.findAgentById(promotion.getAuditUserId());
			if (auditAgentDTO != null) {
				promotionDTO.setAuditUserName(auditAgentDTO.getRealName());
			}
			
			list.add(promotionDTO);
		}
		return list;
	}

	@Override
	public int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		return promotionService.getCommitPromotionCount(promotionQueryBeanDTO);
	}

	@Override
	public long getAreaPermission(List<AreaDTO> areaDTOs) {
		if (CollectionUtils.isEmpty(areaDTOs)) {
			return 0;
		}
		
		long areaPermission = 0;
		
		for (AreaDTO areaDTO : areaDTOs) {
			if (locationMap.containsKey(areaDTO.getId())) {
				areaPermission += (long) Math.pow(2, locationMap.get(areaDTO.getId()));
			}
		}
		return areaPermission;
	}
}
