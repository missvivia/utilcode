/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mainsite.facade;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketParam;
import com.xyl.mmall.promotion.dto.RefundDTO;

/**
 * CalCenterFacade.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-22
 * @since 1.0
 */
public interface CalCenterFacade {

	/**
	 * 计算活动(包括活动和优惠券)
	 * 
	 * @param skuItems
	 * @param paramDTO
	 * @param resultDTO
	 */
	FavorCaculateResultDTO caculateActivation(Map<Long, List<PromotionSkuItemDTO>> skuPoListMap,
			FavorCaculateParamDTO paramDTO);
	
    
    /**
     * 新增优惠券计算，by: durianskh@gmail.com
     * 
     * @param paramDTO
     * @param resultDTO
     */
    void caculateMmallCoupon(FavorCaculateParamDTO paramDTO, FavorCaculateResultDTO resultDTO);

	/**
	 * 判断用户的优惠券
	 * 
	 * @param skuItems
	 * @param paramDTO
	 * @param resultDTO
	 */
	FavorCaculateResultDTO bindCoupon(Map<Long, List<PromotionSkuItemDTO>> skuItems, FavorCaculateParamDTO paramDTO);

	/**
	 * 退款计算
	 * 
	 * @param refundItems
	 *            要退款的 key:po value:po list
	 * @param skuItems
	 *            订单去掉要退款的部分 key:po value:po list
	 * @param 参数
	 * @return
	 */
	RefundDTO refundCaculation(Map<Long, List<PromotionSkuItemDTO>> refundItems,
			Map<Long, List<PromotionSkuItemDTO>> skuItems, FavorCaculateParamDTO paramDTO);
	
	/**
	 * 计算红包
	 * @param skuParam
	 * @return
	 */
	RedPacketParam caculateRedPackets(RedPacketParam redPacketParam);
	
	/**
	 * 绑定优惠券
	 * @param userId
	 * @param userName
	 * @param couponCode
	 * @return
	 */
	public FavorCaculateResultDTO bindCoupon(long userId, String userName, String couponCode);
}
