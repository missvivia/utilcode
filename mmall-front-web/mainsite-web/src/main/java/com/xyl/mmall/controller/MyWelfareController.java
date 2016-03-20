/*
 * @(#) 2014-10-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.util.CodeInfoUtil;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.PromotionLock;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.FavorCaculateService;
import com.xyl.mmall.promotion.service.UserCouponService;
import com.xyl.mmall.promotion.service.UserRedPacketService;
import com.xyl.mmall.promotion.utils.ActivationUtils;
import com.xyl.mmall.util.AreaUtils;

/**
 * MyWelfareController.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-14
 * @since 1.0
 */
@Controller
public class MyWelfareController extends BaseController {

	@Autowired
	private UserCouponService userCouponService;
	
	@Autowired
	private CouponService couponService;

	@Autowired
	private UserRedPacketService userRedPacketService;
	
	@Autowired
	private CalCenterFacade calCenterFacade;
	
	@Autowired
	private FavorCaculateService favorCaculateService;
	
	@Autowired
	private LocationFacade locationFacade;

	@BILog(action = "page", type = "couponPage")
	@RequestMapping("/coupon")
	public String index(Model model) {
		long userId = SecurityContextUtils.getUserId();
		BigDecimal totalCash = userRedPacketService.getTotalCash(userId, new PromotionLock(userId));
		model.addAttribute("totalCash", totalCash);
		appendStaticMethod(model);
		return "pages/coupon/coupon";
	}
	

