/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.ActivitySchedule;
import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.activity.Effect;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionCartDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.dto.RedPacketParam;
import com.xyl.mmall.promotion.dto.SkuParam;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.enums.ActivationHandlerType;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.CodeType;
import com.xyl.mmall.promotion.enums.ConditionType;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.enums.EffectType;
import com.xyl.mmall.promotion.enums.RedPacketOrderType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.Promotion;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.meta.UserRedPacket;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.meta.tcc.RedPacketOrderTCC;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.FavorCaculateService;
import com.xyl.mmall.promotion.service.PromotionService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.service.judgement.impl.JudgementEngine;
import com.xyl.mmall.promotion.utils.ActivationUtils;
import com.xyl.mmall.promotion.utils.CartTipUtils;

/**
 * FavorCaculateServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@Service("favorCaculateService")
public class FavorCaculateServiceImpl implements FavorCaculateService {

	@Autowired
	private CouponService couponService;

	@Autowired
	private UserRedPacketService userRedPacketService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private JudgementEngine judgementEngine;
	
	@Value("${mainsite.host}")
	private String host;

	/**
	 * 计算入口 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.promotion.service.FavorCaculateService#caculateActivation(java.util.Map,
	 *      com.xyl.mmall.promotion.dto.FavorCaculateParamDTO,
	 *      com.xyl.mmall.promotion.dto.FavorCaculateResultDTO)
	 */
	@Override
	public FavorCaculateResultDTO caculateActivation(Map<Long, List<PromotionSkuItemDTO>> skuPoMap,
			FavorCaculateParamDTO paramDTO) {
		FavorCaculateResultDTO resultDTO = new FavorCaculateResultDTO();
		// 1.读取促销的参数
		if (paramDTO != null) {
			// 2.计算活动
			determineAndcaculate(skuPoMap, paramDTO, resultDTO, ActivationConstants.ACTIVATION_TYPE_ORDER);
			// 活动不满足的话，重置最终零售价
			caculateResultTotalPrice(resultDTO, true);
			
			caculateCoupon(skuPoMap, paramDTO, resultDTO);

			// 可用的红包金额
			BigDecimal canUseRedPackets = userRedPacketService.getTotalCash(paramDTO.getUserId(), new PromotionLock(
					paramDTO.getUserId()));
			resultDTO.setCanUseRedPackets(canUseRedPackets);

		}
		return resultDTO;
	}

	@Override
	public RedPacketParam caculateRedPackets(RedPacketParam redPacketParam) {
		// 可用的红包金额
		BigDecimal canUseRedPackets = userRedPacketService.getTotalCash(redPacketParam.getUserId(), new PromotionLock(redPacketParam.getUserId()));
		// 判断可用红包是否满足
		if (canUseRedPackets != null && canUseRedPackets.compareTo(redPacketParam.getUsedRedPrice()) < 0) {
			redPacketParam.setRpState(ActivationConstants.STATE_NOT_MATCH);
			return redPacketParam;
		}
		
		List<UserRedPacketDTO> redPackets = userRedPacketService.getUserRedPacketList(redPacketParam.getUserId(),
				ActivationConstants.STATE_CAN_USE, Integer.MAX_VALUE, 0);
		// 用户没有红包
		if (CollectionUtils.isEmpty(redPackets)) {
			//红包不存在
			redPacketParam.setRpState(ActivationConstants.STATE_NOT_EXISTS);
			return redPacketParam;
		}
		
		Collections.sort(redPackets);
		
		// 计算红包
		caculateRedPacketsDetail(redPacketParam, redPackets);
		return redPacketParam;
	}
	
    // 新增计算优惠券，by: durianskh@gmail.com
    public void caculateMmallCoupon(FavorCaculateParamDTO paramDTO, FavorCaculateResultDTO resultDTO)
    {
        if (!paramDTO.isCaculateCoupon())
        {
            return;
        }
        
        // 设置用户选中的优惠券
        CouponDTO couponDTO = userCouponService.getUserCouponDTO(paramDTO.getUserId(),
                paramDTO.getUserCouponId(), ActivationConstants.STATE_CAN_USE, true);
        if (couponDTO != null)
        {
            resultDTO.setCouponDTO(couponDTO);
        }
        
        // 计算优惠券
        CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
        couponOrderTCC.setCouponCode(couponDTO.getCouponCode());
        couponOrderTCC.setCouponHandlerType(ActivationHandlerType.DEFAULT);
        couponOrderTCC.setCouponOrderType(CouponOrderType.USE_COUPON);
        couponOrderTCC.setUserId(paramDTO.getUserId());
        int index = 0;
        Coupon coupon = couponService.getCouponByCode(couponDTO.getCouponCode(), true);
        List<Activation> activations = ActivationUtils.containActivations(coupon.getItems());
        boolean state = false;
        BigDecimal grossPrice = new BigDecimal(0);
        for (PromotionSkuItemDTO pDTO : resultDTO.getSkuList())
        {
            grossPrice = grossPrice.add(pDTO.getOriRetailPrice().multiply(
                    new BigDecimal(pDTO.getCount())));
        }
        for (Activation activation : activations)
        {
            Condition condition = activation.getCondition();
            state = judgementEngine.judge(condition, grossPrice);
            if (state)
            {
                break;
            }
            index++;
        }
        couponOrderTCC.setCouponIdx(index);
        couponOrderTCC.setUserCouponId(couponDTO.getUserCouponId());
        // 其他字段在下单时从order对象获取
        resultDTO.getCouponOrderTCCList().add(couponOrderTCC);
        
        
    }
	
	private void caculateCoupon(Map<Long, List<PromotionSkuItemDTO>> skuPoMap, FavorCaculateParamDTO paramDTO,
			FavorCaculateResultDTO resultDTO) {
		if (!paramDTO.isCaculateCoupon()) {
			return;
		}

		// 判断有效的优惠券
		determineCoupons(paramDTO, resultDTO);
		if (!paramDTO.isRefund()) {
			if (paramDTO.getUserCouponId() <= 0) {
				return;
			}

			// 传入优惠券，判断优惠券
			UserCoupon uc = userCouponService.getUserCouponById(paramDTO.getUserId(), paramDTO.getUserCouponId());
			if (uc == null) {
				resultDTO.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
				return;
			}

			// 失效的处理
			if (!uc.isValid()) {
				if (uc.getValidStartTime() > System.currentTimeMillis()) {
					resultDTO.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
				} else {
					resultDTO.setCouponState(ActivationConstants.STATE_EXPIRED);
				}
				return;
			}

			// 不能使用的情况
			if (uc.getState() != ActivationConstants.STATE_CAN_USE) {
				resultDTO.setCouponState(uc.getState());
				return;
			}

			Coupon coupon = couponService.getCouponByCode(uc.getCouponCode(), false);

			// 优惠券不存在
			if (coupon == null) {
				resultDTO.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
				return;
			}

			// 将此优惠券添加到结果中，做下一步判断
			CouponDTO couponDTO = new CouponDTO(coupon);
			couponDTO.setUserCouponId(uc.getId());
			resultDTO.setCouponDTO(couponDTO);
		}
		// 计算优惠券
		determineAndcaculate(skuPoMap, paramDTO, resultDTO, ActivationConstants.ACTIVATION_TYPE_COUPON);
		// 优惠券不重置最终零售价
		caculateResultTotalPrice(resultDTO, false);
	}

	/**
	 * 确定活动或者优惠券并计算
	 * 
	 * @param skuPoMap
	 * @param paramDTO
	 * @param resultDTO
	 */
	private void determineAndcaculate(Map<Long, List<PromotionSkuItemDTO>> skuPoMap, FavorCaculateParamDTO paramDTO,
			FavorCaculateResultDTO resultDTO, int activationType) {
		// 先判断po
		if (groupPoSku(skuPoMap, paramDTO, resultDTO, activationType)) {
			// 计算活动
			caculateActivation(skuPoMap, paramDTO, resultDTO, activationType);
		}
	}

	/**
	 * 计算总价
	 * 
	 * @param resultDTO
	 * @param reset
	 *            是否重置不满足条件的最终零售价
	 */
	private void caculateResultTotalPrice(FavorCaculateResultDTO resultDTO, boolean reset) {
		List<PromotionCartDTO> cartDTOs = resultDTO.getActivations();
		if (!CollectionUtils.isEmpty(cartDTOs)) {
			for (PromotionCartDTO cartDTO : cartDTOs) {
				// 满足条件的不重置
				addTotalPrice(resultDTO, cartDTO.getPoSkuMap(), false);
			}
		}

		// 将不满足条件添加到总价中
		if (CollectionUtils.isEmpty(resultDTO.getNotSatisfySkuList())) {
			return;
		}

		Map<Long, List<PromotionSkuItemDTO>> notSatisfyMap = resultDTO.getNotSatisfySkuList();

		addTotalPrice(resultDTO, notSatisfyMap, reset);
	}

	private void addTotalPrice(FavorCaculateResultDTO resultDTO, Map<Long, List<PromotionSkuItemDTO>> map, boolean reset) {
		if (CollectionUtils.isEmpty(map)) {
			return;
		}

		for (Entry<Long, List<PromotionSkuItemDTO>> entry : map.entrySet()) {
			for (PromotionSkuItemDTO dto : entry.getValue()) {
				if (reset) {
					dto.setRetailPrice(dto.getOriRetailPrice());
					dto.setCartPrice(dto.getOriRetailPrice());
				}
				resultDTO.setTotalRetailPrice(resultDTO.getTotalRetailPrice().add(
						dto.getRetailPrice().multiply(new BigDecimal(dto.getCount()))));
			}
		}
	}

	/**
	 * 绑定优惠券 (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.promotion.service.FavorCaculateService#bindCoupon(java.util.Map,
	 *      com.xyl.mmall.promotion.dto.FavorCaculateParamDTO)
	 */
	@Override
	@Transaction
	public FavorCaculateResultDTO bindCoupon(Map<Long, List<PromotionSkuItemDTO>> skuPoMap,
			FavorCaculateParamDTO paramDTO) {
		FavorCaculateResultDTO resultDTO = new FavorCaculateResultDTO();
		if (StringUtils.isNotBlank(paramDTO.getCouponCode())) {
			binderCouponDetail(skuPoMap, paramDTO, resultDTO);
		}
		return resultDTO;
	}

	/**
	 * 优惠券确认 ,获取用户可用的优惠券列表
	 * 
	 * @param skuItems
	 * @param paramDTO
	 * @param resultDTO
	 */
	private FavorCaculateResultDTO determineCoupons(FavorCaculateParamDTO paramDTO, FavorCaculateResultDTO resultDTO) {
		long userId = paramDTO.getUserId();
		// 获取用户的优惠券列表
		List<CouponDTO> coupons = userCouponService.getUserCouponListByState(userId, ActivationConstants.STATE_CAN_USE,
				Integer.MAX_VALUE, 0);
		if (CollectionUtils.isEmpty(coupons)) {
			return resultDTO;
		}

		for (CouponDTO uc : coupons) {
			if (!judgeCouponCondition(uc, resultDTO)) {
				continue;
			}
			resultDTO.getUserCoupons().add(uc);
		}

		return resultDTO;
	}
	
	@Override
	@Transaction
	public FavorCaculateResultDTO bindCoupon(String couponCode, long userId, String userName) {
		FavorCaculateResultDTO result = new FavorCaculateResultDTO();
		Coupon coupon = couponService.getCouponByCode(couponCode, false);
		// 优惠券不存在
		if (coupon == null || coupon.getAuditState() != StateConstants.PASS) {
			result.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
			return result;
		}
		
		// 优惠券已经过期
		if (!coupon.isValid()) {
			if (coupon.getStartTime() > System.currentTimeMillis()) {
				result.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
			} else {
				result.setCouponState(ActivationConstants.STATE_EXPIRED);
			}
			return result;
		}
		
		// 该优惠券已经绑定到其他用户下
		if (coupon.getBinderType() != BinderType.SYSTEM_BINDER) {
			couponService.getObjectLockByCode(couponCode);
			if (Arrays.asList(coupon.getUsers().split(",")).contains(userName)) {
				// 非系统绑定，判断是否被其他用户绑定
				int count = userCouponService.getUserCouponCountByCode(userId, coupon.getCouponCode());
				if (count > 0) {
					result.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
					return result;
				}
			} else {
				result.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
				return result;
			}
		} else {
			// 每个券没人可以使用的次数
			if (coupon.getCodeType() == CodeType.PUBLIC) {
				// 获取绑定的次数
				int count = userCouponService
						.getUserCouponCountByCode(userId, coupon.getCouponCode());
				if (count >= coupon.getTimes()) {
					result.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
					return result;
				}
			} else {
				couponService.getObjectLockByCode(couponCode);
				int count = userCouponService.getUserCouponCountByCode(coupon.getCouponCode());
				if (count >= coupon.getTimes()) {
					result.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
					return result;
				}
			}
		}
		
		// 将此优惠券添加到结果中，做下一步判断
		CouponDTO couponDTO = new CouponDTO(coupon);
		result.setCouponDTO(couponDTO);

		// 绑定优惠券
		if (coupon != null) {
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setCouponCode(coupon.getCouponCode());
			userCoupon.setUserId(userId);
			userCoupon.setValidStartTime(coupon.getStartTime());
			userCoupon.setValidEndTime(coupon.getEndTime());

			// 满足条件和用户绑定
			if (judgeCouponCondition(new CouponDTO(coupon))) {
				userCoupon = userCouponService.addUserCoupon(userCoupon);
				result.getCouponDTO().setUserCouponId(userCoupon.getId());
			} else {
				// 不满足条件提示不匹配
				result.setCouponState(ActivationConstants.STATE_NOT_MATCH);
				return result;
			}
		}
		return result;
	}

	private boolean judgeCouponCondition(CouponDTO couponDTO) {
		String contents = couponDTO.getItems();
		List<Activation> activations = ActivationUtils.containActivations(contents);

		if (CollectionUtils.isEmpty(activations)) {
			return false;
		}


		return true;
	}
	
	@Transaction
	private boolean binderCouponDetail(Map<Long, List<PromotionSkuItemDTO>> skuPoMap, FavorCaculateParamDTO paramDTO,
			FavorCaculateResultDTO resultDTO) {
		if (CollectionUtils.isEmpty(skuPoMap)) {
			return false;
		}
		Coupon coupon = null;
		// 判断手动输入的优惠券
		if (StringUtils.isNotBlank(paramDTO.getCouponCode())) {
			coupon = couponService.getCouponByCode(paramDTO.getCouponCode(), false);

			// 优惠券不存在
			if (coupon == null || coupon.getAuditState() != StateConstants.PASS) {
				resultDTO.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
				return false;
			}
			
			// 优惠券已经过期
			if (!coupon.isValid()) {
				if (coupon.getStartTime() > System.currentTimeMillis()) {
					resultDTO.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
				} else {
					resultDTO.setCouponState(ActivationConstants.STATE_EXPIRED);
				}
				return false;
			}

			// 该优惠券已经绑定到其他用户下
			if (coupon.getBinderType() != BinderType.SYSTEM_BINDER) {
				couponService.getObjectLockByCode(paramDTO.getCouponCode());
				// 非系统绑定，判断是否被其他用户绑定
				int count = userCouponService.getUserCouponCountByCode(coupon.getCouponCode());
				if (count > 0) {
					resultDTO.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
					return false;
				}
			} else {
				// 每个券没人可以使用的次数
				if (coupon.getCodeType() == CodeType.PUBLIC) {
					// 获取绑定的次数
					int count = userCouponService
							.getUserCouponCountByCode(paramDTO.getUserId(), coupon.getCouponCode());
					if (count >= coupon.getTimes()) {
						resultDTO.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
						return false;
					}
				} else {
					couponService.getObjectLockByCode(paramDTO.getCouponCode());
					int count = userCouponService.getUserCouponCountByCode(coupon.getCouponCode());
					if (count >= coupon.getTimes()) {
						resultDTO.setCouponState(ActivationConstants.STATE_HAS_BIND_OTHERS);
						return false;
					}
				}
			}

			// 将此优惠券添加到结果中，做下一步判断
			CouponDTO couponDTO = new CouponDTO(coupon);
			resultDTO.setCouponDTO(couponDTO);
		}

		// 分组处理，优惠券关联po时打开
		if (!groupPoSku(skuPoMap, paramDTO, resultDTO, ActivationConstants.ACTIVATION_TYPE_COUPON)) {
			resultDTO.setCouponState(ActivationConstants.STATE_NOT_MATCH);
			return false;
		}

		long userId = paramDTO.getUserId();

		// 绑定优惠券
		if (coupon != null) {
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setCouponCode(coupon.getCouponCode());
			userCoupon.setUserId(userId);
			userCoupon.setValidStartTime(coupon.getStartTime());
			userCoupon.setValidEndTime(coupon.getEndTime());

			// 满足条件和用户绑定
			if (judgeCouponCondition(new CouponDTO(coupon), resultDTO)) {
				userCoupon = userCouponService.addUserCoupon(userCoupon);
				resultDTO.getCouponDTO().setUserCouponId(userCoupon.getId());
			} else {
				// 不满足条件提示不匹配
				resultDTO.setCouponState(ActivationConstants.STATE_NOT_MATCH);
				return false;
			}
		}
		return true;
	}

	/**
	 * 处理优惠券条件
	 * 
	 * @param coupon
	 * @param uc
	 * @param resultDTO
	 * @return
	 */
	private boolean judgeCouponCondition(CouponDTO couponDTO, FavorCaculateResultDTO resultDTO) {
		if (resultDTO == null) {
			return false;
		}

		List<PromotionSkuItemDTO> dtos = new ArrayList<>();

		List<PromotionCartDTO> list = resultDTO.getActivations();
		// 没有优惠的活动
		if (!CollectionUtils.isEmpty(list)) {
			for (PromotionCartDTO dto : list) {
				for (Entry<Long, List<PromotionSkuItemDTO>> entry : dto.getPoSkuMap().entrySet()) {
					dtos.addAll(entry.getValue());
				}
			}
		}

		if (!CollectionUtils.isEmpty(resultDTO.getNotSatisfySkuList())) {
			for (Entry<Long, List<PromotionSkuItemDTO>> entry : resultDTO.getNotSatisfySkuList().entrySet()) {
				dtos.addAll(entry.getValue());
			}
		}

		String contents = couponDTO.getItems();
		List<Activation> activations = ActivationUtils.containActivations(contents);

		if (CollectionUtils.isEmpty(activations)) {
			return false;
		}

		boolean state = false;

		for (Activation activation : activations) {
			Condition condition = activation.getCondition();
			state = judgeActivationCondition(dtos, condition);
			if (state) {
				break;
			}
		}

		return state;
	}

	/**
	 * 判断activation中condition
	 * 
	 * @param uc
	 * @param resultDTO
	 * @param list
	 * @param condition
	 */
	private boolean judgeActivationCondition(List<PromotionSkuItemDTO> list, Condition condition) {
		return judgePoMapCondition(condition, list);
	}

	/**
	 * 判断每一个po列表的条件
	 * 
	 * @param uc
	 * @param resultDTO
	 * @param condition
	 * @param dto
	 */
	private boolean judgePoMapCondition(Condition condition, List<PromotionSkuItemDTO> list) {
		return judgeConditions(condition, list);
	}

	/**
	 * 对参与活动的po进行分组
	 * 
	 * @param skuItems
	 * @param paramDTO
	 * @param resultDTO
	 */
	private boolean groupPoSku(Map<Long, List<PromotionSkuItemDTO>> skuItemMap, FavorCaculateParamDTO paramDTO,
			FavorCaculateResultDTO resultDTO, int activationType) {
		List<PromotionCartDTO> cartDTOs = resultDTO.getActivations();
		if (CollectionUtils.isEmpty(cartDTOs)) {
			cartDTOs = new ArrayList<>();
		}
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			// key:po value:promotion
			Map<Long, PromotionDTO> poPromotionMap = paramDTO.getPoPromotionMap();
			// 参数中传入了指定的po : 活动映射
			if (!CollectionUtil.isEmptyOfMap(poPromotionMap)) {
				//将skuitemmap中不包含的活动移到不满足条件map中
				for (Entry<Long, List<PromotionSkuItemDTO>>  entry : skuItemMap.entrySet()) {
					if (!poPromotionMap.containsKey(entry.getKey())) {
						resultDTO.getNotSatisfySkuList().put(entry.getKey(), entry.getValue());
						skuItemMap.remove(entry.getKey());
					}
				}
				
				if (CollectionUtils.isEmpty(skuItemMap)) {
					return false;
				}
				
				for (Entry<Long, PromotionDTO> entry : poPromotionMap.entrySet()) {
					PromotionDTO promotionDTO = entry.getValue();
					Promotion promotion = promotionService.getPromotionById(promotionDTO.getId());
					if (promotion == null) {
						continue;
					}

					PromotionDTO dto = entry.getValue();

					if (skuItemMap.containsKey(entry.getKey())) {
						handlePoCart(cartDTOs, skuItemMap, dto, null, activationType);
					} else {
						
					}
				}
			} else {
				//退款 购买时没有使用优惠 不处理
				if (paramDTO.isRefund()) {
					resultDTO.setNotSatisfySkuList(skuItemMap);
					return false;
				}
				// 根据站点获取对应的活动列表
				List<Promotion> promotions = promotionService.getDeclarePromotionList(paramDTO.getAreaPermission(), paramDTO.getPlatformType());
				// 如果指定站点活动为空，不做处理
				if (CollectionUtils.isEmpty(promotions)) {
					resultDTO.setNotSatisfySkuList(skuItemMap);
					return false;
				}
				// 预处理，获取活动po的skuPoMap
				presetSatisfyPoMapFromActivation(cartDTOs, skuItemMap, paramDTO, promotions, activationType);
			}
		} else if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			Map<Long, CouponDTO> poCouponMap = paramDTO.getPoCouponMap();
			// 参数中传入了指定的po：优惠券映射（均指和此用户绑定的优惠券）
			if (!CollectionUtil.isEmptyOfMap(poCouponMap)) {
				for (Entry<Long, CouponDTO> entry : poCouponMap.entrySet()) {
					// if (!skuItemMap.containsKey(entry.getKey())) {
					// continue;
					// }

					CouponDTO couponDTO = entry.getValue();
					if (couponDTO == null) {
						continue;
					}

					UserCoupon uc = userCouponService.getUserCouponById(paramDTO.getUserId(),
							couponDTO.getUserCouponId());
					if (uc == null) {
						resultDTO.setCouponState(ActivationConstants.STATE_NOT_EXISTS);
						return false;
					}

					if (!uc.isValid() && !paramDTO.isRefund()) {
						if (uc.getValidStartTime() > System.currentTimeMillis()) {
							resultDTO.setCouponState(ActivationConstants.STATE_NOT_TAKE_EFFECT);
						} else {
							resultDTO.setCouponState(ActivationConstants.STATE_EXPIRED);
						}
						return false;
					}

					Coupon c = couponService.getCouponByCode(uc.getCouponCode(), false);
					if (c == null) {
						continue;
					}

					resultDTO.setCouponDTO(couponDTO);
					resultDTO.setIndex(couponDTO.getIndex());

					handlePoCart(cartDTOs, skuItemMap, null, couponDTO, activationType);
				}
			} else {
				//退款 购买时没有使用优惠券 不处理
				if (paramDTO.isRefund()) {
					return false;
				}
				if (resultDTO.getCouponDTO() != null) {
					handlePoCart(cartDTOs, skuItemMap, null, resultDTO.getCouponDTO(), activationType);
				}
			}
		}

		// 设置到resultdto中
		resultDTO.setActivations(cartDTOs);
		resultDTO.getNotSatisfySkuList().putAll(skuItemMap);
		// 没有找到符合条件的po，优惠券暂时不处理po
		if (CollectionUtils.isEmpty(cartDTOs) && activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			return false;
		}
		return true;
	}

	/**
	 * 预先设置对满足活动的po进行分组
	 * 
	 * @param cartDTOs
	 * @param sourceMap
	 * @param promotions
	 */
	private void presetSatisfyPoMapFromActivation(List<PromotionCartDTO> cartDTOs,
			Map<Long, List<PromotionSkuItemDTO>> sourceMap, FavorCaculateParamDTO paramDTO, List<Promotion> promotions,
			int activationType) {
		// 对每个活动进行判断
		for (Promotion promotion : promotions) {
			if (promotion == null) {
				continue;
			}

			handlePoCart(cartDTOs, sourceMap, new PromotionDTO(promotion), null, activationType);
		}
	}

	/**
	 * 处理po cart
	 * 
	 * @param cartDTOs
	 * @param sourceMap
	 * @param promotion
	 */
	private void handlePoCart(List<PromotionCartDTO> cartDTOs, Map<Long, List<PromotionSkuItemDTO>> sourceMap,
			PromotionDTO promotionDTO, CouponDTO couponDTO, int activationType) {
		if (CollectionUtils.isEmpty(sourceMap)) {
			return;
		}
		
		PromotionCartDTO cartDTO = new PromotionCartDTO();
		// 没有传入优惠券
		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON && couponDTO == null) {
			return;
		}

		// 没有传入活动
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER && promotionDTO == null) {
			return;
		}

		String items = null;
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			items = promotionDTO.getItems();
		}

		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			items = couponDTO.getItems();
		}

		List<Activation> activations = ActivationUtils.containActivations(items);
		// 不包含促销信息
		if (CollectionUtils.isEmpty(activations)) {
			return;
		}

		for (Activation activation : activations) {
			mappingPoSku(sourceMap, promotionDTO, couponDTO, cartDTO, activation, activationType);
		}

		if (cartDTO != null && cartDTO.getPoSkuMap() != null && cartDTO.getPoSkuMap().size() > 0) {
			cartDTOs.add(cartDTO);
		}
	}

	/**
	 * cart和活动po建立关系
	 * 
	 * @param sourceMap
	 * @param promotion
	 * @param cartDTO
	 * @param activation
	 * @return
	 */
	private void mappingPoSku(Map<Long, List<PromotionSkuItemDTO>> sourceMap, PromotionDTO promotionDTO,
			CouponDTO couponDTO, PromotionCartDTO cartDTO, Activation activation, int activationType) {
		List<ActivitySchedule> schedules = activation.getSchedules();
		// 优惠券特殊处理,设置所有的均有效
		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			// 设置兼容以后优惠券关联po
			// cartDTO.setCouponDTO(couponDTO);
			return;
		}

		// 没有指定档期退出
		if (CollectionUtils.isEmpty(schedules)
				&& !ActivationConstants.OVERALL.equalsIgnoreCase(promotionDTO.getDeclarePO())) {
			return;
		}

		// 遍历，判断po指定的档期在购物车是否存在，并分组
		for (ActivitySchedule schedule : schedules) {
			// 优惠券暂不做po处理,不是全部且不包含指定档期的退出
			if (!sourceMap.containsKey(schedule.getId())
					&& !ActivationConstants.OVERALL.equalsIgnoreCase(promotionDTO.getDeclarePO())) {
				continue;
			}
			if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
				cartDTO.setPromotionDTO(promotionDTO);
			} else if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
				cartDTO.setCouponDTO(couponDTO);

			}
			cartDTO.getPoSkuMap().put(schedule.getId(), sourceMap.get(schedule.getId()));
			// 从原始map中删除
			sourceMap.remove(schedule.getId());
		}
	}

	/**
	 * 判断条件
	 * 
	 * @param conditionDTO
	 * @param skuItems
	 */
	private boolean judgeConditions(Condition condition, List<PromotionSkuItemDTO> skuItems) {
		if (CollectionUtils.isEmpty(skuItems)) {
			return false;
		}

		boolean isPass = false;

		Object base = null;

		// 没有限定任何条件
		if (condition == null) {
			return true;
		}

		// 条件判断
		if (condition.getType() == ConditionType.BASIC_PRICE_SATISFY.getIntValue()) {
			// 订单价格满
			for (PromotionSkuItemDTO dto : skuItems) {
				if (base == null) {
					base = dto.getRetailPrice().multiply(new BigDecimal(dto.getCount()));
				} else {
					base = ((BigDecimal) base).add(dto.getRetailPrice().multiply(new BigDecimal(dto.getCount())));
				}
			}

		} else if (condition.getType() == ConditionType.BASIC_COUNT_SATISFY.getIntValue()) {
			// 订单商品数满
			for (PromotionSkuItemDTO dto : skuItems) {
				if (base == null) {
					base = dto.getCount();
				} else {
					base = (int) base + dto.getCount();
				}
			}
		}

		isPass = judgementEngine.judge(condition, base);
		// 如果任一条件不满足 退出
		if (!isPass) {
			return false;
		}

		return isPass;
	}
	
    public Boolean judgeCouponCondition(Condition condition, Object base)
    {
        return judgementEngine.judge(condition, base);
    }

	/**
	 * 计算
	 * 
	 * @param skuItems
	 * @param paramDTO
	 * @param resultDTO
	 * @param activationType
	 */
	private void caculateActivation(Map<Long, List<PromotionSkuItemDTO>> skuItems, FavorCaculateParamDTO paramDTO,
			FavorCaculateResultDTO resultDTO, int activationType) {
		if (resultDTO == null) {
			return;
		}

		List<PromotionCartDTO> list = resultDTO.getActivations();
		// 没有优惠的活动
		if (CollectionUtils.isEmpty(list) && activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			return;
		}

		List<PromotionSkuItemDTO> itemList = new ArrayList<>();
		// 如果是优惠券，将不满足条件的加入到itemlist计算，优惠券关联po时去掉此判断
		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			itemList.addAll(mergeMapValues(resultDTO.getNotSatisfySkuList()));
		}

		// 遍历计算活动效果
		Iterator<PromotionCartDTO> iterator = list.iterator();
		while (iterator.hasNext()) {
			PromotionCartDTO dto = iterator.next();
			if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
				if (caculatePromotionCartDTO(resultDTO, null, dto, activationType)) {
					continue;
				}

				// 添加到不满足条件列表
				// resultDTO.getNotSatisfySkuList().putAll(dto.getPoSkuMap());
				// 清除满足条件列表
				// iterator.remove();

			} else {
				itemList.addAll(mergeMapValues(dto.getPoSkuMap()));
			}
		}

		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			if (caculatePromotionCartDTO(resultDTO, itemList, null, activationType)) {
				return;
			}
			resultDTO.setCouponDTO(null);
			// // 添加到不满足条件列表
			// resultDTO.getNotSatisfySkuList().putAll(skuItems);
			// // 清除满足条件列表
			// list.clear();
			// resultDTO.setCouponDTO(null);

		}
	}

	/**
	 * 计算每一个分组的dto
	 * 
	 * @param resultDTO
	 * @param dto
	 */
	private boolean caculatePromotionCartDTO(FavorCaculateResultDTO resultDTO, List<PromotionSkuItemDTO> cacItems,
			PromotionCartDTO dto, int activationType) {
		PromotionDTO promotionDTO = dto != null ? dto.getPromotionDTO() : null;
		CouponDTO couponDTO = dto == null ? resultDTO.getCouponDTO() : null;
		Map<Long, List<PromotionSkuItemDTO>> map = dto != null ? dto.getPoSkuMap() : null;
		String stepTip = null;

		// 是否指定活动效果
		List<Activation> activations = hasActivation(promotionDTO, couponDTO, activationType);
		if (CollectionUtils.isEmpty(activations)) {
			return false;
		}

		if (CollectionUtils.isEmpty(cacItems)) {
			cacItems = mergeMapValues(map);
		}

		boolean satisfy = false;
		int index = 0;

		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON && couponDTO.getIndex() > -1) {
			index = couponDTO.getIndex();
			Activation activation = activations.get(index);
			satisfy = judgeConditions(activation.getCondition(), cacItems);

		} else if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER && promotionDTO.getIndex() > -1) {
			index = promotionDTO.getIndex();
			Activation activation = activations.get(index);
			satisfy = judgeConditions(activation.getCondition(), cacItems);
		} else {
			for (Activation activation : activations) {
				satisfy = judgeConditions(activation.getCondition(), cacItems);
				// 满足条件
				if (satisfy) {
					break;
				}
				index++;
			}
		}

		// 不满足条件不计算
		if (!satisfy) {
			stepTip = buildStepTip(activations.get(0), cacItems);
			if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
				dto.setStepTip(stepTip);
			}

			if (promotionDTO != null) {
				if (!ActivationConstants.OVERALL.equals(promotionDTO.getDeclarePO())
						&& promotionDTO.getDeclarePO().indexOf(",") == -1) {
					dto.setCoudanUrl(host + "/schedule?scheduleId=" + promotionDTO.getDeclarePO());
				} else {
					dto.setCoudanUrl(host);
				}
			}

			return false;
		} else {
			if (index + 1 < activations.size()) {
				stepTip = buildStepTip(activations.get(index + 1), cacItems);
			}
		}

		// 设置享用的第几个优惠
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			dto.setIndex(index);
			// 设置下一个阶梯提示
			dto.setStepTip(stepTip);
		} else {
			// 设置优惠券享用的优惠效果索引
			resultDTO.setIndex(index);
		}

		if (StringUtils.isNotBlank(stepTip) && promotionDTO != null) {
			if (!ActivationConstants.OVERALL.equals(promotionDTO.getDeclarePO())
					&& promotionDTO.getDeclarePO().indexOf(",") == -1) {
				dto.setCoudanUrl(host + "/schedule?scheduleId=" + promotionDTO.getDeclarePO());
			} else {
				dto.setCoudanUrl(host);
			}
		}

		detailCaculate(activations, cacItems, resultDTO, activationType);
		// 添加couponordertcc和usercoupontcc
		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
			couponOrderTCC.setCouponCode(couponDTO.getCouponCode());
			couponOrderTCC.setCouponHandlerType(ActivationHandlerType.DEFAULT);
			couponOrderTCC.setCouponOrderType(CouponOrderType.USE_COUPON);
			couponOrderTCC.setUserId(cacItems.get(0).getUserId());
			couponOrderTCC.setCouponIdx(index);
			couponOrderTCC.setUserCouponId(couponDTO.getUserCouponId());
			// 其他字段在下单时从order对象获取
			resultDTO.getCouponOrderTCCList().add(couponOrderTCC);
		}
		return true;
	}

	/**
	 * 构建阶梯提示
	 * 
	 * @param activation
	 */
	private String buildStepTip(Activation activation, List<PromotionSkuItemDTO> list) {
		BigDecimal price = BigDecimal.ZERO;
		int count = 0;

		Condition condition = activation.getCondition();

		// 获取已购商品的总价或总件数
		for (PromotionSkuItemDTO dto : list) {
			if (condition.getType() == ConditionType.BASIC_PRICE_SATISFY.getIntValue()) {
				price = price.add(dto.getOriRetailPrice().multiply(new BigDecimal(dto.getCount())));
			} else if (condition.getType() == ConditionType.BASIC_COUNT_SATISFY.getIntValue()) {
				count += dto.getCount();
			}
		}

		String value = CartTipUtils.getFirstValue(condition.getValue());

		BigDecimal nextStepLackPrice = new BigDecimal(value).subtract(price);
		int nextStepLackCount = Integer.valueOf(value) - count;

		return CartTipUtils.buildTipFromCondition(activation, nextStepLackPrice, nextStepLackCount, true, "、");
	}

	private List<PromotionSkuItemDTO> mergeMapValues(Map<Long, List<PromotionSkuItemDTO>> map) {
		List<PromotionSkuItemDTO> cacItems = new ArrayList<>();
		if (CollectionUtils.isEmpty(map)) {
			return cacItems;
		}
		
		for (Entry<Long, List<PromotionSkuItemDTO>> entry : map.entrySet()) {
			cacItems.addAll(entry.getValue());
		}
		return cacItems;
	}

	/**
	 * 是否包含活动
	 * 
	 * @param promotionDTO
	 * @param map
	 * @return
	 */
	private List<Activation> hasActivation(PromotionDTO promotionDTO, CouponDTO couponDTO, int activationType) {
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER && (promotionDTO == null)) {
			return null;
		}

		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON && (couponDTO == null)) {
			return null;
		}

		String items = null;
		if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
			items = promotionDTO.getItems();
		}

		if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
			items = couponDTO.getItems();
		}

		// 活动是否指定效果
		List<Activation> activations = ActivationUtils.containActivations(items);
		if (CollectionUtils.isEmpty(activations)) {
			return null;
		}
		
		return activations;
	}

	/**
	 * 根据活动详细计算
	 * 
	 * @param activations
	 * @param value
	 */
	private void detailCaculate(List<Activation> activations, List<PromotionSkuItemDTO> list,
			FavorCaculateResultDTO resultDTO, int activationType) {

		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		for (Activation activation : activations) {
			// 不满足条件，继续
			if (!judgeConditions(activation.getCondition(), list)) {
				continue;
			}
			List<Effect> effects = activation.getResult();
			if (CollectionUtils.isEmpty(effects)) {
				continue;
			}

			caculateEffects(effects, list, resultDTO, activationType);
			break;
		}
	}

	/**
	 * 根据设置的效果进行计算
	 * 
	 * @param effects
	 * @param list
	 * @param resultDTO
	 * @param activationType
	 */
	private void caculateEffects(List<Effect> effects, List<PromotionSkuItemDTO> list,
			FavorCaculateResultDTO resultDTO, int activationType) {

		for (Effect effect : effects) {
			if (effect.getType() == EffectType.ACTIVATION_DISCOUNT.getIntValue()) {
				// 订单打折
				for (PromotionSkuItemDTO dto : list) {
					BigDecimal discountCash = BigDecimal.ZERO;
					// 对每个产品设定指定折扣价格
					// 设置购物车价格
					dto.setCartPrice(dto.getRetailPrice().multiply(new BigDecimal(effect.getValue()))
							.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
					// 计算优惠的金额
					discountCash = dto.getRetailPrice().subtract(dto.getCartPrice())
							.setScale(2, BigDecimal.ROUND_HALF_UP);
					// 设置最终零售价
					dto.setRetailPrice(dto.getRetailPrice().multiply(new BigDecimal(effect.getValue()))
							.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));

					if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
						dto.setCouponDiscountAmount(discountCash);
						resultDTO.setTotalCouponDiscount(resultDTO.getTotalCouponDiscount().add(discountCash.multiply(new BigDecimal(dto
								.getCount()))));
					} else if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
						dto.setPromotionDiscountAmount(discountCash);
						resultDTO.setTotalPromotionDiscount(resultDTO.getTotalPromotionDiscount().add(discountCash.multiply(new BigDecimal(dto
								.getCount()))));
					}
				}
			} else if (effect.getType() == EffectType.ACTIVATION_MINUS.getIntValue()) {
				// 订单减，均摊价格
				BigDecimal discountCash = new BigDecimal(effect.getValue());
				avgItemPrice(resultDTO, list, discountCash, activationType);

			} else if (effect.getType() == EffectType.ACTIVATION_EXPRESS_FREE.getIntValue()) {
				// 包邮
				if (!resultDTO.isFreeExpPrice()) {
					resultDTO.setFreeExpPrice(true);
				}
			} else if (effect.getType() == EffectType.ACTIVATION_COUPON_PRESENT.getIntValue()) {
				// 送优惠券
				CouponOrderTCC couponOrderTCC = new CouponOrderTCC();
				couponOrderTCC.setCouponCode(effect.getValue());
				couponOrderTCC.setCouponHandlerType(ActivationHandlerType.DEFAULT);
				couponOrderTCC.setCouponOrderType(CouponOrderType.RETURN_COUPON);
				// 其他字段在下单时从order对象获取
				resultDTO.getCouponOrderTCCList().add(couponOrderTCC);
			} else if (effect.getType() == EffectType.ACTIVATION_RED_PACKETS_PRESENT.getIntValue()) {
				// 活动返红包
				RedPacketOrderTCC redPacketOrderTCC = new RedPacketOrderTCC();
				redPacketOrderTCC.setRedPacketId(Long.valueOf(effect.getValue()));
				redPacketOrderTCC.setUserId(list.get(0).getUserId());
				redPacketOrderTCC.setRedPacketOrderType(RedPacketOrderType.RETURN_RED_PACKET);
				redPacketOrderTCC.setRedPacketHandlerType(ActivationHandlerType.DEFAULT);
				resultDTO.getRedPacketOrderTCCList().add(redPacketOrderTCC);
			}
		}
	}

	/**
	 * 价格平摊
	 * 
	 * @param list
	 * @param discountCash
	 * @param activationType
	 */
	private void avgItemPrice(FavorCaculateResultDTO resultDTO, List<? extends PromotionSkuItemDTO> list, BigDecimal discountCash, int activationType) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		// 按照count从大到小排列，避免最后一个计算存在精度误差
		Collections.sort(list);

		BigDecimal cartOriRPrice = BigDecimal.ZERO;
		for (PromotionSkuItemDTO skuItemDTO : list) {
			cartOriRPrice = cartOriRPrice.add(skuItemDTO.getRetailPrice().multiply(
					BigDecimal.valueOf(skuItemDTO.getCount())));
		}
		// 判断总零售价是否为0
		if (cartOriRPrice.compareTo(BigDecimal.ZERO) == 0)
			return;
		
		if (cartOriRPrice.compareTo(discountCash) <= 0) {
			discountCash = cartOriRPrice;
		}
		
		BigDecimal remainCash = discountCash;
		int index = 0;

		for (PromotionSkuItemDTO item : list) {
			// 均摊价格
			BigDecimal retail = item.getRetailPrice().subtract(
							item.getRetailPrice().multiply(discountCash).divide(cartOriRPrice, 2, RoundingMode.HALF_UP))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			retail = retail.compareTo(BigDecimal.ZERO) >= 0 ? retail : BigDecimal.ZERO;
			// 最后一项
			// 计算item优惠金额
			BigDecimal itemDiscountCash = item.getRetailPrice().subtract(retail).setScale(2, BigDecimal.ROUND_HALF_UP);
			// 设置剩余金额,最后一项等于金额不操作
			if (index < list.size() - 1) {
				remainCash = remainCash.subtract(itemDiscountCash.multiply(new BigDecimal(item.getCount())));
			}
			if (index == list.size() - 1) {
				// 剩余金额不等于均摊金额,重新设置金额
				if (remainCash.compareTo(itemDiscountCash.multiply(new BigDecimal(item.getCount()))) != 0) {
					retail = item.getRetailPrice().multiply(new BigDecimal(item.getCount())).subtract(remainCash)
							.divide(new BigDecimal(item.getCount()), 2, BigDecimal.ROUND_HALF_UP);
				}

				// item优惠金额大于剩余优惠金额，小于剩余金额，优惠金额为item优惠金额本身不做处理
				if (itemDiscountCash.compareTo(remainCash) != 0) {
					itemDiscountCash = remainCash.divide(new BigDecimal(item.getCount()), 2, BigDecimal.ROUND_HALF_UP);
				}
			}

			// 设置优惠金额
			if (activationType == ActivationConstants.ACTIVATION_TYPE_COUPON) {
				item.setCouponDiscountAmount(itemDiscountCash);
				resultDTO.setTotalCouponDiscount(resultDTO.getTotalCouponDiscount().add(itemDiscountCash.multiply(new BigDecimal(item
						.getCount()))));
			} else if (activationType == ActivationConstants.ACTIVATION_TYPE_ORDER) {
				item.setPromotionDiscountAmount(itemDiscountCash);
				resultDTO.setTotalPromotionDiscount(resultDTO.getTotalPromotionDiscount().add(itemDiscountCash.multiply(new BigDecimal(item
						.getCount()))));
			} else if (activationType == ActivationConstants.ACTIVATION_TYPE_REDPACKETS) {
				item.setHbPrice(itemDiscountCash);
			}

			retail = retail.compareTo(BigDecimal.ZERO) >= 0 ? retail : BigDecimal.ZERO;

			index++;

			item.setRetailPrice(retail);
			item.setCartPrice(item.getRetailPrice());
		}
	}

	/**
	 * 计算用户红包
	 * 
	 * @param userId
	 * @param cash
	 * @return
	 */
	public void caculateRedPacketsDetail(RedPacketParam redPacketParam, List<UserRedPacketDTO> redPackets) {
		BigDecimal cash = redPacketParam.getUsedRedPrice();
		//使用金额为0,退出
		if (cash.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		
		List<SkuParam> list = redPacketParam.getSkuParams();
		//实际支付总金额
		BigDecimal totalRetail = getTotalRetailPrice(list);
		BigDecimal diff = cash.subtract(totalRetail);
		//计算快递费用标记
		boolean calExp = false;
		// 红包金额大于实付金额，设置红包金额为实付金额。此时可将多出的红包部分用于抵扣运费
		if (diff.compareTo(BigDecimal.ZERO) > 0) {
			//差值
			//多余部分抵扣邮费
			calExp = true;
			if (diff.compareTo(redPacketParam.getExpressPrice()) <= 0) {
				redPacketParam.setExpressDiscountPrice(diff);
			} else {
				redPacketParam.setExpressDiscountPrice(redPacketParam.getExpressPrice());
			}
			cash = totalRetail;
			redPacketParam.setUsedRedPrice(cash);
		}
		
		redPacketParam.setRealUsedRedPrice(cash.add(redPacketParam.getExpressDiscountPrice()));
		
		long userId = redPacketParam.getUserId();

		BigDecimal userCash = BigDecimal.ZERO;
		// 即将扣除的红包列表
		List<UserRedPacket> takeOffPacket = new ArrayList<>();
		List<UserRedPacket> expressPacket = new ArrayList<>();
		//组合使用的红包
		UserRedPacketDTO composePacket = null;
		//临界值剩余的红包金额
		BigDecimal remainCash = BigDecimal.ZERO;
		
		//产品使用红包扣除
		Iterator<UserRedPacketDTO> iterator = redPackets.iterator();
		while (iterator.hasNext()) {
			UserRedPacketDTO dto = iterator.next();
			//copy一份
			UserRedPacket userRedPacket = dto.cloneObject();
			userCash = userCash.add(userRedPacket.getRemainCash());
			takeOffPacket.add(userRedPacket);
			// 获取的红包金额大于扣除的金额，退出不再获取
			if (userCash.compareTo(cash) >= 0) {
				if (userCash.compareTo(cash) > 0) {
					remainCash = userCash.subtract(cash);
					composePacket = new UserRedPacketDTO(userRedPacket);
				}
				//移动后将原列表记录删除
				iterator.remove();
				break;
			}
			iterator.remove();
		}
		BigDecimal expDiscount = redPacketParam.getExpressDiscountPrice();
		//扣除红包
		if (calExp) {
			if (composePacket != null) {
				expressPacket.add(composePacket);
			}
			
			//临界值剩下红包不够支付邮费
			if (remainCash.compareTo(expDiscount) < 0) {
				//扣除产品抵扣红包后，还有剩下的红包，用来扣除邮费
				while (iterator.hasNext()) {
					UserRedPacketDTO dto = iterator.next();
					//copy一份
					UserRedPacket userRedPacket = dto.cloneObject();
					remainCash = remainCash.add(userRedPacket.getRemainCash());
					expressPacket.add(userRedPacket);
					// 获取的红包金额大于扣除的金额，退出不再获取
					if (remainCash.compareTo(expDiscount) >= 0) {
						//移动后将原列表记录删除
						iterator.remove();
						break;
					}
					iterator.remove();
				}
			}
		}
		
		List<RedPacketOrderTCC> redPacketOrderTCCs = new ArrayList<>();
		
		BigDecimal thresholdRemainPrice = BigDecimal.ZERO;
		
		// 遍历红包，进行扣款
		for (UserRedPacket userRedPacket : takeOffPacket) {
			RedPacketOrderTCC redPacketOrderTCC = new RedPacketOrderTCC();
			cash = cash.subtract(userRedPacket.getRemainCash());
			// 红包金额不够，全额红包支付
			if (cash.compareTo(BigDecimal.ZERO) >= 0) {
				redPacketOrderTCC.setCash(userRedPacket.getRemainCash());
			} else {
				if (composePacket.getId() == userRedPacket.getId()) {
					thresholdRemainPrice = BigDecimal.ZERO.subtract(cash);
				}
				redPacketOrderTCC.setCash(cash.add(userRedPacket.getRemainCash()));
			}
			redPacketOrderTCC.setUserRedPacketId(userRedPacket.getId());
			redPacketOrderTCC.setUserId(userId);
			redPacketOrderTCC.setRedPacketOrderType(RedPacketOrderType.USE_RED_PACKET);
			redPacketOrderTCC.setRedPacketHandlerType(ActivationHandlerType.DEFAULT);

			redPacketOrderTCCs.add(redPacketOrderTCC);
		}
		
		if (!CollectionUtils.isEmpty(expressPacket)) {
			for (UserRedPacket userRedPacket : expressPacket) {
				RedPacketOrderTCC redPacketOrderTCC = new RedPacketOrderTCC();
				if (composePacket.getId() == userRedPacket.getId()) {
					expDiscount = expDiscount.subtract(thresholdRemainPrice);
					// 红包金额不够，全额红包支付
					if (expDiscount.compareTo(BigDecimal.ZERO) >= 0) {
						redPacketOrderTCC.setCash(thresholdRemainPrice);
					} else {
						redPacketOrderTCC.setCash(thresholdRemainPrice.add(expDiscount));
					}
				} else {
					expDiscount = expDiscount.subtract(userRedPacket.getRemainCash());
					// 红包金额不够，全额红包支付
					if (expDiscount.compareTo(BigDecimal.ZERO) >= 0) {
						redPacketOrderTCC.setCash(userRedPacket.getRemainCash());
					} else {
						redPacketOrderTCC.setCash(userRedPacket.getRemainCash().add(expDiscount));
					}
				}
				
				redPacketOrderTCC.setUserRedPacketId(userRedPacket.getId());
				redPacketOrderTCC.setUserId(userId);
				redPacketOrderTCC.setRedPacketOrderType(RedPacketOrderType.USE_RED_PACKET_FOR_EXPRESS);
				redPacketOrderTCC.setRedPacketHandlerType(ActivationHandlerType.DEFAULT);

				redPacketOrderTCCs.add(redPacketOrderTCC);
			}
		}
		
		redPacketParam.setRedPacketOrderTCCList(redPacketOrderTCCs);
		//均摊红包到每个sku
		avgRedPackets(redPacketParam);
	}

	private BigDecimal getTotalRetailPrice(List<SkuParam> list) {
		if (CollectionUtils.isEmpty(list)) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal total = BigDecimal.ZERO;
		
		for (SkuParam param : list) {
			total = total.add(param.getSalePrice().multiply(new BigDecimal(param.getCount())));
		}
		return total;
	}

	/**
	 * 红包均摊
	 * @param resultDTO
	 * @param cash 使用红包金额
	 */
	private void avgRedPackets(RedPacketParam packetParam) {
		List<SkuParam> params = packetParam.getSkuParams();
		
		if (CollectionUtils.isEmpty(params)) {
			return;
		}
		
		List<PromotionSkuItemDTO> list = new ArrayList<>(params.size());
		for (SkuParam skuParam : params) {
			PromotionSkuItemDTO dto = new PromotionSkuItemDTO();
			dto.setCartPrice(skuParam.getSalePrice());
			dto.setCount(skuParam.getCount());
			dto.setHbPrice(BigDecimal.ZERO);
			dto.setRetailPrice(skuParam.getSalePrice());
			dto.setSkuId(skuParam.getSkuId());
			dto.setOriRetailPrice(skuParam.getOriRPrice());
			list.add(dto);
		}
		
		avgItemPrice(null, list, packetParam.getUsedRedPrice(), ActivationConstants.ACTIVATION_TYPE_REDPACKETS);
		
		for (SkuParam param : params) {
			Iterator<PromotionSkuItemDTO> iterator = list.iterator();
			while (iterator.hasNext()) {
				PromotionSkuItemDTO dto = iterator.next();
				if (param.getSkuId() == dto.getSkuId()) {
					param.setRedSPrice(dto.getHbPrice());
					iterator.remove();
					break;
				}
			}
		}
		
	}

	public static void main(String[] args) {
		FavorCaculateServiceImpl impl = new FavorCaculateServiceImpl();
		
		RedPacketParam redPacketParam = new RedPacketParam();
		
		List<SkuParam> list = new ArrayList<>();
		SkuParam dto1 = new SkuParam();
		dto1.setSalePrice(new BigDecimal(3));
		dto1.setCount(1);
		dto1.setOriRPrice(new BigDecimal(3));

		SkuParam dto2 = new SkuParam();
		dto2.setSalePrice(new BigDecimal(2));
		dto2.setCount(1);
		dto2.setOriRPrice(new BigDecimal(2));

		list.add(dto1);
		list.add(dto2);
		
		redPacketParam.setSkuParams(list);
		
		List<UserRedPacketDTO> dtos = new ArrayList<>();
		UserRedPacketDTO packetDTO1 = new UserRedPacketDTO();
		packetDTO1.setId(1);
		packetDTO1.setCash(new BigDecimal(3));
		packetDTO1.setRemainCash(new BigDecimal(3));
		packetDTO1.setUsedCash(BigDecimal.ZERO);
		
		UserRedPacketDTO packetDTO2 = new UserRedPacketDTO();
		packetDTO2.setId(2);
		packetDTO2.setCash(new BigDecimal(3));
		packetDTO2.setRemainCash(new BigDecimal(3));
		packetDTO2.setUsedCash(BigDecimal.ZERO);
		
		UserRedPacketDTO packetDTO3 = new UserRedPacketDTO();
		packetDTO3.setId(3);
		packetDTO3.setCash(new BigDecimal(2));
		packetDTO3.setRemainCash(new BigDecimal(5));
		packetDTO3.setUsedCash(BigDecimal.ZERO);
		
		UserRedPacketDTO packetDTO4 = new UserRedPacketDTO();
		packetDTO4.setId(4);
		packetDTO4.setCash(new BigDecimal(2));
		packetDTO4.setRemainCash(new BigDecimal(2));
		packetDTO4.setUsedCash(BigDecimal.ZERO);
		
		dtos.add(packetDTO1);
		dtos.add(packetDTO2);
		dtos.add(packetDTO3);
		dtos.add(packetDTO4);
		
		redPacketParam.setExpressPrice(new BigDecimal(7));
		redPacketParam.setUsedRedPrice(new BigDecimal(13));
		
		impl.caculateRedPacketsDetail(redPacketParam, dtos);

		for (SkuParam dto : list) {
			System.out.println(dto.getSalePrice() + " " + dto.getRedSPrice());
		}
		
		System.out.println(redPacketParam.getExpressDiscountPrice());
	}
}
