/*
 * @(#) 2014-10-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.tcc.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.exception.ServiceNoThrowException;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.TCCParamDTO;
import com.xyl.mmall.promotion.enums.CouponOrderType;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.meta.tcc.CouponOrderTCC;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.service.tcc.ActivityTCCService;
import com.xyl.mmall.promotion.service.tcc.CouponOrderTCCService;
import com.xyl.mmall.promotion.service.tcc.RedPacketOrderTCCService;

/**
 * ActivityTCCServiceImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-9
 * @since 1.0
 */
@Service("activityTCCService")
public class ActivityTCCServiceImpl implements ActivityTCCService {

	@Autowired
	private CouponOrderTCCService couponOrderTCCService;

	@Autowired
	private UserCouponService userCouponService;

	@Autowired
	private RedPacketOrderTCCService redPacketOrderTCCService;

	@Autowired
	private UserRedPacketService userRedPacketService;

    @Override
    @Transaction
    public boolean tryAddActivityTCC(long tranId, TCCParamDTO tccParamDTO,
            FavorCaculateResultDTO resultDTO)
    {
        if (resultDTO == null)
        {
            return true;
        }
        
        boolean succ = true;
        
        // 添加优惠券订单TCC
        try
        {
            List<CouponOrderTCC> couponOrderTCCs = resultDTO.getCouponOrderTCCList();
            if (!CollectionUtils.isEmpty(couponOrderTCCs))
            {
                for (CouponOrderTCC couponOrderTCC : couponOrderTCCs)
                {
                    couponOrderTCC.setOrderId(tccParamDTO.getOrderId());
                    couponOrderTCC.setUserId(tccParamDTO.getUserId());
                    // 使用优惠券
                    if (couponOrderTCC.getCouponOrderType() == CouponOrderType.USE_COUPON)
                    {
                        UserCoupon userCoupon = userCouponService.getUserCoupon(
                                tccParamDTO.getUserId(), couponOrderTCC.getUserCouponId(),
                                ActivationConstants.STATE_CAN_USE, true);
                        if (userCoupon == null)
                        {
                            throw new ServiceException("用户可用优惠券不存在");
                        }
                    }
                    
                    succ = succ
                            && couponOrderTCCService.tryAddCouponOrderTCC(tranId, couponOrderTCC) != null;
                    if (!succ)
                    {
                        throw new ServiceException("添加coupon order tcc异常");
                    }
                }
            }
            
            // // 添加红包tcc
            // List<RedPacketOrderTCC> redPacketOrderTCCs =
            // resultDTO.getRedPacketOrderTCCList();
            // if (!CollectionUtils.isEmpty(redPacketOrderTCCs))
            // {
            // for (RedPacketOrderTCC redPacketOrderTCC : redPacketOrderTCCs)
            // {
            // redPacketOrderTCC.setOrderId(tccParamDTO.getOrderId());
            // redPacketOrderTCC.setUserId(tccParamDTO.getUserId());
            //
            // // 判断红包使用金额是否足够
            // if (redPacketOrderTCC.getRedPacketOrderType() ==
            // RedPacketOrderType.USE_RED_PACKET)
            // {
            // UserRedPacket urp = userRedPacketService.getUserRedPacketById(
            // redPacketOrderTCC.getUserRedPacketId(),
            // redPacketOrderTCC.getUserId());
            // // 指定红包剩余金额
            // BigDecimal cash = urp.getRemainCash();
            // if (cash == null || cash.compareTo(redPacketOrderTCC.getCash()) < 0)
            // {
            // throw new ServiceException("红包可使用余额不足");
            // }
            // }
            //
            // succ = succ
            // && redPacketOrderTCCService.tryAddRedPacketOrderTCC(tranId,
            // redPacketOrderTCC) != null;
            // if (!succ)
            // {
            // throw new ServiceException("添加red packets order tcc异常");
            // }
            // }
            // }
        }
        catch (ServiceException e)
        {
            cancelAddActivityTCC(tranId);
            throw e;
        }
        return succ;
    }
    
    @Override
    @Transaction
    public boolean tryAddActivityTCC(long tranId, Map<Long, TCCParamDTO> tccParamDTOMap,
            Map<Long, FavorCaculateResultDTO> fcResultDTOMap)
    {
        if (fcResultDTOMap == null)
        {
            return true;
        }
        
        boolean succ = true;
        
        // 添加优惠券订单TCC
        try
        {
            for (Map.Entry<Long, FavorCaculateResultDTO> entry : fcResultDTOMap.entrySet())
            {
                List<CouponOrderTCC> couponOrderTCCs = entry.getValue().getCouponOrderTCCList();
                if (!CollectionUtils.isEmpty(couponOrderTCCs))
                {
                    for (CouponOrderTCC couponOrderTCC : couponOrderTCCs)
                    {
                        TCCParamDTO tccParamDTO = tccParamDTOMap.get(entry.getKey());
                        couponOrderTCC.setOrderId(tccParamDTO.getOrderId());
                        couponOrderTCC.setUserId(tccParamDTO.getUserId());
                        // 使用优惠券
                        if (couponOrderTCC.getCouponOrderType() == CouponOrderType.USE_COUPON)
                        {
                            UserCoupon userCoupon = userCouponService.getUserCoupon(
                                    tccParamDTO.getUserId(), couponOrderTCC.getUserCouponId(),
                                    ActivationConstants.STATE_CAN_USE, true);
                            if (userCoupon == null)
                            {
                                throw new ServiceException("用户可用优惠券不存在");
                            }
                        }
                        
                        succ = succ
                                && couponOrderTCCService.tryAddCouponOrderTCC(tranId,
                                        couponOrderTCC) != null;
                        if (!succ)
                        {
                            throw new ServiceException("添加coupon order tcc异常");
                        }
                    }
                }
            }
        }
        catch (ServiceException e)
        {
            cancelAddActivityTCC(tranId);
            throw e;
        }
        return succ;
    }

	@Override
	@Transaction
	public boolean confirmAddActivityTCC(long tranId) {
		// 优惠券订单确定
		boolean isSucc = true;
		try {
			isSucc = isSucc && couponOrderTCCService.confirmAddCouponOrderTCC(tranId);

			if (!isSucc) {
				throw new ServiceException("确认CouponOrderTCC异常");
			}

//			// 红包确认
//			isSucc = isSucc && redPacketOrderTCCService.confirmAddRedPacketOrderTCC(tranId);
//
//			if (!isSucc) {
//				throw new ServiceException("确认RedPacketOrderTCC异常");
//			}
		} catch (Exception e) {
			cancelAddActivityTCC(tranId);
			throw e;
		}

		return isSucc;
	}

	@Override
	@Transaction
	public boolean cancelAddActivityTCC(long tranId) {
		// 优惠券订单确定
		boolean status = couponOrderTCCService.cancelAddCouponOrderTCC(tranId);

		if (!status) {
			throw new ServiceNoThrowException("删除CouponOrderTCC异常");
		}

//		// 红包订单确认
//		status = redPacketOrderTCCService.cancelAddRedPacketOrderTCC(tranId);
//
//		if (!status) {
//			throw new ServiceNoThrowException("删除RedPacketOrderTCC异常");
//		}

		return status;
	}

}
