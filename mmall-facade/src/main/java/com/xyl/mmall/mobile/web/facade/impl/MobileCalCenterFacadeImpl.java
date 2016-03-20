/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.mobile.web.facade.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.mobile.web.facade.MobileCalCenterFacade;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketParam;
import com.xyl.mmall.promotion.dto.RefundDTO;
import com.xyl.mmall.promotion.service.FavorCaculateService;

/**
 * CalCenterFacadeImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-22
 * @since 1.0
 */

@Facade("mobileCalCenterFacade")
public class MobileCalCenterFacadeImpl implements MobileCalCenterFacade {

	@Autowired
	private FavorCaculateService favorCaculateService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private PromotionFacade promotionFacade;
	
	@Override
	public FavorCaculateResultDTO caculateActivation(Map<Long, List<PromotionSkuItemDTO>> skuItems,
			FavorCaculateParamDTO paramDTO) {
//		long areaPermission = promotionFacade.getProvinceCodePermission(paramDTO.getProvince());
//		paramDTO.setAreaPermission(areaPermission);
		
		FavorCaculateResultDTO resultDTO = favorCaculateService.caculateActivation(skuItems, paramDTO);
		return resultDTO;
	}
	
    // 新增计算优惠券的接口，by: durianskh@gmail.com
    public void caculateMmallCoupon(FavorCaculateParamDTO paramDTO, FavorCaculateResultDTO resultDTO)
    {
        favorCaculateService.caculateMmallCoupon(paramDTO, resultDTO);
    }
	

	@Override
	public FavorCaculateResultDTO bindCoupon(Map<Long, List<PromotionSkuItemDTO>> skuItems,
			FavorCaculateParamDTO paramDTO) {
		FavorCaculateResultDTO resultDTO = favorCaculateService.bindCoupon(skuItems, paramDTO);
		return resultDTO;
	}
	
	@Override
	public RedPacketParam caculateRedPackets(RedPacketParam redPacketParam) {
		return favorCaculateService.caculateRedPackets(redPacketParam);
	}
	
	@Override
	public RefundDTO refundCaculation(Map<Long, List<PromotionSkuItemDTO>> refundItemMap,
			Map<Long, List<PromotionSkuItemDTO>> skuItemMap, FavorCaculateParamDTO paramDTO) {
		RefundDTO refundDTO = new RefundDTO();
		if (CollectionUtils.isEmpty(refundItemMap)) {
			return null;
		}
		
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			for (PromotionSkuItemDTO dto : entry.getValue()) {
				refundDTO.setTotalOriCash(refundDTO.getTotalOriCash().add(dto.getOriRetailPrice().multiply(new BigDecimal(dto.getCount()))));
			}
		}
		
		//全退
		if (CollectionUtils.isEmpty(skuItemMap)) {
			refundAll(refundItemMap, refundDTO);
			refundDTO.setExpFree(false);
			return refundDTO;
		}
		
		//购买时没有使用任何的活动和优惠券
		if (CollectionUtils.isEmpty(paramDTO.getPoCouponMap()) && CollectionUtils.isEmpty(paramDTO.getPoPromotionMap())) {
			refundAll(refundItemMap, refundDTO);
			refundDTO.setExpFree(false);
			return refundDTO;
		}
		
		paramDTO.setRefund(true);
		// 计算每个po实际支付总额
		Map<Long, BigDecimal> totalPriceMap = caculateTotalRetailPrice(refundItemMap, skuItemMap);
		// 优惠券优惠总金额
		BigDecimal couponCash = caculateCouponCash(refundItemMap, skuItemMap);
		// 每个活动的优惠金额
		Map<Long, BigDecimal> promotionDiscountMap = caculatePromotionDiscount(refundItemMap, skuItemMap);
		
		paramDTO.setCaculateCoupon(true);

