/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dao.PromotionDao;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.promotion.enums.ConditionType;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.promotion.utils.ActivationUtils;
import com.xyl.mmall.promotion.utils.CartTipUtils;

/**
 * PromotionServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
@Service("promotionService")
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionDao promotionDao;

	@Override
	public List<Promotion> getPromotionList(long userId, int state, long areaPermission, int limit, int offset) {
		return getPromotionListByExpiredState(userId, false, state, 0, areaPermission, null, limit, offset);
	}

	@Override
	public int getPromotionCount(long userId, int state, long areaPermission) {
		return promotionDao.getPromotionCount(userId, false, state, areaPermission);
	}

	@Override
	public Promotion addPromotion(Promotion promotion) {
		return promotionDao.addPromotion(promotion);
	}

	@Override
	@CacheEvict(value="promotionCache", allEntries = true)
	public boolean updatePromotion(Promotion promotion) {
		return promotionDao.updatePromotion(promotion);
	}

	@Override
	@Cacheable(value="promotionCache")
	public Promotion getPromotionById(long id) {
		return promotionDao.getPromotionById(id);
	}

	@Override
	public List<Promotion> getDeclarePromotionList(long areaPermission, PlatformType platformType) {
		List<Promotion> list = getPromotionListByExpiredState(-1, true, StateConstants.PASS, 0, areaPermission,
				platformType, Integer.MAX_VALUE, 0);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		return list;
	}

	private List<Promotion> getPromotionListByExpiredState(long userId, boolean isNoExpiredOnly, int state,
			long fullPermission, long selectedPermission, PlatformType platformType, int limit, int offset) {
		return promotionDao.getPromotionList(userId, isNoExpiredOnly, state, fullPermission, selectedPermission, platformType, limit, offset);
	}

	@Override
	public Promotion getPromotionByPO(long poId, long id, long start, long end, boolean isValid) {
		return promotionDao.getPromotionByPO(String.valueOf(poId), id, start, end, isValid);
	}

	@Override
	public Map<Long, PromotionDTO> getPromotionListByPos(List<Long> pos, boolean isValid) {
		if (CollectionUtils.isEmpty(pos)) {
			return null;
		}

		Map<Long, PromotionDTO> poMap = new HashMap<>();

		for (long po : pos) {
			Promotion promotion = getPromotionByPO(po, 0, isValid);
			if (promotion == null) {
				continue;
			}
			poMap.put(po, new PromotionDTO(promotion));
		}
		return poMap;
	}

	@Override
	public Map<Integer, Integer> getCommitMap() {
		Map<Integer, Integer> map = new HashMap<>();
		List<Promotion> promotions = getPromotionList(-1, StateConstants.COMMIT, 0, Integer.MAX_VALUE, 0);
		if (CollectionUtils.isEmpty(promotions)) {
			return map;
		}

		for (Promotion promotion : promotions) {
			String provinces = promotion.getSelectedProvince();
			if (StringUtils.isBlank(provinces)) {
				continue;
			}

			for (String province : provinces.split(",")) {
				int provinceId = Integer.valueOf(province);
				if (map.containsKey(provinceId)) {
					map.put(provinceId, map.get(provinceId) + 1);
				} else {
					map.put(provinceId, 1);
				}
			}
		}

		return map;
	}

	@Override
	public Promotion getPromotionByPO(long poId, long id, boolean isValid) {
		return getPromotionByPO(poId, id, 0, 0, isValid);
	}

	@Override
	public List<Promotion> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		return promotionDao.getCommitPromotions(promotionQueryBeanDTO);
	}

	@Override
	public int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO) {
		return promotionDao.getCommitPromotionCount(promotionQueryBeanDTO);
	}

	@Override
	public Map<Long, String> getPromotionTipMap(List<Long> pos, boolean isValid) {
		Map<Long, PromotionDTO> map = getPromotionListByPos(pos, isValid);
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		
		Map<Long, String> tipMap = new HashMap<>();
		
		for (Entry<Long, PromotionDTO> entry : map.entrySet()) {
			PromotionDTO promotionDTO = entry.getValue();
			List<Activation> activations = ActivationUtils.containActivations(promotionDTO.getItems());
			if (CollectionUtils.isEmpty(activations)) {
				continue;
			}
			Activation activation = activations.get(0);
			Condition condition = activation.getCondition();
			BigDecimal price = BigDecimal.ZERO;
			int count = 0;
			if (condition.getType() == ConditionType.BASIC_PRICE_SATISFY.getIntValue()) {
				price = new BigDecimal(CartTipUtils.getFirstValue(condition.getValue()));
			} else if (condition.getType() == ConditionType.BASIC_COUNT_SATISFY.getIntValue()) {
				count += Integer.valueOf(CartTipUtils.getFirstValue(condition.getValue()));
			}
			String tip = CartTipUtils.buildTipFromCondition(activation, price, count, false, ";");
			tipMap.put(entry.getKey(), tip);
		}
		
		return tipMap;
	}
}
