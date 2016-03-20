/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketParam;

/**
 * FavorCaculateService.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-10
 * @since      1.0
 */
public interface FavorCaculateService {
	
	/**
	 * 计算活动
	 * @param skuItems key:poid value:po的skuitem列表
	 * @param paramDTO 参数
	 * @param resultDTO 结果
	 */
	FavorCaculateResultDTO caculateActivation(Map<Long, List<PromotionSkuItemDTO>> skuItems, FavorCaculateParamDTO paramDTO);
	
    /**
     * 
     * @param paramDTO
     * @param resultDTO
     */
    void caculateMmallCoupon(FavorCaculateParamDTO paramDTO, FavorCaculateResultDTO resultDTO);
	
	/**
	 * 绑定优惠券
	 * @param skuPoMap
	 * @param paramDTO
	 * @return
	 */
	FavorCaculateResultDTO bindCoupon(Map<Long, List<PromotionSkuItemDTO>> skuPoMap, FavorCaculateParamDTO paramDTO);

	/**
	 * 计算红包
	 * @param skuParam
	 */
	RedPacketParam caculateRedPackets(RedPacketParam redPacketParam);
	
    /**
     * 判断优惠券是否满足使用条件，by: durianskh@gmail.com
     * 
     * @param condition
     * @param base
     * @return
     */
    Boolean judgeCouponCondition(Condition condition, Object base);
	
	/**
	 * 绑定优惠券
	 * @param couponCode
	 * @param userId
	 * @param userName
	 * @return
	 */
	public FavorCaculateResultDTO bindCoupon(String couponCode, long userId, String userName);
}