		FavorCaculateResultDTO resultDTO = caculateActivation(skuItemMap, paramDTO);
		refundDTO.setExpFree(resultDTO.isFreeExpPrice());
		if (resultDTO.getCouponDTO() == null && !CollectionUtils.isEmpty(paramDTO.getPoCouponMap())) {
			// 传入的优惠券不为空，计算时不符合优惠券，将使用的返回给用户
			refundDTO.setReturn(true);
			// 扣除优惠券优惠金额
			refundDTO.setCouponCash(couponCash);
		} else {
			//总的优惠券使用金额减去不需要退款使用的优惠券金额，需要扣除的优惠券金额
			BigDecimal coupon = couponCash.subtract(resultDTO.getTotalCouponDiscount());
			//扣除的优惠券优惠金额
			refundDTO.setCouponCash(coupon.compareTo(BigDecimal.ZERO) > 0 ? coupon : BigDecimal.ZERO);
		}
		// 结果中满足条件的
		List<PromotionCartDTO> cartDTOs = resultDTO.getActivations();
		// 结果中不满足条件的
		Map<Long, List<PromotionSkuItemDTO>> poSkuMap = resultDTO.getNotSatisfySkuList();

//		caculateCartDTO(refundItemMap, cartDTOs, totalPriceMap, promotionDiscountMap, refundDTO);
		moveCartToMap(cartDTOs, poSkuMap);
		//cartDTOs为空时，需要添加
		caculateMap(refundItemMap, poSkuMap, totalPriceMap, promotionDiscountMap, refundDTO);
		BigDecimal refund = refundDTO.getRefundTotalCash().setScale(2, BigDecimal.ROUND_HALF_UP);
		refund = refund.compareTo(BigDecimal.ZERO) > 0 ? refund : BigDecimal.ZERO;
		refundDTO.setRefundTotalCash(refund);
		BigDecimal promotion = refundDTO.getPromotionCash();
		promotion = promotion.compareTo(BigDecimal.ZERO) > 0 ? promotion : BigDecimal.ZERO;
		refundDTO.setPromotionCash(promotion);
		