	@RequestMapping("/mycoupon/data/couponList")
	public @ResponseBody BaseJsonVO listCouponData(@RequestParam(value = "limit", required = false, defaultValue = "8") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "state", required = false, defaultValue = "-1") int state) {
		long userId = SecurityContextUtils.getUserId();

		List<CouponDTO> dtos = userCouponService.getUserCouponListByState(userId, state, limit, offset);
		int total = userCouponService.getUserCouponCount(userId, state);
		JSONObject json = new JSONObject();
		json.put("total", total);
		json.put("list", dtos);

		BaseJsonVO ret = new BaseJsonVO();
		ret.setCode(ResponseCode.RES_SUCCESS);
		ret.setResult(json);
		return ret;
	}
	
	
    // 获取当前进货单中可用的优惠券列表，by: durianskh@gmail.com
    // 当前进货单可用优惠券的条件是：1、优惠券区域与商店区域一致；2、进货单总价符合优惠券使用条件
    @RequestMapping("/mycoupon/data/couponList/canuse")
    public @ResponseBody BaseJsonVO listCouponDataCanUse(
            @RequestParam(value = "districtId", required = true) long districtId,
            @RequestParam(value = "grossPrice", required = true) BigDecimal grossPrice)
    {
        BaseJsonVO ret = new BaseJsonVO();
        long userId = SecurityContextUtils.getUserId();
        districtId = AreaUtils.getAreaCode();
        LocationCode locationCode = locationFacade.getLocationCodeByCode(districtId);
        if (locationCode == null) {
        	ret.setCodeAndMessage(ResponseCode.RES_ERROR, "区域错误！");
        	return ret;
        }
        long ctiyId = locationCode.getParentCode();
        // provinceId = 33L;
        
        // 获取全部可用的优惠券
        List<CouponDTO> dtos = userCouponService.getUserCouponListByState(userId,
                ActivationConstants.STATE_CAN_USE, Integer.MAX_VALUE, 0);
        
        JSONObject json = new JSONObject();
        int total = 0;
        List<CouponDTO> result = new ArrayList<CouponDTO>();
        
        if (dtos != null)
        {
            // 判断优惠券的区域与商店的区域是否一致
            Map<String, List<CouponDTO>> couponDTOMap = new HashMap<String, List<CouponDTO>>();
            for (CouponDTO couponDTO : dtos)
            {
                if (couponDTOMap.containsKey(couponDTO.getCouponCode()))
                {
                    couponDTOMap.get(couponDTO.getCouponCode()).add(couponDTO);
                }
                else
                {
                    List<CouponDTO> list = new ArrayList<CouponDTO>();
                    list.add(couponDTO);
                    couponDTOMap.put(couponDTO.getCouponCode(), list);
                }
            }
            
            Iterator<Map.Entry<String, List<CouponDTO>>> iter = couponDTOMap.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, List<CouponDTO>> entry = iter.next();
                Coupon coupon = couponService.getCouponByCode(entry.getKey(), true);
                if(coupon == null)
                {
                    iter.remove();
                    continue;
                }
                if ("000000".equals(coupon.getAreaIds()))
                {
                    // 优惠券全国可用
                    continue;
                }
                else
                {
                    if (Arrays.asList(coupon.getAreaIds().split(",")).contains(String.valueOf(ctiyId)))
                    {
                        // 优惠券在当前的商店所在区域可以使用
                        // 判断进货单总价是否满足优惠券使用条件
                        List<Activation> activations = ActivationUtils.containActivations(coupon
                                .getItems());
                        if (!CollectionUtils.isEmpty(activations))
                        {
                            boolean state = false;
                            for (Activation activation : activations)
                            {
                                Condition condition = activation.getCondition();
                                state = favorCaculateService.judgeCouponCondition(condition,
                                        grossPrice);
                                if (state)
                                {
                                    break;
                                }
                            }
                            if (state)
                            {
                                // 优惠券满足全部使用条件
                                continue;
                            }
                        }
                    }
                    
                    // 优惠券不满足使用条件
                    iter.remove();
                }
            }
            
            for (Map.Entry<String, List<CouponDTO>> entry : couponDTOMap.entrySet())
            {
                total += entry.getValue().size();
                result.addAll(entry.getValue());
            }
        }
        
        json.put("total", total);
        json.put("list", result);
        
        ret.setCode(ResponseCode.RES_SUCCESS);
        ret.setResult(json);
        return ret;
    }
	

	@RequestMapping("/mycoupon/data/redpacketList")
	public @ResponseBody
	Map<String, Object> listRedPacketData(
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(value = "state", required = false, defaultValue = "-1") int state) {
		long userId = SecurityContextUtils.getUserId();
		List<UserRedPacketDTO> dtos = userRedPacketService.getUserRedPacketList(userId, state, limit, offset);
		int total = userRedPacketService.getUserRedPacketCount(userId, state);
		Map<String, Object> jsonMap2 = new LinkedHashMap<>();
		jsonMap2.put("total", total);
		jsonMap2.put("list", dtos);

		Map<String, Object> jsonMap1 = new LinkedHashMap<>();
		jsonMap1.put("code", 200);
		jsonMap1.put("result", jsonMap2);
		return jsonMap1;
	}
	
	@RequestMapping("/mycoupon/bindCoupon")
	public @ResponseBody BaseJsonVO bindCoupon(@RequestParam(value = "couponCode", required = true) String couponCode,
			@RequestParam(value = "verifyCode", required = true) String verifyCode, HttpSession session) {
		BaseJsonVO ret = new BaseJsonVO();
		// 验证码校验
		long cur = System.currentTimeMillis();
		Long codeTime = (Long) session.getAttribute(CodeInfoUtil.KAPTCHA_SESSION_DATE);
		if (codeTime != null && cur - codeTime.longValue() < 5 * 60 * 1000) {
			String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if (!StringUtils.equalsIgnoreCase(code, verifyCode)) {
				ret.setCode(ResponseCode.RES_ERROR);
				ret.setMessage("验证码错误！");
				return ret;
			}
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("验证码超时！");
			return ret;
		}
		FavorCaculateResultDTO caculateResultDTO = calCenterFacade.bindCoupon(SecurityContextUtils.getUserId(),
				SecurityContextUtils.getUserName(), couponCode);
		if (caculateResultDTO == null) {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setResult(ActivationConstants.STATE_NOT_MATCH);
			ret.setMessage("优惠券不匹配！");
			return ret;
		}
		ret.setResult(caculateResultDTO.getCouponState());
		if (caculateResultDTO.getCouponState() == ActivationConstants.STATE_CAN_USE
				&& caculateResultDTO.getCouponDTO() != null) {
			ret.setCode(ResponseCode.RES_SUCCESS);
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
		}
		return ret;
	}
}
