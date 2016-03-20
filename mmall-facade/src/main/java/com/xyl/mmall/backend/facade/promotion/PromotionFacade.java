/*
 * 2014-9-19
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;

/**
 * PromotionFacade.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-19
 * @since      1.0
 */
public interface PromotionFacade {
	/**
	 * 获取活动列表
	 * @param state 状态
	 * @param province 站点
	 * @param limit 一次获取数量
	 * @param offset 偏移量
	 * @return
	 */
	List<PromotionDTO> getPromotionList(long userId, int state, long province, int limit, int offset);
	
	/**
	 * 获取区域权限
	 * @param areaDTOs
	 * @return
	 */
	long getAreaPermission(List<AreaDTO> areaDTOs);
	
	/**
	 * 获取某个省份的权限值
	 * @param province
	 * @return
	 */
	long getProvinceCodePermission(long province);
	
	/**
	 * 获取记录总数
	 * @param state
	 * @param province
	 * @return
	 */
	int getPromotionCount(long userId, int state, long province);
	
	/**
	 * 添加一个活动
	 * @param promotion 活动对象
	 * @return
	 */
	PromotionDTO addPromotion(PromotionDTO promotion);
	
	/**
	 * 修改一条活动
	 * @param promotion 活动对象
	 * @return
	 */
	boolean updatePromotion(PromotionDTO promotion);
	
	/**
	 * 获取指定id的活动
	 * @param id
	 * @return
	 */
	PromotionDTO getPromotionById(long id);
	
	/**
	 * 通过指定的po获取活动
	 * @param po
	 * @return
	 */
	PromotionDTO getPromotionByPO(long po, long id, long start, long end, boolean isValid);
	
	/**
	 * 获取po列表
	 * @param pos
	 * @return
	 */
	Map<Long, PromotionDTO> getPromotionListByPos(List<Long> pos, boolean isValid);
	
	/**
	 * 通过指定的po获取活动
	 * @param po
	 * @return
	 */
	PromotionDTO getPromotionByPO(long poId, long id, boolean isValid);
	
	/**
	 * 审核的活动
	 * @param promotionQueryBeanDTO
	 * @return
	 */
	List<PromotionDTO> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO);

	/**
	 * 审核的活动数量
	 * @param promotionQueryBeanDTO
	 * @return
	 */
	int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO);
}