		refundDTO.setRefundMap(refundItemMap);
		return refundDTO;
	}

	private void moveCartToMap(List<PromotionCartDTO> cartDTOs, Map<Long, List<PromotionSkuItemDTO>> poSkuMap) {
		if (CollectionUtils.isEmpty(cartDTOs)) {
			return;
		}
		
		for (PromotionCartDTO cartDTO : cartDTOs) {
			poSkuMap.putAll(cartDTO.getPoSkuMap());
		}
	}

	private void refundAll(Map<Long, List<PromotionSkuItemDTO>> refundItemMap, RefundDTO refundDTO) {
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			for (PromotionSkuItemDTO dto : entry.getValue()) {
				//优惠券
				if (dto.getCouponDiscountAmount() != null) {
					refundDTO.setCouponCash(refundDTO.getCouponCash().add(dto.getCouponDiscountAmount().multiply(new BigDecimal(dto.getCount()))));
				}
				//活动
				if (dto.getPromotionDiscountAmount() != null) {
					refundDTO.setPromotionCash(refundDTO.getPromotionCash().add(dto.getPromotionDiscountAmount().multiply(new BigDecimal(dto.getCount()))));
				}
				//销售价
				if (dto.getRetailPrice() != null) {
					refundDTO.setRefundTotalCash(refundDTO.getRefundTotalCash().add(dto.getRetailPrice().multiply(new BigDecimal(dto.getCount()))));
				}
				refundDTO.setReturn(true);
				refundDTO.setRefundMap(refundItemMap);
			}
		}
	}

	/**
	 * 计算每个活动的优惠金额
	 * 
	 * @param refundItemMap
	 * @param skuItemMap
	 * @return
	 */
	private Map<Long, BigDecimal> caculatePromotionDiscount(Map<Long, List<PromotionSkuItemDTO>> refundItemMap,
			Map<Long, List<PromotionSkuItemDTO>> skuItemMap) {
		Map<Long, BigDecimal> map = new HashMap<>();
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			BigDecimal discount = BigDecimal.ZERO;
			// 退款部分活动优惠
			List<PromotionSkuItemDTO> refunds = entry.getValue();
			// 重新计算部分优惠
			List<PromotionSkuItemDTO> buys = skuItemMap.get(entry.getKey());
			// 退款部分金额
			discount = discount.add(addDiscountPrice(refunds));
			// 购买部分金额，最终零售价设置为原价，便于重新计算
			discount = discount.add(addDiscountPrice(buys));

			map.put(entry.getKey(), discount);
		}
		
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : skuItemMap.entrySet()) {
			BigDecimal discount = BigDecimal.ZERO;
			if (map.containsKey(entry.getKey())) {
				continue;
			}
			discount = discount.add(addDiscountPrice(entry.getValue()));
			map.put(entry.getKey(), discount);
		}
		
		return map;
	}

	private BigDecimal addDiscountPrice(List<PromotionSkuItemDTO> items) {
		BigDecimal total = BigDecimal.ZERO;
		if (CollectionUtils.isEmpty(items)) {
			return total;
		}

		for (PromotionSkuItemDTO dto : items) {
			if (dto.getPromotionDiscountAmount() != null) {
				total = total.add(dto.getPromotionDiscountAmount().multiply(new BigDecimal(dto.getCount())));
			}
			dto.setPromotionDiscountAmount(BigDecimal.ZERO);
		}

		return total;
	}

	/**
	 * 计算优惠券的优惠总金额
	 * 
	 * @param refundItemMap
	 * @param skuItemMap
	 * @return
	 */
	private BigDecimal caculateCouponCash(Map<Long, List<PromotionSkuItemDTO>> refundItemMap,
			Map<Long, List<PromotionSkuItemDTO>> skuItemMap) {
		BigDecimal couponCash = BigDecimal.ZERO;
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			for (PromotionSkuItemDTO dto : entry.getValue()) {
				if (dto.getCouponDiscountAmount() != null) {
					couponCash = couponCash.add(dto.getCouponDiscountAmount().multiply(new BigDecimal(dto.getCount())));
				}
			}
		}

		for (Entry<Long, List<PromotionSkuItemDTO>> entry : skuItemMap.entrySet()) {
			for (PromotionSkuItemDTO dto : entry.getValue()) {
				if (dto.getCouponDiscountAmount() != null) {
					couponCash = couponCash.add(dto.getCouponDiscountAmount().multiply(new BigDecimal(dto.getCount())));
				}
			}
		}
		return couponCash;
	}

	/**
	 * 计算每个po的总价
	 * 
	 * @param refundItemMap
	 * @param skuItemMap
	 * @return
	 */
	private Map<Long, BigDecimal> caculateTotalRetailPrice(Map<Long, List<PromotionSkuItemDTO>> refundItemMap,
			Map<Long, List<PromotionSkuItemDTO>> skuItemMap) {
		Map<Long, BigDecimal> map = new HashMap<>();
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			BigDecimal total = BigDecimal.ZERO;
			// 退款部分
			List<PromotionSkuItemDTO> refunds = entry.getValue();
			// 重新计算部分
			List<PromotionSkuItemDTO> buys = skuItemMap.get(entry.getKey());
			// 退款部分金额
			total = total.add(addRetailPrice(refunds, false));
			// 购买部分金额，最终零售价设置为原价，便于重新计算
			total = total.add(addRetailPrice(buys, true));

			map.put(entry.getKey(), total);
		}
		
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : skuItemMap.entrySet()) {
			//如果map中包含po，退出，已经计算过
			if (map.containsKey(entry.getKey())) {
				continue;
			}
			BigDecimal total = BigDecimal.ZERO;
			total = total.add(addRetailPrice(entry.getValue(), true));
			map.put(entry.getKey(), total);
		}
		
		return map;
	}

	/**
	 * 计算总价
	 * 
	 * @param poId
	 * @param map
	 * @param items
	 */
	private BigDecimal addRetailPrice(List<PromotionSkuItemDTO> items, boolean reset) {
		BigDecimal total = BigDecimal.ZERO;
		if (CollectionUtils.isEmpty(items)) {
			return total;
		}

		for (PromotionSkuItemDTO dto : items) {
			total = total.add(dto.getRetailPrice().multiply(new BigDecimal(dto.getCount())));
			if (reset) {
				dto.setRetailPrice(dto.getOriRetailPrice());
			}
		}

		return total;
	}

	/**
	 * po退款价格计算
	 * 
	 * @param refundItemMap
	 * @param map
	 * @param totalPriceMap
	 * @param poSkuMap
	 * @param promotionDiscountMap
	 * @param refundDTO
	 * @param addOri refund不包含部分是否添加到退款总额中
	 * @return
	 */
	private void caculateMap(Map<Long, List<PromotionSkuItemDTO>> refundItemMap,
			Map<Long, List<PromotionSkuItemDTO>> poSkuMap, Map<Long, BigDecimal> totalPriceMap,
			Map<Long, BigDecimal> promotionDiscountMap, RefundDTO refundDTO) {
		if (CollectionUtils.isEmpty(poSkuMap)) {
			return;
		}
		//临时价格，用于记录不相交的退款
		BigDecimal tmpCash = BigDecimal.ZERO;
		
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : poSkuMap.entrySet()) {
			// 单个po的总价
			List<PromotionSkuItemDTO> buys = entry.getValue();
			BigDecimal total = addRetailPrice(buys, false);
			// 购买时支付的金额
			BigDecimal oriTotal = totalPriceMap.get(entry.getKey());
			// 需要退给用户的金额
			BigDecimal refundCash = oriTotal.subtract(total);
			//活动优惠金额
			BigDecimal promotionDiscountCash = addPromotionDiscountCash(buys);
			if (CollectionUtils.isEmpty(refundItemMap.get(entry.getKey()))) {
				tmpCash = tmpCash.add(refundCash);
			} else {
			    // 购买时支付的价格小于等于不享受活动的价格，不退款
				if (refundCash.compareTo(BigDecimal.ZERO) <= 0) {
					refundCash = BigDecimal.ZERO;
					// 将退款的价格设置成0
					setRefundItemPrice(refundDTO, refundCash, refundItemMap.get(entry.getKey()));
				} else {
					//设置退款总金额
					refundDTO.setRefundTotalCash(refundDTO.getRefundTotalCash().add(refundCash));
					setRefundItemPrice(refundDTO, refundCash, refundItemMap.get(entry.getKey()));
				}
				//扣除活动优惠价格
				BigDecimal discount = promotionDiscountMap.get(entry.getKey());
				if (discount == null) {
					continue;
				}
				BigDecimal pCash = refundDTO.getPromotionCash().add(discount.subtract(promotionDiscountCash));
				refundDTO.setPromotionCash(pCash);
			}
		}
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : refundItemMap.entrySet()) {
			//相同的key已经处理过
			if (poSkuMap.containsKey(entry.getKey())) {
				continue;
			}
			// 购买时支付的金额
			BigDecimal oriTotal = totalPriceMap.get(entry.getKey());
			//若不包含，扣除此po参与活动的价格
			refundDTO.setPromotionCash(refundDTO.getPromotionCash().add(promotionDiscountMap.get(entry.getKey())));
			refundDTO.setRefundTotalCash(refundDTO.getRefundTotalCash().add(oriTotal));
		}
		
		refundDTO.setRefundTotalCash(refundDTO.getRefundTotalCash().add(tmpCash));
	}

	private BigDecimal addPromotionDiscountCash(List<PromotionSkuItemDTO> buys) {
		BigDecimal discountCash = BigDecimal.ZERO;
		if (CollectionUtils.isEmpty(buys)) {
			return discountCash;
		}
		
		for (PromotionSkuItemDTO dto : buys) {
			if (dto.getPromotionDiscountAmount() != null) {
				discountCash = discountCash.add(dto.getPromotionDiscountAmount().multiply(new BigDecimal(dto.getCount())));
			}
		}
		return discountCash;
	}

	/**
	 * 设置退款项的价格
	 * 
	 * @param refundCash
	 * @param refundItems
	 */
	private void setRefundItemPrice(RefundDTO refundDTO, BigDecimal refundCash, List<PromotionSkuItemDTO> refundItems) {
		if (CollectionUtils.isEmpty(refundItems)) {
			return;
		}

		BigDecimal remainCash = refundCash;
		Collections.sort(refundItems);
		for (PromotionSkuItemDTO dto : refundItems) {
			//设置原始总价
			if (remainCash.compareTo(BigDecimal.ZERO) == 0) {
				dto.setRetailPrice(BigDecimal.ZERO);
				continue;
			}
			
			//退款金额大于商品零售价
			if (remainCash.compareTo(dto.getOriRetailPrice().multiply(new BigDecimal(dto.getCount()))) > 0) {
				// 设置原价,要除以数量
				dto.setRetailPrice(dto.getOriRetailPrice());
				dto.setCouponDiscountAmount(BigDecimal.ZERO);
				dto.setPromotionDiscountAmount(BigDecimal.ZERO);
				remainCash = remainCash.subtract(dto.getOriRetailPrice().multiply(new BigDecimal(dto.getCount())));
			} else {
				dto.setRetailPrice(remainCash.divide(new BigDecimal(dto.getCount()), 2, BigDecimal.ROUND_HALF_UP));
				remainCash = BigDecimal.ZERO;
			}
		}
	}

	@Override
	public FavorCaculateResultDTO bindCoupon(long userId, String userName, String couponCode) {
		return favorCaculateService.bindCoupon(couponCode, userId, userName);
	}
}
