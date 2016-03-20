/*
 * @(#) 2014-10-14
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.common.facade.LocationFacade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.mainsite.facade.CalCenterFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.ios.facade.pageView.couponList.CouponVO;
import com.xyl.mmall.mobile.ios.facade.param.CouponSearchParam;
import com.xyl.mmall.promotion.activity.Activation;
import com.xyl.mmall.promotion.activity.Condition;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.UserRedPacketDTO;
import com.xyl.mmall.promotion.meta.Coupon;
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
@RequestMapping("/m")
public class MobileMyWelfareController extends BaseController {

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

	@RequestMapping("/mycoupon/data/couponList")
	public @ResponseBody BaseJsonVO listCouponData(CouponSearchParam ddbParam) {
		long userId = SecurityContextUtils.getUserId();
		if (ddbParam.getLimit() == 0) {
			ddbParam.setLimit(8);
		}

		List<CouponDTO> dtos = new ArrayList<>();
		int total = 0;
		if (ddbParam.getGrossPrice() == null || ddbParam.getGrossPrice().intValue() == 0) {
			// dtos = userCouponService.getUserCouponListByState(userId,
			// ddbParam.getState(), ddbParam.getLimit(),
			// ddbParam.getOffset());
			// total = userCouponService.getUserCouponCount(userId,
			// ddbParam.getState());
			List<CouponDTO> sortAll = new ArrayList<>();

			List<CouponDTO> all = userCouponService.getUserCouponListByState(userId, ddbParam.getState(), Integer.MAX_VALUE, 0);
			List<CouponDTO> canUseList = new ArrayList<>();
			List<CouponDTO> beUsedList = new ArrayList<>();
			List<CouponDTO> expiredList = new ArrayList<>();
			for (int i = all.size() - 1; i >= 0; i--) {
				CouponDTO couponDTO = all.get(i);
				if (couponDTO.getEndTime() < getLastDate().getTime()) {
					all.remove(i);
					continue;
				}
				if (couponDTO.getCouponState() == ActivationConstants.STATE_CAN_USE) {
					canUseList.add(couponDTO);
				} else if (couponDTO.getCouponState() == ActivationConstants.STATE_EXPIRED) {
					expiredList.add(couponDTO);
				} else if (couponDTO.getCouponState() == ActivationConstants.STATE_HAS_BEAN_USED) {
					beUsedList.add(couponDTO);
				}

			}

			// 排序 1、可用，即将过期 2、可用 3、已使用 4、已过期
			Collections.sort(canUseList, new Comparator<CouponDTO>() {
				Date now = new Date();
				@Override
				public int compare(CouponDTO o1, CouponDTO o2) {
					long end1 = o1.getEndTime() - now.getTime();
					long end2 = o2.getEndTime() - now.getTime();
					if (end1 - end2 > 0) {
						return 1;
					} else if (end1 - end2 < 0) {
						return -1;
					} else {
						if (o1.getId() > o2.getId()) {
							return 1;
						} else {
							return -1;
						}
					}

				}
			});
			
			sortAll.addAll(canUseList);
			sortAll.addAll(beUsedList);
			sortAll.addAll(beUsedList);
			total = sortAll.size();
			
			int limit = ddbParam.getLimit();
			int offset = ddbParam.getOffset();
			int j=1;
			for (int i = offset; i < sortAll.size(); i++) {
				dtos.add(sortAll.get(i));
				j++;
				if(limit < j){
					break;
				}
			}

		} else {
			BaseJsonVO ret = new BaseJsonVO();
			long districtId = AreaUtils.getAreaCode();
			LocationCode locationCode = locationFacade.getLocationCodeByCode(districtId);
			if (locationCode == null) {
				ret.setCodeAndMessage(ResponseCode.RES_ERROR, "区域错误！");
				return ret;
			}
			long ctiyId = locationCode.getParentCode();
			// provinceId = 33L;

			// 获取全部可用的优惠券
			List<CouponDTO> dtosUsed = userCouponService.getUserCouponListByState(userId,
					ActivationConstants.STATE_CAN_USE, Integer.MAX_VALUE, 0);

			dtos = new ArrayList<CouponDTO>();

			if (dtosUsed != null) {
				// 判断优惠券的区域与商店的区域是否一致
				Map<String, List<CouponDTO>> couponDTOMap = new HashMap<String, List<CouponDTO>>();
				for (CouponDTO couponDTO : dtosUsed) {
					if (couponDTOMap.containsKey(couponDTO.getCouponCode())) {
						couponDTOMap.get(couponDTO.getCouponCode()).add(couponDTO);
					} else {
						List<CouponDTO> list = new ArrayList<CouponDTO>();
						list.add(couponDTO);
						couponDTOMap.put(couponDTO.getCouponCode(), list);
					}
				}

				Iterator<Map.Entry<String, List<CouponDTO>>> iter = couponDTOMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, List<CouponDTO>> entry = iter.next();
					Coupon coupon = couponService.getCouponByCode(entry.getKey(), true);
					if (coupon == null) {
						iter.remove();
						continue;
					}
					if ("000000".equals(coupon.getAreaIds())) {
						// 优惠券全国可用
						continue;
					} else {
						if (Arrays.asList(coupon.getAreaIds().split(",")).contains(String.valueOf(ctiyId))) {
							// 优惠券在当前的商店所在区域可以使用
							// 判断进货单总价是否满足优惠券使用条件
							List<Activation> activations = ActivationUtils.containActivations(coupon.getItems());
							if (!CollectionUtils.isEmpty(activations)) {
								boolean state = false;
								for (Activation activation : activations) {
									Condition condition = activation.getCondition();
									state = favorCaculateService.judgeCouponCondition(condition,
											ddbParam.getGrossPrice());
									if (state) {
										break;
									}
								}
								if (state) {
									// 优惠券满足全部使用条件
									continue;
								}
							}
						}

						// 优惠券不满足使用条件
						iter.remove();
					}
				}

				for (Map.Entry<String, List<CouponDTO>> entry : couponDTOMap.entrySet()) {
					total += entry.getValue().size();
					dtos.addAll(entry.getValue());
				}
			}
		}
		ddbParam.setTotalCount(total);
		List<CouponVO> listCoupon = new ArrayList<>();
		ConverToCouponVo(dtos, listCoupon);
		return Converter.pageBaseJsonVO(listCoupon, ddbParam);
	}

	/**
	 * 两个月之内的 组装并排序 1、可用，即将过期 2、可用 3、已使用 4、已过期
	 * 
	 * @param dtos
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private void ConverToCouponVo(List<CouponDTO> dtos, List<CouponVO> list) {

		Date date = getLastDate();
		if (CollectionUtils.isNotEmpty(dtos)) {
			CouponVO couponVO = null;
			for (CouponDTO couponDTO : dtos) {
				if (couponDTO.getEndTime() < date.getTime()) {
					continue;
				}
				couponVO = new CouponVO();
				couponVO.setId(couponDTO.getUserCouponId());
				couponVO.setEndTime(couponDTO.getEndTime());
				couponVO.setStartTime(couponDTO.getStartTime());
				couponVO.setStartDate(DateUtil.dateToString(new Date(couponDTO.getStartTime()), DateUtil.LONG_PATTERN));
				couponVO.setEndDate(DateUtil.dateToString(new Date(couponDTO.getEndTime()), DateUtil.LONG_PATTERN));
				couponVO.setState(couponDTO.getCouponState());
				couponVO.setCouponCode(couponDTO.getCouponCode());
				couponVO.setFavorType(couponDTO.getFavorType());
				List<Map<String, Object>> contentVOs = JsonUtils.fromJson(couponDTO.getItems(), List.class);

				list.add(couponVO);
				if (CollectionUtils.isNotEmpty(contentVOs)) {
					Map<String, Object> contentVO = contentVOs.get(0);
					if (contentVO == null || contentVO.isEmpty())
						continue;

					Map<String, Object> map = contentVOs.get(0);
					if (map.get("result") instanceof List) {
						List<Map<String, String>> results = (List<Map<String, String>>) map.get("result");
						if (CollectionUtils.isNotEmpty(results)) {
							Map<String, String> lMap = results.get(0);
							String facValue = lMap.get("value");
							if (facValue != null) {
								couponVO.setFacValue(facValue);
							}
						}

					}

					if (map.get("condition") instanceof Map) {
						Map<String, String> temp = (Map<String, String>) map.get("condition");
						String condition = temp.get("value");
						if (StringUtils.isNotBlank(condition)) {
							couponVO.setCondition(condition.substring(0, condition.length() - 1));
						}
					}

				}

			}
		}
	}

	private Date getLastDate() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -2);
		return calendar.getTime();
	}

	@RequestMapping("/mycoupon/data/redpacketList")
	public @ResponseBody Map<String, Object> listRedPacketData(
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
	public @ResponseBody BaseJsonVO bindCoupon(@RequestParam(value = "couponCode", required = true) String couponCode) {
		BaseJsonVO ret = new BaseJsonVO();
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
			ret.setMessage("添加成功");
		} else {
			ret.setCode(ResponseCode.RES_ERROR);
			ret.setMessage("添加失败");
		}
		return ret;
	}
}
