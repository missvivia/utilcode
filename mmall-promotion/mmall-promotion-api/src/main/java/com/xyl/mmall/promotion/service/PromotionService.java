/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * PromotionService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-9
 * @since      1.0
 */
public interface PromotionService {
	
	/**
	 * 获取活动
	 */
	List<Promotion> getDeclarePromotionList(long areaPermission, PlatformType platformType);
	
	/**
	 * 获取活动列表
	 * @param state 状态
	 * @param province 站点
	 * @param limit 一次获取数量
	 * @param offset 偏移量
	 * @return
	 */
	List<Promotion> getPromotionList(long userId, int state, long areaPermission, int limit, int offset);
	
	/**
	 * 获取记录总数
	 * @param state
	 * @param province
	 * @return
	 */
	int getPromotionCount(long userId, int state, long areaPermission);
	
	/**
	 * 添加一个活动
	 * @param promotion 活动对象
	 * @return
	 */
	Promotion addPromotion(Promotion promotion);
	
	/**
	 * 修改一条活动
	 * @param promotion 活动对象
	 * @return
	 */
	boolean updatePromotion(Promotion promotion);
	
	/**
	 * 获取指定id的活动
	 * @param id
	 * @return
	 */
	Promotion getPromotionById(long id);
	
	/**
	 * 通过指定的po获取活动
	 * @param poId
	 * @return
	 */
	Promotion getPromotionByPO(long poId, long id, long start, long end, boolean isValid);
	
	/**
	 * 通过指定的po获取活动
	 * @param poId
	 * @return
	 */
	Promotion getPromotionByPO(long poId, long id, boolean isValid);
	
	/**
	 * 获取列表对象
	 * @param pos
	 * @return
	 */
	Map<Long, PromotionDTO> getPromotionListByPos(List<Long> pos, boolean isValid);
	
	/**
	 * 获取活动提示
	 * @param pos
	 * @param isValid
	 * @return
	 */
	Map<Long, String> getPromotionTipMap(List<Long> pos, boolean isValid);
	
	/**
	 * 获取提交审核的map,根据省份
	 * @return
	 */
	Map<Integer, Integer> getCommitMap();
	
	/**
	 * 审核的活动
	 * @param promotionQueryBeanDTO
	 * @return
	 */
	List<Promotion> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO);

	/**
	 * 审核的活动总数
	 * @param promotionQueryBeanDTO
	 * @return
	 */
	int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO);
}
